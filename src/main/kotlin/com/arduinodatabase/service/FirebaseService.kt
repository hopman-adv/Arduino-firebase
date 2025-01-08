package com.arduinodatabase.service

import com.arduinodatabase.model.Temperature
import com.google.cloud.firestore.Firestore
import org.springframework.stereotype.Service

@Service
class FirebaseService(val firebase: Firestore) {
    fun getTemperature(id: String): MutableMap<String, Any>? {
        return firebase.collection("temperature").document(id).get().get().data
    }
    
    fun saveTemperature(temperature: Temperature) {
        firebase.collection("temperature").document().set(temperature)
    }
    
}