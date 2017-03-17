package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class DownloadController {

    //Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "/tmp/uploadFolder/";

    // Meta data file will have a suffix like below
    private static String META_DATA_SUFFIX = "_META.txt";

    @GetMapping("/download")
    public String index() {
        return "download";
    }

    @RequestMapping("/download")
    public String singleFileDownload(@RequestParam("fileName") String fileName,
                                     RedirectAttributes redirectAttributes) {

        List<String> result = new ArrayList<>();

        // =============== if user didn't specify an input filename,
        // then print listing of all files, filtered by META_DATA_SUFFIX
        if (fileName.isEmpty()) {
            Path downloadPath = Paths.get(UPLOADED_FOLDER);
            DirectoryStream<Path> stream = null;
            try {
                stream = Files.newDirectoryStream(downloadPath, "*{" + META_DATA_SUFFIX + "}");

                result.add("Please specify META file name. Available files:");
                for (Path path : stream) {
                    result.add(path.getFileName().toString());
                }
                stream.close();

                redirectAttributes.addFlashAttribute("message", result);
            } catch (IOException e) {
                //todo: for now, if any error, just print it to display
                result.add("Error: " + e.getMessage());
            }

            return "redirect:downloadStatus";
        }


        // =================== filename passed in.
        // Try parsing it and returning the result
        String fileContent;
        try {
            fileContent = new String(Files.readAllBytes(Paths.get(UPLOADED_FOLDER + fileName)), StandardCharsets.UTF_8);
            result.add("File content is:");

            // We want to print each metadata line as a separate row;  hence break it up into multiple rows
            //  and then add it to our result
            String[] fileContentRows = fileContent.split("(\r\n|\n)");
            Collections.addAll(result, fileContentRows);
        } catch (IOException e) {
            //todo: exception; e.g. file not found. For now, just return a simple message
            fileContent = "Either file not found or error in reading it! " + e.getMessage();
            result.add(fileContent);
        }

        System.out.println("file content is: " + fileContent);
        redirectAttributes.addFlashAttribute("message", result);

        return "redirect:/downloadStatus";
    }

    @GetMapping("/downloadStatus")
    public String downloadStatus() {
        return "downloadStatus";
    }

}