package com.digiquad.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class XmlToJsonService 
{
    private final XmlMapper xmlMapper;
    private final ObjectMapper objMapper;

    public XmlToJsonService() 
    {
        this.xmlMapper = new XmlMapper();
        this.objMapper = new ObjectMapper();
    }

    public String convertAndSave(MultipartFile file, String outputDirectory) throws IOException 
    {
        if (file.getOriginalFilename().endsWith(".xml")) 
        {
        	 Path outputDir = Paths.get(outputDirectory);
             if (!Files.exists(outputDir)) 
             {
                 Files.createDirectories(outputDir);
             }

             JsonNode tree = xmlMapper.readTree(file.getInputStream());
             
             String jsonString = objMapper.writerWithDefaultPrettyPrinter().writeValueAsString(tree);
            
             // Define JSON File Path
             String jsonFileName = file.getOriginalFilename().replace(".xml", ".json");
             Path jsonFilePath = outputDir.resolve(jsonFileName);
             
             objMapper.writeValue(jsonFilePath.toFile(), tree);

             return jsonFilePath.toAbsolutePath().toString();
		}
        else
        {
        	throw new IOException("Invalid file format. Please upload a valid XML format.");
        }
    }
}
