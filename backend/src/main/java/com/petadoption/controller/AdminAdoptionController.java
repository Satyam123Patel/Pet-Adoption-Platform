// //1.1
// package com.petadoption.controller;

// import com.petadoption.entity.AdoptionRequest;
// import com.petadoption.repository.AdoptionRequestRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.jdbc.core.JdbcTemplate;
// import org.springframework.transaction.annotation.Transactional;
// import org.springframework.web.bind.annotation.*;

// import java.time.LocalDateTime;
// import java.util.*;

// @RestController
// @RequestMapping("/api/admin/adoptions")
// @CrossOrigin(origins = "http://localhost:5173")
// public class AdminAdoptionController {

//     @Autowired
//     private AdoptionRequestRepository adoptionRequestRepository;

//     @Autowired
//     private JdbcTemplate jdbcTemplate;

//     @GetMapping("/pending")
//     public ResponseEntity<?> getPendingRequests() {
//         try {
//             String sql = """
//                     SELECT
//                         ar.id AS request_id,
//                         ar.user_id,
//                         ar.pet_id,
//                         ar.living_situation,
//                         ar.previous_experience,
//                         ar.family_composition,
//                         ar.status,
//                         ar.created_at,

//                         u.name AS user_name,
//                         u.email AS user_email,

//                         p.name AS pet_name,
//                         p.breed AS pet_breed,
//                         p.age AS pet_age,
//                         p.category AS pet_category,
//                         p.image_url AS pet_image

//                     FROM adoption_requests ar
//                     INNER JOIN users u ON ar.user_id = u.id
//                     INNER JOIN pet p ON ar.pet_id = p.id
//                     WHERE ar.status = 'PENDING'
//                     ORDER BY ar.created_at DESC
//                     """;

//             List<Map<String, Object>> requests = jdbcTemplate.queryForList(sql);
//             System.out.println("✅ Found " + requests.size() + " pending requests");

//             return ResponseEntity.ok(requests);

//         } catch (Exception e) {
//             System.err.println("❌ Error: " + e.getMessage());
//             return ResponseEntity.badRequest()
//                     .body(Map.of("error", e.getMessage()));
//         }
//     }

//     @PutMapping("/{id}/approve")
//     @Transactional
//     public ResponseEntity<?> approveRequest(@PathVariable Long id) {
//         try {
//             AdoptionRequest request = adoptionRequestRepository.findById(id)
//                     .orElseThrow(() -> new RuntimeException("Request not found"));

//             request.setStatus(AdoptionRequest.Status.APPROVED);
//             request.setUpdatedAt(LocalDateTime.now());
//             adoptionRequestRepository.save(request);

//             String sql = "UPDATE pet SET status = 'adopted' WHERE id = ?";
//             jdbcTemplate.update(sql, request.getPetId());

//             System.out.println("✅ Request approved");

//             return ResponseEntity.ok(Map.of("message", "Approved successfully"));

//         } catch (Exception e) {
//             return ResponseEntity.badRequest()
//                     .body(Map.of("error", e.getMessage()));
//         }
//     }

//     @PutMapping("/{id}/reject")
//     @Transactional
//     public ResponseEntity<?> rejectRequest(@PathVariable Long id) {
//         try {
//             AdoptionRequest request = adoptionRequestRepository.findById(id)
//                     .orElseThrow(() -> new RuntimeException("Request not found"));

//             request.setStatus(AdoptionRequest.Status.REJECTED);
//             request.setUpdatedAt(LocalDateTime.now());
//             adoptionRequestRepository.save(request);

//             String sql = "UPDATE pet SET status = 'available' WHERE id = ?";
//             jdbcTemplate.update(sql, request.getPetId());

//             System.out.println("❌ Request rejected");

//             return ResponseEntity.ok(Map.of("message", "Rejected"));

//         } catch (Exception e) {
//             return ResponseEntity.badRequest()
//                     .body(Map.of("error", e.getMessage()));
//         }
//     }
// }

package com.petadoption.controller;

import com.petadoption.entity.AdoptionRequest;
import com.petadoption.entity.User;
import com.petadoption.entity.Pet;
import com.petadoption.repository.AdoptionRequestRepository;
import com.petadoption.repository.UserRepository;
import com.petadoption.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/admin/adoptions")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminAdoptionController {

    @Autowired
    private AdoptionRequestRepository adoptionRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JavaMailSender mailSender; // ✅ Email sender

    @GetMapping("/pending")
    public ResponseEntity<?> getPendingRequests() {
        try {
            String sql = """
                    SELECT
                        ar.id AS request_id,
                        ar.user_id,
                        ar.pet_id,
                        ar.living_situation,
                        ar.previous_experience,
                        ar.family_composition,
                        ar.status,
                        ar.created_at,

                        u.name AS user_name,
                        u.email AS user_email,

                        p.name AS pet_name,
                        p.breed AS pet_breed,
                        p.age AS pet_age,
                        p.category AS pet_category,
                        p.image_url AS pet_image

                    FROM adoption_requests ar
                    INNER JOIN users u ON ar.user_id = u.id
                    INNER JOIN pet p ON ar.pet_id = p.id
                    WHERE ar.status = 'PENDING'
                    ORDER BY ar.created_at DESC
                    """;

            List<Map<String, Object>> requests = jdbcTemplate.queryForList(sql);
            System.out.println("✅ Found " + requests.size() + " pending requests");

            return ResponseEntity.ok(requests);

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/approve")
    @Transactional
    public ResponseEntity<?> approveRequest(@PathVariable Long id) {
        try {
            AdoptionRequest request = adoptionRequestRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Request not found"));

            // Get user and pet details for email
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Pet pet = petRepository.findById(request.getPetId())
                    .orElseThrow(() -> new RuntimeException("Pet not found"));

            // Update status
            request.setStatus(AdoptionRequest.Status.APPROVED);
            request.setUpdatedAt(LocalDateTime.now());
            adoptionRequestRepository.save(request);

            String sql = "UPDATE pet SET status = 'adopted' WHERE id = ?";
            jdbcTemplate.update(sql, request.getPetId());

            // ✅ Send email to user
            sendAdoptionApprovalEmail(user, pet);

            System.out.println("✅ Request approved and email sent");

            return ResponseEntity.ok(Map.of("message", "Approved successfully. Email sent to user."));

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/reject")
    @Transactional
    public ResponseEntity<?> rejectRequest(@PathVariable Long id) {
        try {
            AdoptionRequest request = adoptionRequestRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Request not found"));

            request.setStatus(AdoptionRequest.Status.REJECTED);
            request.setUpdatedAt(LocalDateTime.now());
            adoptionRequestRepository.save(request);

            String sql = "UPDATE pet SET status = 'available' WHERE id = ?";
            jdbcTemplate.update(sql, request.getPetId());

            System.out.println("❌ Request rejected");

            return ResponseEntity.ok(Map.of("message", "Rejected"));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ Email notification for adoption approval
    private void sendAdoptionApprovalEmail(User user, Pet pet) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom("phalphaleutkarsh@gmail.com");
            message.setTo(user.getEmail());
            message.setSubject("🎉 Your Pet Adoption Request Approved!");

            String emailBody = String.format("""
                    Dear %s,

                    Congratulations! Your adoption request for %s has been APPROVED! 🎉

                    Pet Details:
                    - Name: %s
                    - Breed: %s
                    - Age: %d years
                    - Category: %s

                    Next Steps:
                    1. Visit our center to complete the adoption paperwork
                    2. Bring a valid ID and proof of address
                    3. Pay the adoption fee (if applicable)
                    4. Take your new family member home!

                    Contact us at: phalphaleutkarsh@gmail.com

                    Thank you for choosing to adopt!

                    Best regards,
                    Pet Adoption Team
                    """,
                    user.getName(),
                    pet.getName(),
                    pet.getName(),
                    pet.getBreed(),
                    pet.getAge(),
                    pet.getCategory());

            message.setText(emailBody);
            mailSender.send(message);

            System.out.println("📧 Adoption approval email sent to: " + user.getEmail());

        } catch (Exception e) {
            System.err.println("❌ Failed to send email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
