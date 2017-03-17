package com.example.beans;

import org.junit.Test;
import static org.junit.Assert.*;


public class FileInfoTest {
    @Test
    public void getFileName() throws Exception {
        FileInfo fileInfo = new FileInfo("foo");
        assertEquals(fileInfo.getMessage(), "foo");
    }

}