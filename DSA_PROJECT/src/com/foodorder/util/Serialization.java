package com.foodorder.util;

import java.io.*;
import java.util.List;

public class Serialization {

    // Serialize a single object to a file
    public static void saveObject(Object obj, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filename))) {
            oos.writeObject(obj);
            System.out.println("✅ Data saved to " + filename);
        } catch (IOException e) {
            System.out.println("❌ Error saving data: " + e.getMessage());
        }
    }

    // Deserialize a single object from a file
    public static Object loadObject(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("⚠️ No saved data found at " + filename);
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(filename))) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("❌ Error loading data: " + e.getMessage());
            return null;
        }
    }

    // Generic method for loading a list of objects
    @SuppressWarnings("unchecked")
    public static <T> List<T> loadObjectList(String filename) {
        Object obj = loadObject(filename);
        if (obj != null && obj instanceof List<?>) {
            return (List<T>) obj;
        }
        return null;
    }
}
