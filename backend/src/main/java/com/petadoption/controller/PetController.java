// //1
// package com.petadoption.controller;

// import com.petadoption.entity.Pet;
// import com.petadoption.repository.PetRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;

// @RestController
// @RequestMapping("/pets") // ✅ This handles /pets
// @CrossOrigin(origins = "http://localhost:5173")
// public class PetController {

//     @Autowired
//     private PetRepository petRepository;

//     // Get all available pets (PUBLIC - no auth needed)
//     @GetMapping
//     public ResponseEntity<List<Pet>> getAllPets() {
//         List<Pet> pets = petRepository.findByStatus("available");
//         return ResponseEntity.ok(pets);
//     }

//     // Get pet by ID
//     @GetMapping("/{id}")
//     public ResponseEntity<Pet> getPetById(@PathVariable Long id) {
//         Pet pet = petRepository.findById(id)
//                 .orElseThrow(() -> new RuntimeException("Pet not found"));
//         return ResponseEntity.ok(pet);
//     }
// }

package com.petadoption.controller;

import com.petadoption.entity.Pet;
import com.petadoption.repository.PetRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/pets")
@CrossOrigin(origins = "http://localhost:5173")
public class PetController {

    private static final String IMAGE_UPLOAD_DIR = "D:/Petpostimages/";

    @Autowired
    private PetRepository petRepository;

    // =========================================
    // Get all available pets
    // =========================================
    @GetMapping
    public ResponseEntity<List<Pet>> getAllPets() {
        List<Pet> pets = petRepository.findByStatus("available");
        return ResponseEntity.ok(pets);
    }

    // =========================================
    // Get pet by ID
    // =========================================
    @GetMapping("/{id}")
    public ResponseEntity<Pet> getPetById(@PathVariable Long id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pet not found with ID: " + id));
        return ResponseEntity.ok(pet);
    }

    // =========================================
    // Serve pet image by filename
    // =========================================
    @GetMapping("/images/{fileName:.+}")
    public ResponseEntity<Resource> getPetImage(@PathVariable String fileName) {
        try {
            Path imagePath = Paths.get(IMAGE_UPLOAD_DIR).resolve(fileName).normalize();
            Resource resource = new UrlResource(imagePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            // Detect MIME type (jpg/png/etc.)
            String contentType = Files.probeContentType(imagePath);
            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
