package db;

import config.Constants;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class FileStoreTest {

    @Test
    public void storeAndReadOutput() {
        String testOutput = "{\"value\": \"key\"}";
        String filepath = Constants.ROOT_DIR + "test.txt";
        FileSystemStore.store(filepath, testOutput);

        assertEquals(FileSystemStore.read(filepath), testOutput);
        removeFile(filepath);
    }

    private void removeFile(String path) {
        new File(path).delete();
    }

}