// //1
// package com.petadoption.controller;

// import com.petadoption.entity.PendingPets;
// import com.petadoption.repository.PendingPetRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.jdbc.core.JdbcTemplate;
// import org.springframework.transaction.annotation.Transactional;
// import org.springframework.web.bind.annotation.*;

// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.nio.file.Paths;
// import java.nio.file.StandardCopyOption;
// import java.util.*;

// @RestController
// @RequestMapping("/api/admin/pets")
// @CrossOrigin(origins = "http://localhost:5173")
// public class AdminPanelController {

//         @Autowired
//         private PendingPetRepository pendingPetRepository;

//         @Autowired
//         private JdbcTemplate jdbcTemplate;

//         private static final String IMAGE_DIR = "D:\\Petpostimages\\";

//         @GetMapping("/pending")
//         public ResponseEntity<?> getPendingPets() {
//                 try {
//                         List<PendingPets> pendingPets = pendingPetRepository.findByStatus("pending");
//                         System.out.println("📋 Found " + pendingPets.size() + " pending posted pets");
//                         return ResponseEntity.ok(pendingPets);
//                 } catch (Exception e) {
//                         System.err.println("❌ Error fetching pending pets: " + e.getMessage());
//                         return ResponseEntity.badRequest()
//                                         .body(Map.of("error", "Failed to fetch pending pets: " + e.getMessage()));
//                 }
//         }

//         @GetMapping("/approved")
//         public ResponseEntity<?> getApprovedPets() {
//                 try {
//                         String sql = "SELECT * FROM pet WHERE status = 'available' ORDER BY id DESC";
//                         List<Map<String, Object>> pets = jdbcTemplate.queryForList(sql);
//                         System.out.println("📋 Found " + pets.size() + " available pets for adoption");
//                         return ResponseEntity.ok(pets);
//                 } catch (Exception e) {
//                         System.err.println("❌ Error: " + e.getMessage());
//                         return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
//                 }
//         }

//         @GetMapping("/adopted")
//         public ResponseEntity<?> getAdoptedPets() {
//                 try {
//                         String sql = "SELECT * FROM pet WHERE status = 'adopted' ORDER BY id DESC";
//                         List<Map<String, Object>> pets = jdbcTemplate.queryForList(sql);
//                         System.out.println("📋 Found " + pets.size() + " adopted pets");
//                         return ResponseEntity.ok(pets);
//                 } catch (Exception e) {
//                         return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
//                 }
//         }

//         @PostMapping("/approve/{id}")
//         @Transactional
//         public ResponseEntity<?> approvePet(@PathVariable Long id) {
//                 try {
//                         System.out.println("✅ Approving posted pet ID: " + id);

//                         Optional<PendingPets> pendingOpt = pendingPetRepository.findById(id);
//                         if (!pendingOpt.isPresent()) {
//                                 return ResponseEntity.badRequest().body(Map.of("error", "Pet not found"));
//                         }

//                         PendingPets pendingPet = pendingOpt.get();

//                         String insertSql = """
//                                         INSERT INTO pet (name, category, breed, age, gender, status, image_url)
//                                         VALUES (?, ?, ?, ?, ?, 'available', ?)
//                                         """;

//                         String gender = "U";
//                         if (pendingPet.getGender() != null && pendingPet.getGender().length() > 0) {
//                                 String g = pendingPet.getGender().substring(0, 1).toUpperCase();
//                                 if (g.equals("M") || g.equals("F")) {
//                                         gender = g;
//                                 }
//                         }

//                         jdbcTemplate.update(insertSql,
//                                         pendingPet.getName(),
//                                         pendingPet.getCategory(),
//                                         pendingPet.getBreed(),
//                                         pendingPet.getAge(),
//                                         gender,
//                                         pendingPet.getImagePath());

//                         System.out.println("✅ Pet added to adoption list");

//                         pendingPet.setStatus("approved");
//                         pendingPetRepository.save(pendingPet);

//                         return ResponseEntity.ok(Map.of("message", "Pet approved successfully"));

//                 } catch (Exception e) {
//                         System.err.println("❌ Error approving pet: " + e.getMessage());
//                         e.printStackTrace();
//                         return ResponseEntity.badRequest()
//                                         .body(Map.of("error", "Failed to approve pet: " + e.getMessage()));
//                 }
//         }

//         @PostMapping("/reject/{id}")
//         public ResponseEntity<?> rejectPet(@PathVariable Long id) {
//                 try {
//                         Optional<PendingPets> pendingOpt = pendingPetRepository.findById(id);
//                         if (!pendingOpt.isPresent()) {
//                                 return ResponseEntity.badRequest().body(Map.of("error", "Pet not found"));
//                         }

//                         PendingPets pendingPet = pendingOpt.get();
//                         pendingPet.setStatus("rejected");
//                         pendingPetRepository.save(pendingPet);

//                         System.out.println("❌ Pet rejected: ID " + id);
//                         return ResponseEntity.ok(Map.of("message", "Pet rejected successfully"));

//                 } catch (Exception e) {
//                         return ResponseEntity.badRequest()
//                                         .body(Map.of("error", "Failed to reject pet: " + e.getMessage()));
//                 }
//         }

//         @DeleteMapping("/{id}")
//         public ResponseEntity<?> deletePet(@PathVariable Long id) {
//                 try {
//                         String sql = "DELETE FROM pet WHERE id = ?";
//                         jdbcTemplate.update(sql, id);
//                         System.out.println("🗑️ Deleted pet ID: " + id);
//                         return ResponseEntity.ok(Map.of("message", "Pet deleted successfully"));
//                 } catch (Exception e) {
//                         return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
//                 }
//         }
// }

/*
 * package com.yourpackage.controller;
 * 
 * import java.io.IOException;
 * import java.nio.file.Files;
 * import java.nio.file.Path;
 * import java.nio.file.Paths;
 * import java.nio.file.StandardCopyOption;
 * 
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.beans.factory.annotation.Value;
 * import org.springframework.jdbc.core.JdbcTemplate;
 * import org.springframework.web.bind.annotation.CrossOrigin;
 * import org.springframework.web.bind.annotation.RequestMapping;
 * import org.springframework.web.bind.annotation.RestController;
 * 
 * import com.yourpackage.repository.PendingPetRepository;
 * 
 * @RestController
 * 
 * @RequestMapping("/api/admin/pets")
 * 
 * @CrossOrigin(origins = "http://localhost:5173")
 * public class AdminPanelController {
 * 
 * @Autowired
 * private PendingPetRepository pendingPetRepository;
 * 
 * @Autowired
 * private JdbcTemplate jdbcTemplate;
 * 
 * // ✅ Read from application.properties
 * 
 * @Value("${file.upload-dir}")
 * private String IMAGE_DIR;
 * 
 * // Example utility method (if you are saving files)
 * protected String saveImage(org.springframework.web.multipart.MultipartFile
 * file) throws IOException {
 * if (file == null || file.isEmpty()) {
 * return null;
 * }
 * 
 * Path uploadPath = Paths.get(IMAGE_DIR);
 * if (!Files.exists(uploadPath)) {
 * Files.createDirectories(uploadPath);
 * }
 * 
 * String filename = System.currentTimeMillis() + "_" +
 * file.getOriginalFilename();
 * Path filePath = uploadPath.resolve(filename);
 * 
 * Files.copy(file.getInputStream(), filePath,
 * StandardCopyOption.REPLACE_EXISTING);
 * 
 * return filename; // store ONLY filename in DB
 * }
 * }
 * 
 */

//1.1
package com.petadoption.controller;

import com.petadoption.entity.PendingPets;
import com.petadoption.repository.PendingPetRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/pets")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminPanelController {

        @Autowired
        private PendingPetRepository pendingPetRepository;

        @Autowired
        private JdbcTemplate jdbcTemplate;

        // =========================================
        // Get all pending pets
        // =========================================
        @GetMapping("/pending")
        public ResponseEntity<?> getPendingPets() {
                try {
                        List<PendingPets> pendingPets = pendingPetRepository.findByStatus("pending");
                        return ResponseEntity.ok(pendingPets);
                } catch (Exception e) {
                        return ResponseEntity.badRequest()
                                        .body(Map.of("error", "Failed to fetch pending pets: " + e.getMessage()));
                }
        }

        // =========================================
        // Get approved (available) pets
        // =========================================
        @GetMapping("/approved")
        public ResponseEntity<?> getApprovedPets() {
                try {
                        String sql = "SELECT * FROM pet WHERE status = 'available' ORDER BY id DESC";
                        return ResponseEntity.ok(jdbcTemplate.queryForList(sql));
                } catch (Exception e) {
                        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
                }
        }

        // =========================================
        // Get adopted pets
        // =========================================
        @GetMapping("/adopted")
        public ResponseEntity<?> getAdoptedPets() {
                try {
                        String sql = "SELECT * FROM pet WHERE status = 'adopted' ORDER BY id DESC";
                        return ResponseEntity.ok(jdbcTemplate.queryForList(sql));
                } catch (Exception e) {
                        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
                }
        }

        // =========================================
        // Approve pending pet
        // =========================================
        @PostMapping("/approve/{id}")
        @Transactional
        public ResponseEntity<?> approvePet(@PathVariable Long id) {
                try {
                        PendingPets pendingPet = pendingPetRepository.findById(id)
                                        .orElseThrow(() -> new RuntimeException("Pet not found"));

                        // Normalize gender
                        String gender = "U";
                        if (pendingPet.getGender() != null && !pendingPet.getGender().isBlank()) {
                                String g = pendingPet.getGender().substring(0, 1).toUpperCase();
                                if (g.equals("M") || g.equals("F")) {
                                        gender = g;
                                }
                        }

                        // ✅ IMPORTANT: image_url = ONLY filename
                        String insertSql = """
                                        INSERT INTO pet (name, category, breed, age, gender, status, image_url)
                                        VALUES (?, ?, ?, ?, ?, 'available', ?)
                                        """;

                        jdbcTemplate.update(
                                        insertSql,
                                        pendingPet.getName(),
                                        pendingPet.getCategory(),
                                        pendingPet.getBreed(),
                                        pendingPet.getAge(),
                                        gender,
                                        pendingPet.getImagePath() // ✅ filename only
                        );

                        // Update pending pet status
                        pendingPet.setStatus("approved");
                        pendingPetRepository.save(pendingPet);

                        return ResponseEntity.ok(Map.of("message", "Pet approved successfully"));

                } catch (Exception e) {
                        return ResponseEntity.badRequest()
                                        .body(Map.of("error", "Failed to approve pet: " + e.getMessage()));
                }
        }

        // =========================================
        // Reject pending pet
        // =========================================
        @PostMapping("/reject/{id}")
        public ResponseEntity<?> rejectPet(@PathVariable Long id) {
                try {
                        PendingPets pendingPet = pendingPetRepository.findById(id)
                                        .orElseThrow(() -> new RuntimeException("Pet not found"));

                        pendingPet.setStatus("rejected");
                        pendingPetRepository.save(pendingPet);

                        return ResponseEntity.ok(Map.of("message", "Pet rejected successfully"));

                } catch (Exception e) {
                        return ResponseEntity.badRequest()
                                        .body(Map.of("error", "Failed to reject pet: " + e.getMessage()));
                }
        }

        // =========================================
        // Delete approved pet
        // =========================================
        @DeleteMapping("/{id}")
        public ResponseEntity<?> deletePet(@PathVariable Long id) {
                try {
                        jdbcTemplate.update("DELETE FROM pet WHERE id = ?", id);
                        return ResponseEntity.ok(Map.of("message", "Pet deleted successfully"));
                } catch (Exception e) {
                        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
                }
        }
}
