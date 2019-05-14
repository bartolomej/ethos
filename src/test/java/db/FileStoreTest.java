package db;

import config.Constants;
import org.junit.Test;

import static org.junit.Assert.*;

public class FileStoreTest {

    @Test
    public void storeAndReadOutput() {
        String testOutput = "{\"value\": \"key\"}";
        String filepath = Constants.ROOT_DIR + "test.txt";
        FileStore.store(filepath, testOutput);

        assertEquals(FileStore.read(filepath), testOutput);
    }

}