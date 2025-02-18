package com.application.controller;

import com.application.dto.QueryPaginationResult;
import com.application.model.image.elasticesearch.Photo;
import com.application.service.PhotoService;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/images")
public class PhotoController {

    private static final String UPLOAD_DIR = "uploaded_images/";
    private static final int MAX_WIDTH = 1920; // desired width
    private static final int MAX_HEIGHT = 600; // desired height
    private final PhotoService photoService;

    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchPhoto(@RequestParam(name = "q") String query, @RequestParam(name = "skip", defaultValue = "0") Integer skip, @RequestParam(name = "limit", defaultValue = "30") Integer limit) {
        try {

            // * prepare data
            QueryPaginationResult<List<Photo>> queryPaginationResult = new QueryPaginationResult();

            if (skip == null) {
                skip = 0;
            }
            if (limit == null || limit > 30) limit = 30;


            List<Photo> photos = photoService.searchByText(query, skip, limit);
            return ResponseEntity.ok(new QueryPaginationResult<List<Photo>>(photos.size(), limit, skip, photos));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error fetching photos");
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        // Check if file is empty
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No file uploaded.");
        }

        // Ensure the upload directory exists
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        // Generate a safe file name
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        Path targetLocation = Paths.get(UPLOAD_DIR + originalFileName);

        try {
            // Resize the image before saving it
            Thumbnails.of(file.getInputStream())
                    .size(MAX_WIDTH, MAX_HEIGHT)  // Set the max width and height
                    .outputFormat("jpg")         // Optional: output format (e.g., JPG)
                    .toFile(targetLocation.toFile()); // Save the resized image to the file

            return ResponseEntity.status(HttpStatus.OK).body("File uploaded and resized successfully: " + originalFileName);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file: " + e.getMessage());
        }
    }

}
