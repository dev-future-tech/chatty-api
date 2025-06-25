package com.example.restservice.images;

import com.example.restservice.bookings.Destination;
import com.example.restservice.bookings.DestinationRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/images")
public class ImageController {

    private final String IMAGE_DIRECTORY = "/Users/anthonyikeda/work/git/chatty-api/src/main/resources/images";

    DestinationRepository destinationRepository;

    public ImageController(DestinationRepository destinationRepository) {
        this.destinationRepository = destinationRepository;
    }

    @GetMapping("/destination/{destinationId}")
    public ResponseEntity<Resource> getDestinationImageById(@PathVariable Long destinationId) {
        Destination dest = this.destinationRepository.findByDestinationId(destinationId);
        String imageName = dest.getImageUrl();

        try {
            Resource resource = loadImage(imageName);

            if (resource.exists() && resource.isReadable()) {
                String contentType = determineContentType(imageName);
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{imageName:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) {
        try {
            Resource resource = loadImage(imageName);

            if (resource.exists() && resource.isReadable()) {
                String contentType = determineContentType(imageName);
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private Resource loadImage(String imageName) throws Exception {
        Path imagePath = Paths.get(IMAGE_DIRECTORY).resolve(imageName).normalize();
        return new UrlResource(imagePath.toUri());
    }

    private String determineContentType(String fileName) {
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (fileName.endsWith(".png")) {
            return "image/png";
        } else if (fileName.endsWith(".gif")) {
            return "image/gif";
        }
        // Add more image types as needed
        return MediaType.APPLICATION_OCTET_STREAM_VALUE; // Default to binary stream
    }
}
