package db;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileSystemStore {

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

}
