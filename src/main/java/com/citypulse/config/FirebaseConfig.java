package com.citypulse.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import org.springframework.core.io.ClassPathResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@Configuration
public class FirebaseConfig {

    @Bean
    public Firestore firestore() throws IOException {
        // Try env var path first (recommended), otherwise fallback to classpath resource
        String envPath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
        InputStream creds;
        if (Objects.nonNull(envPath) && !envPath.isBlank()) {
            creds = new FileInputStream(envPath);
        } else {
            ClassPathResource r = new ClassPathResource("citypulse-firebase-key.json");
            creds = r.getInputStream();
        }
        GoogleCredentials credentials = GoogleCredentials.fromStream(creds);
        FirestoreOptions options = FirestoreOptions.newBuilder()
                .setCredentials(credentials)
                .build();
        return options.getService();
    }
}