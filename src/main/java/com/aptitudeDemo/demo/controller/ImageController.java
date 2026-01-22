package com.aptitudeDemo.demo.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aptitudeDemo.demo.model.student.Image;
import com.aptitudeDemo.demo.service.ImageS3Service;


@RestController
@RequestMapping("/image")
public class ImageController {

    @Autowired
    private ImageS3Service imageS3Service;

    @GetMapping("/{studentFormId}")
    public ResponseEntity<?> getImageByStudentFormId(
            @PathVariable String studentFormId
    ) {
        Image image = imageS3Service.getImageByStudentFormId(studentFormId);

        if (image == null) {
            return ResponseEntity
                    .status(404)
                    .body("Image not found for studentFormId: " + studentFormId);
        }

        return ResponseEntity.ok(image);
    }

    
    @PostMapping("/upload/{email}")
    public ResponseEntity<?> uploadImage(
            @PathVariable String email,
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        imageS3Service.uploadAndSaveImageByEmail(file, email);

        return ResponseEntity.ok("Image uploaded successfully");
    }
    
    @GetMapping("/all")
    public ResponseEntity<?> getAllImages() {
        List<Image> images = imageS3Service.getAllImages();
        return ResponseEntity.ok(images);
    }

}