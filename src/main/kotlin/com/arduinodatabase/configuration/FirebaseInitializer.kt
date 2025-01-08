package com.arduinodatabase.configuration

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.Firestore
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.FirestoreClient
import java.io.FileInputStream
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FirebaseConfiguration() {

    @Bean
    fun firestore(): Firestore? {
        val serviceAccount = FileInputStream("src/main/resources/static/serviceAccount.json")
        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .setProjectId("sample-firebase-ai-app-d2b66")
            .build()

        val firebaseApp = FirebaseApp.initializeApp(options);
        return FirestoreClient.getFirestore(firebaseApp);
    }
}