package db;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileStore {

    public static void store(String filepath, String content) {
        try {
            FileWriter fileWriter = new FileWriter(filepath);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String read(String filepath) {
        String input = "";
        try {
            FileReader fileReader = new FileReader(filepath);
            while (fileReader.ready()) {
                input += (char)fileReader.read();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return input;
    }

}
