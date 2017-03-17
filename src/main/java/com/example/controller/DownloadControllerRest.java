package com.example.controller;

import com.example.beans.FileInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class DownloadControllerRest {

    //Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "/tmp/uploadFolder/";

    // Meta data file will have a suffix like below
    private static String META_DATA_SUFFIX = "_META.txt";

    // https://spring.io/guides/gs/rest-service/
//    @RequestMapping(value = "/download", method = RequestMethod.GET, produces = "application/pdf")
    @RequestMapping(value="/rest/download", method = RequestMethod.GET, produces = "application/*")
    public ResponseEntity<?> singleFileDownload(
             @RequestParam(value = "fileName", required=false) String fileName
            ,@RequestParam(value = "download", required=false) String download) {

        List<String> result = new ArrayList<>();

        // =============== if user didn't specify an input filename,
        // then print listing of all files, filtered by META_DATA_SUFFIX
        if (StringUtils.isEmpty(fileName)) {
            Path downloadPath = Paths.get(UPLOADED_FOLDER);
            try {

                // Filter directory based on META_DATA_SUFFIX
//                DirectoryStream<Path> stream = Files.newDirectoryStream(downloadPath, "*{" + META_DATA_SUFFIX + "}");
                DirectoryStream<Path> stream = Files.newDirectoryStream(downloadPath);
                result.add("Please specify META file name. Available files:");
                for (Path path : stream) {
                    result.add(path.getFileName().toString());
                }
                stream.close();
            } catch (IOException e) {
                //todo: for now, if any error, just print it to display
                result.add("Error: " + e.getMessage());
            }

            return new ResponseEntity(result.toString(), HttpStatus.BAD_REQUEST);
        }


        // =================== filename passed in.
        // Try parsing it and returning the result
        String fileContent;
        try {
            Path path = Paths.get(UPLOADED_FOLDER + fileName);
            fileContent = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);

            // We want to print each metadata line as a separate row;  hence break it up into multiple rows
            //  and then add it to our result
            String[] fileContentRows = fileContent.split("(\r\n|\n)");
            Collections.addAll(result, fileContentRows);

            // Now check and see if user wants to download the file; i.e. download=true
            if (Boolean.valueOf(download)) {
                ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

//                InputStreamResource resource = new InputStreamResource(new FileInputStream(UPLOADED_FOLDER + fileName));

                HttpHeaders headers = new HttpHeaders();
                headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
                headers.add("Pragma", "no-cache");
                headers.add("Expires", "0");

                return ResponseEntity.ok()
                        .headers(headers)
                        .contentLength(Files.readAllBytes(path).length)
                        .contentType(MediaType.parseMediaType("application/octet-stream"))
                        .body(resource);
            }

            return new ResponseEntity(result.toString(), HttpStatus.OK);
        } catch (IOException e) {
            //todo: exception; e.g. file not found. For now, just return a simple message
            fileContent = "Either file not found or error in reading it! " + e.getMessage();
            result.add(fileContent);
            return new ResponseEntity(result.toString(), HttpStatus.BAD_REQUEST);
        }

    }

}