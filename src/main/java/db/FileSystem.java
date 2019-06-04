package db;

import config.Constants;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileSystem {

    public static void store(String filepath, String content) {
        File file = new File(filepath);
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String read(String folderPath, String filenameRegex) throws Exception {
        File folder = new File(folderPath);
        String[] files = folder.list();
        if (files == null || files.length == 0) return null;
        for (String filename : files) {
            if (filename.matches(filenameRegex))
                return read(folderPath + filename);
        }
        throw new Exception("File with regex " + filenameRegex + " doesn't exist");
    }

    public static String read(String filepath) {
        String input = "";
        File file = new File(filepath);
        if (!file.exists()) return null;
        try {
            FileReader fileReader = new FileReader(file);
            while (fileReader.ready()) {
                input += (char)fileReader.read();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return input;
    }

    public static void makeDirectory(String path) {
        File fs = new File(path);
        if (fs.exists()) return;
        if (fs.mkdir()) {
            System.out.println("Directory created");
        } else {
            System.out.println("Failed to create directory");
        }
    }

    public static void removeAll(String directory) {
        File folder = new File(directory);
        File[] files = folder.listFiles();
        if (files == null) return;
        for (File file : files) {
            if (file.isDirectory()) {
                removeAll(file.getPath());
            } else {
                file.delete();
            }
        }
    }

}
