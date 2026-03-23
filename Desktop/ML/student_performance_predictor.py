import os
import warnings

import kagglehub
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
from sklearn.compose import ColumnTransformer
from sklearn.linear_model import LinearRegression
from sklearn.metrics import accuracy_score, mean_absolute_error, mean_squared_error, r2_score
from sklearn.model_selection import train_test_split
from sklearn.pipeline import Pipeline
from sklearn.preprocessing import OneHotEncoder


warnings.filterwarnings("ignore")


DATASET_ID = "prajwalkanade/student-performance-prediction-dataset"
RANDOM_STATE = 42
TEST_SIZE = 0.2
K_CATEGORICAL_FEATURES = 8  # pandas-based feature selection (drop lower-ranked categorical columns)


GRADE_TO_SCORE = {
    "Fail": 0,
    "DD": 1,
    "DC": 2,
    "CC": 3,
    "CB": 4,
    "BB": 5,
    "BA": 6,
    "AA": 7,
}

PASS_GRADES = {"AA", "BA", "BB", "CB", "CC"}  # everything else is considered "Fail"


def download_dataset() -> str:
    """Download dataset locally and return the folder path."""
    path = kagglehub.dataset_download(DATASET_ID)
    # The archive folder usually contains exactly one CSV for this dataset.
    for root, _, files in os.walk(path):
        for f in files:
            if f.lower().endswith(".csv") and "students performance" in f.lower():
                return os.path.join(root, f)
    raise FileNotFoundError("Could not locate the CSV inside the Kaggle download.")


def load_and_prepare_dataframe(csv_path: str) -> pd.DataFrame:
    df = pd.read_csv(csv_path)

    # Basic cleanup
    df = df.copy()

    # Fill missing scholarship with mode (pandas step)
    if df["Scholarship"].isna().any():
        df["Scholarship"] = df["Scholarship"].fillna(df["Scholarship"].mode().iloc[0])

    # Drop obvious identifier (we do it in pandas, before feature selection/model)
    # We'll keep it separate so feature-selection can be explicit.
    return df


def pandas_feature_selection(df: pd.DataFrame, k_categorical: int) -> list[str]:
    """
    Select a subset of features using pandas:
    - Keep Weekly_Study_Hours always (numeric)
    - For each categorical column, score it by how much the mean GradeScore varies across categories
    """
    candidate_cols = [c for c in df.columns if c not in ["Student_ID", "Grade"]]

    # Ensure GradeScore exists
    if "GradeScore" not in df.columns:
        raise ValueError("GradeScore must be created before feature selection.")

    numeric_cols = [c for c in candidate_cols if pd.api.types.is_numeric_dtype(df[c])]
    cat_cols = [c for c in candidate_cols if c not in numeric_cols]

    # Always keep numeric columns
    selected = list(numeric_cols)

    # Score categorical columns by std deviation of group means
    scores = {}
    for col in cat_cols:
        group_means = df.groupby(col)["GradeScore"].mean()
        scores[col] = float(group_means.std(skipna=True)) if group_means.shape[0] > 1 else 0.0

    ranked = sorted(scores.items(), key=lambda x: x[1], reverse=True)
    top_k = ranked[: min(k_categorical, len(ranked))]
    selected += [col for col, _ in top_k]

    return selected


def add_targets(df: pd.DataFrame) -> pd.DataFrame:
    df = df.copy()
    df["GradeScore"] = df["Grade"].map(GRADE_TO_SCORE).astype(int)
    df["Pass"] = df["Grade"].isin(PASS_GRADES).astype(int)
    return df


def score_to_grade(score: np.ndarray) -> np.ndarray:
    # Convert regression output back to the nearest grade score
    score_int = np.rint(score).astype(int)
    score_int = np.clip(score_int, 0, 7)
    rev = {v: k for k, v in GRADE_TO_SCORE.items()}
    return np.array([rev[i] for i in score_int], dtype=object)


def build_preprocessor_from_df(df: pd.DataFrame, feature_cols: list[str]) -> tuple[ColumnTransformer, list[str], list[str]]:
    categorical_cols = []
    numeric_cols = []
    for c in feature_cols:
        if pd.api.types.is_numeric_dtype(df[c]):
            numeric_cols.append(c)
        else:
            categorical_cols.append(c)

    preprocessor = ColumnTransformer(
        transformers=[
            ("cat", OneHotEncoder(handle_unknown="ignore"), categorical_cols),
            ("num", "passthrough", numeric_cols),
        ]
    )
    return preprocessor, categorical_cols, numeric_cols


def plot_actual_vs_predicted(y_true: np.ndarray, y_pred: np.ndarray, title: str, out_path: str) -> None:
    plt.figure(figsize=(6.5, 5))
    plt.scatter(y_true, y_pred, alpha=0.8)
    min_v = min(np.min(y_true), np.min(y_pred))
    max_v = max(np.max(y_true), np.max(y_pred))
    plt.plot([min_v, max_v], [min_v, max_v], "r--", linewidth=1)
    plt.xlabel("Actual")
    plt.ylabel("Predicted")
    plt.title(title)
    plt.tight_layout()
    plt.savefig(out_path, dpi=160)
    plt.close()


def plot_confusion_matrix(y_true: np.ndarray, y_pred: np.ndarray, title: str, out_path: str) -> None:
    from sklearn.metrics import confusion_matrix

    fig, ax = plt.subplots(figsize=(5.5, 4.8))
    cm = confusion_matrix(y_true, y_pred, labels=[0, 1])
    im = ax.imshow(cm, cmap="Blues")
    fig.colorbar(im, ax=ax)
    ax.set(
        xticks=np.arange(2),
        yticks=np.arange(2),
        xticklabels=["Fail", "Pass"],
        yticklabels=["Fail", "Pass"],
        ylabel="Actual",
        xlabel="Predicted",
        title=title,
    )
    for (i, j), v in np.ndenumerate(cm):
        ax.text(j, i, str(v), ha="center", va="center", color="black", fontsize=12)
    plt.tight_layout()
    plt.savefig(out_path, dpi=160)
    plt.close(fig)


def main() -> None:
    out_dir = os.path.join(os.path.dirname(__file__), "outputs")
    os.makedirs(out_dir, exist_ok=True)

    csv_path = download_dataset()
    print(f"Loaded dataset from: {csv_path}")

    df = load_and_prepare_dataframe(csv_path)
    df = add_targets(df)

    # Feature selection using pandas
    feature_cols = pandas_feature_selection(df, k_categorical=K_CATEGORICAL_FEATURES)
    # Ensure we don't accidentally include targets/ids
    feature_cols = [c for c in feature_cols if c not in ["Student_ID", "Grade", "GradeScore", "Pass"]]
    print("\nSelected features (pandas-based):")
    for c in feature_cols:
        print(f"- {c}")

    X = df[feature_cols]

    # -------------------------
    # Model A: Linear Regression for GradeScore
    # -------------------------
    y_grade = df["GradeScore"].values
    X_train, X_test, y_train, y_test = train_test_split(
        X, y_grade, test_size=TEST_SIZE, random_state=RANDOM_STATE
    )

    preprocessor, _, _ = build_preprocessor_from_df(df, feature_cols)
    model_grade = Pipeline(
        steps=[
            ("preprocess", preprocessor),
            ("lr", LinearRegression()),
        ]
    )
    model_grade.fit(X_train, y_train)
    y_pred_grade = model_grade.predict(X_test)

    r2 = r2_score(y_test, y_pred_grade)
    mae = mean_absolute_error(y_test, y_pred_grade)
    # Older scikit-learn versions may not support squared=False; compute RMSE manually.
    rmse = float(np.sqrt(mean_squared_error(y_test, y_pred_grade)))

    # Convert predicted scores back to grade labels for grade-accuracy (%)
    y_test_grade_labels = df.loc[X_test.index, "Grade"].values
    y_pred_grade_labels = score_to_grade(y_pred_grade)
    grade_accuracy = accuracy_score(y_test_grade_labels, y_pred_grade_labels)

    print("\n=== GradeScore (Regression with Linear Regression) ===")
    print(f"R^2: {r2:.4f}")
    print(f"MAE: {mae:.4f}")
    print(f"RMSE: {rmse:.4f}")
    print(f"Grade accuracy (predicted grade label): {grade_accuracy * 100:.2f}%")

    plot_actual_vs_predicted(
        y_true=y_test,
        y_pred=y_pred_grade,
        title="GradeScore: Actual vs Predicted (Linear Regression)",
        out_path=os.path.join(out_dir, "actual_vs_predicted_gradescore.png"),
    )

    # -------------------------
    # Model B: Linear Regression for Pass/Fail (derived target)
    # -------------------------
    y_pass = df["Pass"].values
    X_train2, X_test2, y_train2, y_test2 = train_test_split(
        X, y_pass, test_size=TEST_SIZE, random_state=RANDOM_STATE
    )

    model_pass = Pipeline(
        steps=[
            ("preprocess", preprocessor),
            ("lr", LinearRegression()),
        ]
    )
    model_pass.fit(X_train2, y_train2)

    y_pred_pass_cont = model_pass.predict(X_test2)
    y_pred_pass = (y_pred_pass_cont >= 0.5).astype(int)

    pass_acc = accuracy_score(y_test2, y_pred_pass)
    print("\n=== Pass/Fail (Derived from Grade) ===")
    print(f"Pass/Fail accuracy (threshold=0.5): {pass_acc * 100:.2f}%")

    plot_confusion_matrix(
        y_true=y_test2,
        y_pred=y_pred_pass,
        title="Pass/Fail Confusion Matrix (Linear Regression + Threshold)",
        out_path=os.path.join(out_dir, "confusion_matrix_pass_fail.png"),
    )

    print(f"\nPlots saved to: {out_dir}")
    print("Done.")


if __name__ == "__main__":
    main()

