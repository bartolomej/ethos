package db;

import config.Constants;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class FileSystemStoreTest {

    @Test
    public void storeAndReadOutput() {
        String testOutput = "{\"value\": \"key\"}";
        String filepath = Constants.ROOT_DIR + "test.txt";
        FileSystem.store(filepath, testOutput);

        assertEquals(FileSystem.read(filepath), testOutput);
        removeFile(filepath);
    }

    @Test
    public void testRegexFileSearch() throws Exception {
        String testOutput = "{\"value\": \"key\"}";
        String filepath = Constants.ROOT_DIR + "1_t8c7bckjccwc4t.txt";
        FileSystem.store(filepath, testOutput);

        String output = FileSystem.read(Constants.ROOT_DIR, "1_.+");

        assertEquals(output, testOutput);
        removeFile(filepath);
    }

    private void removeFile(String path) {
        new File(path).delete();
    }

}