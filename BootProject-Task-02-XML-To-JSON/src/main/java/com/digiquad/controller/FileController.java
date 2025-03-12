package com.digiquad.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.digiquad.service.XmlToJsonService;

@Controller
public class FileController {

    private final XmlToJsonService xmlToJsonService;

    @Value("${json.output.directory}") 
    private String jsonOutputDirectory;

    public FileController(XmlToJsonService xmlToJsonService) {
        this.xmlToJsonService = xmlToJsonService;
    }

    @GetMapping("/")
    public String uploadPage() {
        return "upload";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
        if (file.isEmpty()) {
            model.addAttribute("error", "Please upload an XML file.");
            return "upload";
        }

        try {
            String jsonFilePath = xmlToJsonService.convertAndSave(file, jsonOutputDirectory);

            model.addAttribute("message", "File converted successfully");
            model.addAttribute("jsonPath", jsonFilePath);
            
        } catch (IOException e) {
            model.addAttribute("error", "Error processing = " + e.getMessage());
        }

        return "upload";
    }
}
