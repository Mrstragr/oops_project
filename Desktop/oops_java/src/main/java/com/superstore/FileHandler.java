package com.superstore;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * FileHandler class for saving and loading data to/from files.
 */
public class FileHandler {

    private static final String DATA_DIR = "resources/";

    static {
        new File(DATA_DIR).mkdirs();
    }

    /**
     * Save object to file.
     */
    public static void saveObject(String filename, Object obj) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_DIR + filename))) {
            oos.writeObject(obj);
        }
    }

    /**
     * Load object from file.
     */
    @SuppressWarnings("unchecked")
    public static <T> T loadObject(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_DIR + filename))) {
            return (T) ois.readObject();
        }
    }

    /**
     * Save list to file.
     */
    public static <T> void saveList(String filename, List<T> list) throws IOException {
        saveObject(filename, list);
    }

    /**
     * Load list from file.
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> loadList(String filename) throws IOException, ClassNotFoundException {
        return (List<T>) loadObject(filename);
    }

    /**
     * Save map to file.
     */
    public static <K, V> void saveMap(String filename, Map<K, V> map) throws IOException {
        saveObject(filename, map);
    }

    /**
     * Load map from file.
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> loadMap(String filename) throws IOException, ClassNotFoundException {
        return (Map<K, V>) loadObject(filename);
    }
}
