# Student Performance Predictor (Linear Regression)

This project predicts student performance using **Linear Regression** with preprocessing/feature selection via **pandas**, and visualizes results using **matplotlib**.

## Reference Project Link
You can also refer to this related project: [Mrstragr/SPP](https://github.com/Mrstragr/SPP)

## Dataset
Kaggle dataset: `prajwalkanade/student-performance-prediction-dataset`

The CSV file used is `Students Performance .csv` with columns:
`Student_ID, Student_Age, Sex, High_School_Type, Scholarship, Additional_Work, Sports_activity, Transportation, Weekly_Study_Hours, Attendance, Reading, Notes, Listening_in_Class, Project_work, Grade`

### Target column
- `Grade` (categorical): `AA, BA, BB, CB, CC, DC, DD, Fail`

To support both regression-style evaluation and pass/fail evaluation, the script derives two targets:

## Targets
### 1) Numeric target (for regression): `GradeScore`
Grades are mapped to an ordered score:
| Grade | GradeScore |
|---|---:|
| Fail | 0 |
| DD | 1 |
| DC | 2 |
| CC | 3 |
| CB | 4 |
| BB | 5 |
| BA | 6 |
| AA | 7 |

### 2) Pass/Fail target: `Pass`
`Pass = 1` if `Grade` is one of `AA, BA, BB, CB, CC`, otherwise `Pass = 0`.

## Preprocessing (pandas + sklearn)
1. **Missing values**
   - Fills missing `Scholarship` with the mode.
2. **Encoding categorical features**
   - Uses `OneHotEncoder(handle_unknown="ignore")` for non-numeric columns.
3. **Weekly hours**
   - `Weekly_Study_Hours` is treated as a numeric feature (`passthrough`).

## Feature selection (pandas-based)
The script selects a subset of features using a simple pandas ranking rule:
- Keeps **all numeric** candidate features.
- For each categorical feature, it computes the **standard deviation of the mean `GradeScore` across categories**.
- It then keeps the top `K` categorical features (default `K_CATEGORICAL_FEATURES = 8`).

## Models
### Model A: Linear Regression for `GradeScore`
- Learns `GradeScore` from selected features.
- Evaluation metrics:
  - `R^2`
  - `MAE`
  - `RMSE` (computed as `sqrt(MSE)`)
- “Grade accuracy”:
  - Predicted numeric `GradeScore` is rounded to the nearest integer in `[0..7]`,
  - Converted back to grade labels (`AA..Fail`),
  - Compared to true `Grade`.

### Model B: Linear Regression for `Pass/Fail`
- Learns `Pass` (0/1) as a regression problem using Linear Regression.
- Converts predicted continuous output to class:
  - `Pass` if prediction >= `0.5`, else `Fail`
- Metric:
  - Pass/Fail accuracy
- Plot:
  - confusion matrix

## Output plots
Plots are saved into `./outputs/`:
- `actual_vs_predicted_gradescore.png`
- `confusion_matrix_pass_fail.png`

## How to run
```bash
python3 student_performance_predictor.py
```

## Requirements
The script uses:
- `kagglehub`
- `pandas`
- `scikit-learn`
- `matplotlib`
- `numpy`

## Notes / Limitations
- Linear Regression is used for both tasks (even pass/fail), so it may not be as strong as classifiers (e.g., logistic regression).
- Because the dataset is small (145 rows), results may vary depending on the train/test split.

