package com.arduinodatabase.model

import com.google.cloud.firestore.annotation.ServerTimestamp
import java.time.LocalDateTime
import java.util.*

data class Temperature(
    val value: Double,
    val unit: String = "Celsius",
    @ServerTimestamp
    val date: Date? = null
)
