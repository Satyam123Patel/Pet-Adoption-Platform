//1
// package com.petadoption.entity;

// import jakarta.persistence.*;
// import java.time.LocalDateTime;

// @Entity
// @Table(name = "adoption_requests")
// public class AdoptionRequest {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     @Column(name = "pet_id", nullable = false)
//     private Long petId;

//     @Column(name = "pet_name")
//     private String petName;

//     @Column(name = "pet_breed")
//     private String petBreed;

//     @Column(name = "pet_age")
//     private Integer petAge;

//     @Column(name = "pet_category")
//     private String petCategory;

//     @Column(name = "pet_image")
//     private String petImage;

//     @Column(nullable = false)
//     private String email;

//     @Column(name = "phone_no")
//     private String phoneNo;

//     @Column(name = "living_situation")
//     private String livingSituation;

//     @Column(name = "previous_experience")
//     private String previousExperience;

//     @Column(name = "family_composition")
//     private String familyComposition;

//     @Column(nullable = false)
//     private String status = "PENDING";

//     @Column(name = "created_at")
//     private LocalDateTime createdAt = LocalDateTime.now();

//     @Column(name = "updated_at")
//     private LocalDateTime updatedAt;

//     // Getters and Setters (KEEP ALL YOUR EXISTING GETTERS/SETTERS)
//     public Long getId() {
//         return id;
//     }

//     public void setId(Long id) {
//         this.id = id;
//     }

//     public Long getPetId() {
//         return petId;
//     }

//     public void setPetId(Long petId) {
//         this.petId = petId;
//     }

//     public String getPetName() {
//         return petName;
//     }

//     public void setPetName(String petName) {
//         this.petName = petName;
//     }

//     public String getPetBreed() {
//         return petBreed;
//     }

//     public void setPetBreed(String petBreed) {
//         this.petBreed = petBreed;
//     }

//     public Integer getPetAge() {
//         return petAge;
//     }

//     public void setPetAge(Integer petAge) {
//         this.petAge = petAge;
//     }

//     public String getPetCategory() {
//         return petCategory;
//     }

//     public void setPetCategory(String petCategory) {
//         this.petCategory = petCategory;
//     }

//     public String getPetImage() {
//         return petImage;
//     }

//     public void setPetImage(String petImage) {
//         this.petImage = petImage;
//     }

//     public String getEmail() {
//         return email;
//     }

//     public void setEmail(String email) {
//         this.email = email;
//     }

//     public String getPhoneNo() {
//         return phoneNo;
//     }

//     public void setPhoneNo(String phoneNo) {
//         this.phoneNo = phoneNo;
//     }

//     public String getLivingSituation() {
//         return livingSituation;
//     }

//     public void setLivingSituation(String livingSituation) {
//         this.livingSituation = livingSituation;
//     }

//     public String getPreviousExperience() {
//         return previousExperience;
//     }

//     public void setPreviousExperience(String previousExperience) {
//         this.previousExperience = previousExperience;
//     }

//     public String getFamilyComposition() {
//         return familyComposition;
//     }

//     public void setFamilyComposition(String familyComposition) {
//         this.familyComposition = familyComposition;
//     }

//     public String getStatus() {
//         return status;
//     }

//     public void setStatus(String status) {
//         this.status = status;
//     }

//     public LocalDateTime getCreatedAt() {
//         return createdAt;
//     }

//     public void setCreatedAt(LocalDateTime createdAt) {
//         this.createdAt = createdAt;
//     }

//     public LocalDateTime getUpdatedAt() {
//         return updatedAt;
//     }

//     public void setUpdatedAt(LocalDateTime updatedAt) {
//         this.updatedAt = updatedAt;
//     }
// }

//1.1
// package com.petadoption.entity;

// import jakarta.persistence.*;
// import java.time.LocalDateTime;

// @Entity
// @Table(name = "adoption_requests")
// public class AdoptionRequest {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     // ✅ Foreign Key to users table
//     @Column(name = "user_id", nullable = false)
//     private Long userId;

//     // ✅ Foreign Key to pet table
//     @Column(name = "pet_id", nullable = false)
//     private Long petId;

//     @Column(name = "living_situation", columnDefinition = "TEXT")
//     private String livingSituation;

//     @Column(name = "previous_experience", columnDefinition = "TEXT")
//     private String previousExperience;

//     @Column(name = "family_composition", columnDefinition = "TEXT")
//     private String familyComposition;

//     @Column(nullable = false)
//     @Enumerated(EnumType.STRING)
//     private Status status = Status.PENDING;

//     @Column(name = "created_at", updatable = false)
//     private LocalDateTime createdAt;

//     @Column(name = "updated_at")
//     private LocalDateTime updatedAt;

//     public enum Status {
//         PENDING, APPROVED, REJECTED
//     }

//     @PrePersist
//     protected void onCreate() {
//         createdAt = LocalDateTime.now();
//         updatedAt = LocalDateTime.now();
//     }

//     @PreUpdate
//     protected void onUpdate() {
//         updatedAt = LocalDateTime.now();
//     }

//     // GETTERS AND SETTERS

//     public Long getId() {
//         return id;
//     }

//     public void setId(Long id) {
//         this.id = id;
//     }

//     public Long getUserId() {
//         return userId;
//     }

//     public void setUserId(Long userId) {
//         this.userId = userId;
//     }

//     public Long getPetId() {
//         return petId;
//     }

//     public void setPetId(Long petId) {
//         this.petId = petId;
//     }

//     public String getLivingSituation() {
//         return livingSituation;
//     }

//     public void setLivingSituation(String livingSituation) {
//         this.livingSituation = livingSituation;
//     }

//     public String getPreviousExperience() {
//         return previousExperience;
//     }

//     public void setPreviousExperience(String previousExperience) {
//         this.previousExperience = previousExperience;
//     }

//     public String getFamilyComposition() {
//         return familyComposition;
//     }

//     public void setFamilyComposition(String familyComposition) {
//         this.familyComposition = familyComposition;
//     }

//     public Status getStatus() {
//         return status;
//     }

//     public void setStatus(Status status) {
//         this.status = status;
//     }

//     public void setStatus(String status) {
//         this.status = Status.valueOf(status.toUpperCase());
//     }

//     public String getStatusString() {
//         return status.name();
//     }

//     public LocalDateTime getCreatedAt() {
//         return createdAt;
//     }

//     public void setCreatedAt(LocalDateTime createdAt) {
//         this.createdAt = createdAt;
//     }

//     public LocalDateTime getUpdatedAt() {
//         return updatedAt;
//     }

//     public void setUpdatedAt(LocalDateTime updatedAt) {
//         this.updatedAt = updatedAt;
//     }
// }

//1.2
// package com.petadoption.entity;

// import jakarta.persistence.*;
// import java.time.LocalDateTime;

// /**
//  * ✅ MATCHES YOUR MySQL DATABASE EXACTLY
//  * - Uses user_id and pet_id foreign keys
//  * - Status as ENUM('PENDING','APPROVED','REJECTED')
//  * - No email column
//  */
// @Entity
// @Table(name = "adoption_requests")
// public class AdoptionRequest {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     // ✅ Foreign Key to users table
//     @Column(name = "user_id", nullable = false)
//     private Long userId;

//     // ✅ Foreign Key to pet table
//     @Column(name = "pet_id", nullable = false)
//     private Long petId;

//     // ✅ Application details
//     @Column(name = "living_situation", columnDefinition = "TEXT")
//     private String livingSituation;

//     @Column(name = "previous_experience", columnDefinition = "TEXT")
//     private String previousExperience;

//     @Column(name = "family_composition", columnDefinition = "TEXT")
//     private String familyComposition;

//     // ✅ Status as ENUM - matches MySQL ENUM('PENDING','APPROVED','REJECTED')
//     @Column(nullable = false, columnDefinition = "ENUM('PENDING','APPROVED','REJECTED')")
//     @Enumerated(EnumType.STRING)
//     private Status status = Status.PENDING;

//     @Column(name = "created_at", updatable = false)
//     private LocalDateTime createdAt;

//     @Column(name = "updated_at")
//     private LocalDateTime updatedAt;

//     // ✅ Status enum - matches database ENUM
//     public enum Status {
//         PENDING, APPROVED, REJECTED
//     }

//     // ✅ Lifecycle hooks
//     @PrePersist
//     protected void onCreate() {
//         createdAt = LocalDateTime.now();
//         updatedAt = LocalDateTime.now();
//     }

//     @PreUpdate
//     protected void onUpdate() {
//         updatedAt = LocalDateTime.now();
//     }

//     // ========================================
//     // CONSTRUCTORS
//     // ========================================

//     public AdoptionRequest() {
//     }

//     public AdoptionRequest(Long userId, Long petId, String livingSituation,
//             String previousExperience, String familyComposition) {
//         this.userId = userId;
//         this.petId = petId;
//         this.livingSituation = livingSituation;
//         this.previousExperience = previousExperience;
//         this.familyComposition = familyComposition;
//         this.status = Status.PENDING;
//     }

//     // ========================================
//     // GETTERS AND SETTERS
//     // ========================================

//     public Long getId() {
//         return id;
//     }

//     public void setId(Long id) {
//         this.id = id;
//     }

//     public Long getUserId() {
//         return userId;
//     }

//     public void setUserId(Long userId) {
//         this.userId = userId;
//     }

//     public Long getPetId() {
//         return petId;
//     }

//     public void setPetId(Long petId) {
//         this.petId = petId;
//     }

//     public String getLivingSituation() {
//         return livingSituation;
//     }

//     public void setLivingSituation(String livingSituation) {
//         this.livingSituation = livingSituation;
//     }

//     public String getPreviousExperience() {
//         return previousExperience;
//     }

//     public void setPreviousExperience(String previousExperience) {
//         this.previousExperience = previousExperience;
//     }

//     public String getFamilyComposition() {
//         return familyComposition;
//     }

//     public void setFamilyComposition(String familyComposition) {
//         this.familyComposition = familyComposition;
//     }

//     public Status getStatus() {
//         return status;
//     }

//     public void setStatus(Status status) {
//         this.status = status;
//     }

//     // Helper method for String status
//     public void setStatus(String status) {
//         this.status = Status.valueOf(status.toUpperCase());
//     }

//     public String getStatusString() {
//         return status.name();
//     }

//     public LocalDateTime getCreatedAt() {
//         return createdAt;
//     }

//     public void setCreatedAt(LocalDateTime createdAt) {
//         this.createdAt = createdAt;
//     }

//     public LocalDateTime getUpdatedAt() {
//         return updatedAt;
//     }

//     public void setUpdatedAt(LocalDateTime updatedAt) {
//         this.updatedAt = updatedAt;
//     }

//     @Override
//     public String toString() {
//         return "AdoptionRequest{" +
//                 "id=" + id +
//                 ", userId=" + userId +
//                 ", petId=" + petId +
//                 ", status=" + status +
//                 ", createdAt=" + createdAt +
//                 '}';
//     }
// }

//1.3
package com.petadoption.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "adoption_requests")
public class AdoptionRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "pet_id", nullable = false)
    private Long petId;

    @Column(name = "living_situation", columnDefinition = "TEXT")
    private String livingSituation;

    @Column(name = "previous_experience", columnDefinition = "TEXT")
    private String previousExperience;

    @Column(name = "family_composition", columnDefinition = "TEXT")
    private String familyComposition;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ✅ ENUM
    public enum Status {
        PENDING, APPROVED, REJECTED
    }

    // ✅ LIFECYCLE HOOKS
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ========================================
    // GETTERS AND SETTERS
    // ========================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPetId() {
        return petId;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }

    public String getLivingSituation() {
        return livingSituation;
    }

    public void setLivingSituation(String livingSituation) {
        this.livingSituation = livingSituation;
    }

    public String getPreviousExperience() {
        return previousExperience;
    }

    public void setPreviousExperience(String previousExperience) {
        this.previousExperience = previousExperience;
    }

    public String getFamilyComposition() {
        return familyComposition;
    }

    public void setFamilyComposition(String familyComposition) {
        this.familyComposition = familyComposition;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    // ✅ Helper method to set status from String
    public void setStatus(String status) {
        this.status = Status.valueOf(status.toUpperCase());
    }

    // ✅ THIS METHOD WAS MISSING - ADD IT!
    public String getStatusString() {
        return status.name();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}