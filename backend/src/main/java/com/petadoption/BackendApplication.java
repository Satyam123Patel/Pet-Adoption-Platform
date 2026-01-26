//1
package com.petadoption;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
}

// package com.petadoption;

// import org.springframework.boot.SpringApplication;
// import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.context.annotation.Bean;
// import org.springframework.security.crypto.password.PasswordEncoder;

// @SpringBootApplication
// public class BackendApplication {

//     public static void main(String[] args) {
//         SpringApplication.run(BackendApplication.class, args);
//     }

//     // ✅ Generate BCrypt hash on startup
//     @Bean
//     CommandLineRunner generateAdminHash(PasswordEncoder passwordEncoder) {
//         return args -> {
//             String password = "admin123";
//             String hash = passwordEncoder.encode(password);

//             System.out.println("\n");
//             System.out.println("========================================");
//             System.out.println("🔐 GENERATED BCRYPT HASH FOR ADMIN");
//             System.out.println("========================================");
//             System.out.println("Password: " + password);
//             System.out.println("Hash: " + hash);
//             System.out.println("Length: " + hash.length());
//             System.out.println("========================================");
//             System.out.println("💡 COPY THIS HASH AND RUN IN MYSQL:");
//             System.out.println("========================================");
//             System.out.println("UPDATE admins SET password_hash = '" + hash + "'");
//             System.out.println("WHERE email = 'satyampatelkatni2003@gmail.com';");
//             System.out.println("");
//             System.out.println("UPDATE users SET password = '" + hash + "'");
//             System.out.println("WHERE email = 'satyampatelkatni2003@gmail.com';");
//             System.out.println("========================================");

//             // Self-test
//             boolean matches = passwordEncoder.matches(password, hash);
//             System.out.println("✅ Self-test verification: " + matches);
//             System.out.println("========================================\n");
//         };
//     }
// }