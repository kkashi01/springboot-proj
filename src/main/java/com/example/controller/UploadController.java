package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

@Controller
public class UploadController {

    //Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "c://tmp//uploadFolder//";

    // Meta data file will have a suffix like below
    private static String META_DATA_SUFFIX = "_META.txt";

//    @GetMapping("/")
//    public String index() {
//        return "upload";
//    }

    @GetMapping("/upload")
    public String index() {
        return "upload";
    }

    @PostMapping("/upload") // //new annotation since 4.3
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:uploadStatus";
        }

        try {

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path uploadPath = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Path uploadPathMeta = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename() + META_DATA_SUFFIX);

            //Create dirs if doesn't exist
            Files.createDirectories(Paths.get(UPLOADED_FOLDER));


            Files.write(uploadPath, bytes);
            String fileAttribData = getFileAttribs(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(uploadPathMeta, fileAttribData.getBytes());

            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + file.getOriginalFilename() + "'");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/uploadStatus";
    }

    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }

    private String getFileAttribs(String absoluteFilePath) {
        String result = "";
        Path file = Paths.get(absoluteFilePath);

        try {
            BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);
            result = String.format("loadedFrom: %s" +
                            "\r\ncreationTime: %s" +
                            "\r\nsize: %s" +
                            "\r\nisDir: %s",
                    absoluteFilePath,
                    attr.creationTime(),
                    attr.size(),
                    attr.isDirectory());
        } catch (IOException e) {
            result = "Error retrieving file attribs: " + e.getMessage();
        }

        return result;
    }
}