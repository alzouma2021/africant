package com.africanb.africanb.utilities;

import com.africanb.africanb.utils.document.DocumentUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
@SpringBootTest
public class DocumentUtilsTests {

    @Test
    public void testCreateFileOnDiskHard() throws IOException {
        byte[] content = "Hello, this is a test content.".getBytes();
        String fileLocation = "testFile.txt";

        assertTrue(DocumentUtils.createFileOnDiskHard(content, fileLocation));
        assertTrue(new File(fileLocation).exists());
        assertEquals("Hello, this is a test content.", new String(Files.readAllBytes(Paths.get(fileLocation))));
        assertTrue(new File(fileLocation).delete());
    }

    @Test
    public void testCheckIfDirectoryExists() {
        String existingDirectory = "src";
        String nonExistingDirectory = "nonExistentDirectory";

        assertTrue(DocumentUtils.checkIfDirectoryExists(existingDirectory));
        assertFalse(DocumentUtils.checkIfDirectoryExists(nonExistingDirectory));
    }

    @Test
    public void testCheckIfDocumentExistsOnDirectory() throws IOException {
        String existingFile = "existingFile.txt";
        String nonExistingFile = "nonExistentFile.txt";
        byte[] content = "Hello, this is a test content.".getBytes();

        DocumentUtils.createFileOnDiskHard(content, existingFile);

        assertTrue(DocumentUtils.checkIfDocumentExistsOnDirectory(existingFile));
        assertFalse(DocumentUtils.checkIfDocumentExistsOnDirectory(nonExistingFile));
        assertTrue(new File(existingFile).delete());
    }

    @Test
    public void testCreateDirectoryOnHardDisk() {
        String directoryPath = "testDirectory";

        assertTrue(DocumentUtils.createDirectoryOnHardDisk(directoryPath));
        assertTrue(new File(directoryPath).exists());
        assertTrue(new File(directoryPath).delete());
    }

    @Test
    public void testCompareFileSizeToLimitSize() {
        MockMultipartFile file = new MockMultipartFile("testFile", "testFile.txt", "text/plain", "Hello, this is a test content.".getBytes());

        assertTrue(DocumentUtils.compareFileSizeToLimitSize(file, 1.0));
    }

}
