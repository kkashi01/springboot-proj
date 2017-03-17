package com.example.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// \tools\curl.exe -F file=@"c://tmp/pom.xml"  http://localhost:8080/upload/
// \tools\curl.exe http://localhost:8080/download/
//  or
// tools\curl.exe "http://localhost:8080/download[?fileName=pom.xml_META.txt][&download=true|false]"
// "C:\Program Files (x86)\GnuWin32\bin\wget.exe" -O foo.txt "http://localhost:8080/download?fileName=pom.xml_META.txt&download=true"

/**
 * Rest service to upload files onto server. For each upload, it will also create
 * a matching file that ends with _META.txt. This text file will contain some
 * basic meta data information
 *
 */
@RestController
public class UploadControllerRest {

    private final Logger logger = LoggerFactory.getLogger(UploadControllerRest.class);

    //Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "/tmp/uploadFolder/";

    // Meta data file will have a suffix like below
    private static String META_DATA_SUFFIX = "_META.txt";


    @PostMapping("/rest/upload")
    // If not @RestController, uncomment this
    //@ResponseBody
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {

        List<String> result = new ArrayList<>();
        if (file == null ||  file.isEmpty()) {
            return new ResponseEntity("please select a file!", HttpStatus.BAD_REQUEST);
        }

        try {
            saveFiles(Arrays.asList(file));
            String fileAttribData = getFileAttribs(UPLOADED_FOLDER + file.getOriginalFilename());

            // Get some basic file attributes and then write it into a matching fiel with suffix
            Path uploadPathMeta = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename() + META_DATA_SUFFIX);
            Files.write(uploadPathMeta, fileAttribData.getBytes());

        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // file uploaded and meta data file written
        return new ResponseEntity("Successfully uploaded - " +
                file.getOriginalFilename(), new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * Method to get some basic file attributes
     * @param filePath
     * @return file attributes
     */
    private String getFileAttribs(String filePath) {
        String result = "";
        Path file = Paths.get(filePath);

        try {
            BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);
            result = String.format("loadedFrom: %s" +
                            "\r\ncreationTime: %s" +
                            "\r\nsize: %s" +
                            "\r\nisDir: %s",
                    filePath,
                    attr.creationTime(),
                    attr.size(),
                    attr.isDirectory());
        } catch (IOException e) {
            result = e.getLocalizedMessage();
        }

        return result;
    }

    private void saveFiles(List<MultipartFile> files) throws IOException {
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }

            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);

            // Now write the meta data file

        }

    }
}