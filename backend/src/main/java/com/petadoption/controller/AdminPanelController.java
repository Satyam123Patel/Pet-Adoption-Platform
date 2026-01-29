// //1.1
// package com.petadoption.controller;

// import com.petadoption.entity.PendingPets;
// import com.petadoption.repository.PendingPetRepository;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.jdbc.core.JdbcTemplate;
// import org.springframework.transaction.annotation.Transactional;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;
// import java.util.Map;

// @RestController
// @RequestMapping("/api/admin/pets")
// @CrossOrigin(origins = "http://localhost:5173")
// public class AdminPanelController {

//         @Autowired
//         private PendingPetRepository pendingPetRepository;

//         @Autowired
//         private JdbcTemplate jdbcTemplate;

//         // =========================================
//         // Get all pending pets
//         // =========================================
//         @GetMapping("/pending")
//         public ResponseEntity<?> getPendingPets() {
//                 try {
//                         List<PendingPets> pendingPets = pendingPetRepository.findByStatus("pending");
//                         return ResponseEntity.ok(pendingPets);
//                 } catch (Exception e) {
//                         return ResponseEntity.badRequest()
//                                         .body(Map.of("error", "Failed to fetch pending pets: " + e.getMessage()));
//                 }
//         }

//         // =========================================
//         // Get approved (available) pets
//         // =========================================
//         @GetMapping("/approved")
//         public ResponseEntity<?> getApprovedPets() {
//                 try {
//                         String sql = "SELECT * FROM pet WHERE status = 'available' ORDER BY id DESC";
//                         return ResponseEntity.ok(jdbcTemplate.queryForList(sql));
//                 } catch (Exception e) {
//                         return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
//                 }
//         }

//         // =========================================
//         // Get adopted pets
//         // =========================================
//         @GetMapping("/adopted")
//         public ResponseEntity<?> getAdoptedPets() {
//                 try {
//                         String sql = "SELECT * FROM pet WHERE status = 'adopted' ORDER BY id DESC";
//                         return ResponseEntity.ok(jdbcTemplate.queryForList(sql));
//                 } catch (Exception e) {
//                         return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
//                 }
//         }

//         // =========================================
//         // Approve pending pet
//         // =========================================
//         @PostMapping("/approve/{id}")
//         @Transactional
//         public ResponseEntity<?> approvePet(@PathVariable Long id) {
//                 try {
//                         PendingPets pendingPet = pendingPetRepository.findById(id)
//                                         .orElseThrow(() -> new RuntimeException("Pet not found"));

//                         // Normalize gender
//                         String gender = "U";
//                         if (pendingPet.getGender() != null && !pendingPet.getGender().isBlank()) {
//                                 String g = pendingPet.getGender().substring(0, 1).toUpperCase();
//                                 if (g.equals("M") || g.equals("F")) {
//                                         gender = g;
//                                 }
//                         }

//                         // ✅ IMPORTANT: image_url = ONLY filename
//                         String insertSql = """
//                                         INSERT INTO pet (name, category, breed, age, gender, status, image_url)
//                                         VALUES (?, ?, ?, ?, ?, 'available', ?)
//                                         """;

//                         jdbcTemplate.update(
//                                         insertSql,
//                                         pendingPet.getName(),
//                                         pendingPet.getCategory(),
//                                         pendingPet.getBreed(),
//                                         pendingPet.getAge(),
//                                         gender,
//                                         pendingPet.getImagePath() // ✅ filename only
//                         );

//                         // Update pending pet status
//                         pendingPet.setStatus("approved");
//                         pendingPetRepository.save(pendingPet);

//                         return ResponseEntity.ok(Map.of("message", "Pet approved successfully"));

//                 } catch (Exception e) {
//                         return ResponseEntity.badRequest()
//                                         .body(Map.of("error", "Failed to approve pet: " + e.getMessage()));
//                 }
//         }

//         // =========================================
//         // Reject pending pet
//         // =========================================
//         @PostMapping("/reject/{id}")
//         public ResponseEntity<?> rejectPet(@PathVariable Long id) {
//                 try {
//                         PendingPets pendingPet = pendingPetRepository.findById(id)
//                                         .orElseThrow(() -> new RuntimeException("Pet not found"));

//                         pendingPet.setStatus("rejected");
//                         pendingPetRepository.save(pendingPet);

//                         return ResponseEntity.ok(Map.of("message", "Pet rejected successfully"));

//                 } catch (Exception e) {
//                         return ResponseEntity.badRequest()
//                                         .body(Map.of("error", "Failed to reject pet: " + e.getMessage()));
//                 }
//         }

//         // =========================================
//         // Delete approved pet
//         // =========================================
//         @DeleteMapping("/{id}")
//         public ResponseEntity<?> deletePet(@PathVariable Long id) {
//                 try {
//                         jdbcTemplate.update("DELETE FROM pet WHERE id = ?", id);
//                         return ResponseEntity.ok(Map.of("message", "Pet deleted successfully"));
//                 } catch (Exception e) {
//                         return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
//                 }
//         }
// }

package com.petadoption.controller;

import com.petadoption.entity.PendingPets;
import com.petadoption.entity.User;
import com.petadoption.repository.PendingPetRepository;
import com.petadoption.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
        private UserRepository userRepository;

        @Autowired
        private JdbcTemplate jdbcTemplate;

        @Autowired
        private JavaMailSender mailSender;

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

        @GetMapping("/approved")
        public ResponseEntity<?> getApprovedPets() {
                try {
                        String sql = "SELECT * FROM pet WHERE status = 'available' ORDER BY id DESC";
                        return ResponseEntity.ok(jdbcTemplate.queryForList(sql));
                } catch (Exception e) {
                        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
                }
        }

        @GetMapping("/adopted")
        public ResponseEntity<?> getAdoptedPets() {
                try {
                        String sql = "SELECT * FROM pet WHERE status = 'adopted' ORDER BY id DESC";
                        return ResponseEntity.ok(jdbcTemplate.queryForList(sql));
                } catch (Exception e) {
                        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
                }
        }

        @PostMapping("/approve/{id}")
        @Transactional
        public ResponseEntity<?> approvePet(@PathVariable Long id) {
                try {
                        PendingPets pendingPet = pendingPetRepository.findById(id)
                                        .orElseThrow(() -> new RuntimeException("Pet not found"));

                        // ✅ Find user by EMAIL
                        User user = userRepository.findByEmail(pendingPet.getEmail())
                                        .orElseThrow(() -> new RuntimeException(
                                                        "User not found with email: " + pendingPet.getEmail()));

                        String gender = "U";
                        if (pendingPet.getGender() != null && !pendingPet.getGender().isBlank()) {
                                String g = pendingPet.getGender().substring(0, 1).toUpperCase();
                                if (g.equals("M") || g.equals("F")) {
                                        gender = g;
                                }
                        }

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
                                        pendingPet.getImagePath());

                        pendingPet.setStatus("approved");
                        pendingPetRepository.save(pendingPet);

                        // ✅ Send email
                        sendPetDonationApprovalEmail(user, pendingPet);

                        System.out.println("✅ Pet approved and email sent to: " + user.getEmail());

                        return ResponseEntity.ok(Map.of("message", "Pet approved successfully. Email sent to donor."));

                } catch (Exception e) {
                        System.err.println("❌ Error: " + e.getMessage());
                        e.printStackTrace();
                        return ResponseEntity.badRequest()
                                        .body(Map.of("error", "Failed to approve pet: " + e.getMessage()));
                }
        }

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

        @DeleteMapping("/{id}")
        public ResponseEntity<?> deletePet(@PathVariable Long id) {
                try {
                        jdbcTemplate.update("DELETE FROM pet WHERE id = ?", id);
                        return ResponseEntity.ok(Map.of("message", "Pet deleted successfully"));
                } catch (Exception e) {
                        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
                }
        }

        // ✅ Email notification
        private void sendPetDonationApprovalEmail(User user, PendingPets pet) {
                try {
                        SimpleMailMessage message = new SimpleMailMessage();

                        message.setFrom("phalphaleutkarsh@gmail.com");
                        message.setTo(user.getEmail());
                        message.setSubject("🎉 Your Pet Donation Approved!");

                        String emailBody = String.format(
                                        """
                                                        Dear %s,

                                                        Great news! Your pet donation has been APPROVED! 🎉

                                                        Pet Details:
                                                        - Name: %s
                                                        - Breed: %s
                                                        - Age: %d years
                                                        - Category: %s

                                                        Your pet is now listed on our adoption platform and will be available for loving families to adopt.

                                                        Thank you for your contribution to helping pets find their forever homes!

                                                        You can view your pet on our website: http://localhost:5173/pets

                                                        If you have any questions, contact us at: phalphaleutkarsh@gmail.com

                                                        Best regards,
                                                        Pet Adoption Team
                                                        """,
                                        user.getName(),
                                        pet.getName(),
                                        pet.getBreed() != null ? pet.getBreed() : "Not specified",
                                        pet.getAge() != null ? pet.getAge() : 0,
                                        pet.getCategory());

                        message.setText(emailBody);
                        mailSender.send(message);

                        System.out.println("📧 Pet donation approval email sent to: " + user.getEmail());

                } catch (Exception e) {
                        System.err.println("❌ Failed to send email: " + e.getMessage());
                        e.printStackTrace();
                }
        }
}
