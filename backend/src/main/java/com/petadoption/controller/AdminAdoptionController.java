//1
// package com.petadoption.controller;

// import com.petadoption.entity.AdoptionRequest;
// import com.petadoption.entity.Pet;
// import com.petadoption.repository.AdoptionRequestRepository;
// import com.petadoption.repository.PetRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.transaction.annotation.Transactional;
// import org.springframework.web.bind.annotation.*;

// import java.time.LocalDateTime;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;

// @RestController
// @RequestMapping("/api/admin/adoptions")
// @CrossOrigin(origins = "http://localhost:5173")
// public class AdminAdoptionController {

//     @Autowired
//     private AdoptionRequestRepository adoptionRequestRepository;

//     @Autowired
//     private PetRepository petRepository;

//     // Get all pending adoption requests
//     @GetMapping("/pending")
//     public ResponseEntity<List<AdoptionRequest>> getPendingRequests() {
//         List<AdoptionRequest> requests = adoptionRequestRepository.findByStatus("PENDING");
//         return ResponseEntity.ok(requests);
//     }

//     // Get all approved adoption requests
//     @GetMapping("/approved")
//     public ResponseEntity<List<AdoptionRequest>> getApprovedRequests() {
//         List<AdoptionRequest> requests = adoptionRequestRepository.findByStatus("APPROVED");
//         return ResponseEntity.ok(requests);
//     }

//     // Get all adoption requests
//     @GetMapping("/all")
//     public ResponseEntity<List<AdoptionRequest>> getAllRequests() {
//         List<AdoptionRequest> requests = adoptionRequestRepository.findAllByOrderByCreatedAtDesc();
//         return ResponseEntity.ok(requests);
//     }

//     // Approve adoption request
//     @PutMapping("/{id}/approve")
//     @Transactional
//     public ResponseEntity<?> approveRequest(@PathVariable Long id) {
//         try {
//             AdoptionRequest request = adoptionRequestRepository.findById(id)
//                     .orElseThrow(() -> new RuntimeException("Request not found"));

//             // Update request status
//             request.setStatus("APPROVED");
//             request.setUpdatedAt(LocalDateTime.now());
//             adoptionRequestRepository.save(request);

//             // Update pet status to adopted
//             Pet pet = petRepository.findById(request.getPetId())
//                     .orElseThrow(() -> new RuntimeException("Pet not found"));
//             pet.setStatus("adopted");
//             petRepository.save(pet);

//             Map<String, String> response = new HashMap<>();
//             response.put("message", "Adoption request approved successfully");
//             return ResponseEntity.ok(response);
//         } catch (Exception e) {
//             return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
//         }
//     }

//     // Reject adoption request
//     @PutMapping("/{id}/reject")
//     @Transactional
//     public ResponseEntity<?> rejectRequest(@PathVariable Long id) {
//         try {
//             AdoptionRequest request = adoptionRequestRepository.findById(id)
//                     .orElseThrow(() -> new RuntimeException("Request not found"));

//             // DELETE the request instead of just marking as rejected
//             adoptionRequestRepository.delete(request);

//             // Update pet status back to available
//             Pet pet = petRepository.findById(request.getPetId())
//                     .orElseThrow(() -> new RuntimeException("Pet not found"));
//             pet.setStatus("available");
//             petRepository.save(pet);

//             Map<String, String> response = new HashMap<>();
//             response.put("message", "Adoption request rejected and deleted");
//             return ResponseEntity.ok(response);
//         } catch (Exception e) {
//             return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
//         }
//     }

//     // Delete adoption request
//     @DeleteMapping("/{id}")
//     public ResponseEntity<?> deleteRequest(@PathVariable Long id) {
//         try {
//             adoptionRequestRepository.deleteById(id);
//             return ResponseEntity.ok(Map.of("message", "Request deleted successfully"));
//         } catch (Exception e) {
//             return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
//         }
//     }
// }

//1.1
package com.petadoption.controller;

import com.petadoption.entity.AdoptionRequest;
import com.petadoption.repository.AdoptionRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
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
    private JdbcTemplate jdbcTemplate;

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

            request.setStatus(AdoptionRequest.Status.APPROVED);
            request.setUpdatedAt(LocalDateTime.now());
            adoptionRequestRepository.save(request);

            String sql = "UPDATE pet SET status = 'adopted' WHERE id = ?";
            jdbcTemplate.update(sql, request.getPetId());

            System.out.println("✅ Request approved");

            return ResponseEntity.ok(Map.of("message", "Approved successfully"));

        } catch (Exception e) {
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
}