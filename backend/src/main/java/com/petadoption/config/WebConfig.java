//1
// package com.petadoption.config;

// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
// import org.springframework.web.servlet.config.annotation.CorsRegistry;

// @Configuration
// public class WebConfig implements WebMvcConfigurer {

//         @Override
//         public void addResourceHandlers(ResourceHandlerRegistry registry) {
//                 // Serve all images from single directory
//                 registry.addResourceHandler("/images/**")
//                                 .addResourceLocations("file:D:/Petpostimages/")
//                                 .setCachePeriod(3600);
//         }

//         @Override
//         public void addCorsMappings(CorsRegistry registry) {
//                 registry.addMapping("/**")
//                                 .allowedOrigins("http://localhost:5173")
//                                 .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                                 .allowedHeaders("*")
//                                 .allowCredentials(true);
//         }
// }

// package com.petadoption.config;

// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
// import org.springframework.web.servlet.config.annotation.CorsRegistry;

// /**
//  * ✅ DUAL IMAGE SOURCES:
//  * 1. Sample pets: src/main/resources/static/images/ (embedded in JAR)
//  * 2. User uploads: D:/Petpostimages/ (external directory)
//  */
// @Configuration
// public class WebConfig implements WebMvcConfigurer {

//         @Override
//         public void addResourceHandlers(ResourceHandlerRegistry registry) {
//                 // ✅ Serve images from BOTH locations
//                 // Spring will check both: first external directory, then classpath
//                 registry.addResourceHandler("/images/**")
//                                 .addResourceLocations(
//                                                 "file:D:/Petpostimages/", // User-uploaded pets
//                                                 "classpath:/static/images/" // Sample pets (from database)
//                                 )
//                                 .setCachePeriod(3600);

//                 System.out.println("✅ Image handler configured:");
//                 System.out.println("   - User uploads: D:/Petpostimages/");
//                 System.out.println("   - Sample pets: classpath:/static/images/");
//         }

//         @Override
//         public void addCorsMappings(CorsRegistry registry) {
//                 registry.addMapping("/**")
//                                 .allowedOrigins("http://localhost:5173")
//                                 .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                                 .allowedHeaders("*")
//                                 .allowCredentials(true);
//         }
// }

package com.petadoption.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

/**
 * ✅ DUAL IMAGE SOURCES:
 * 1. User uploads: D:/Petpostimages/ (external directory)
 * 2. Sample pets: src/main/resources/static/images/ (embedded in JAR)
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
                // ✅ Serve images from BOTH locations
                // Spring checks in order: first external directory, then classpath
                registry.addResourceHandler("/images/**")
                                .addResourceLocations(
                                                "file:D:/Petpostimages/", // User-uploaded pets (pending approval)
                                                "classpath:/static/images/" // Sample pets from database
                                )
                                .setCachePeriod(3600);

                System.out.println("✅ ========================================");
                System.out.println("✅ Image handler configured:");
                System.out.println("✅   - User uploads: D:/Petpostimages/");
                System.out.println("✅   - Sample pets: classpath:/static/images/");
                System.out.println("✅ ========================================");
        }

        @Override
        public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                                .allowedOrigins("http://localhost:5173")
                                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                                .allowedHeaders("*")
                                .allowCredentials(true);
        }
}