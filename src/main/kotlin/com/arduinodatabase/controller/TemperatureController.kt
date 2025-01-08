package com.arduinodatabase.controller

import com.arduinodatabase.model.Temperature
import com.arduinodatabase.service.FirebaseService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TemperatureController(val firebaseService: FirebaseService) {

    @GetMapping("/data")
    fun getTemperature(): MutableMap<String, Any>? {
        return firebaseService.getTemperature("a2x8t0DELNYD5nhhbqSx")
    }
   
    @PostMapping
    fun createTemperature() {
        firebaseService.saveTemperature(Temperature(25.0))
    }
}

