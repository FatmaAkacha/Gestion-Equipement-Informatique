package org.ms.facture_servicePJ.Front;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Autorise toutes les routes
                .allowedOrigins("http://localhost:8080") // Autorise uniquement cette origine
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Méthodes autorisées
                .allowedHeaders("*") // Autorise tous les en-têtes
                .allowCredentials(true); // Autorise les cookies ou identifiants
    }
}
