package com.arduinodatabase.controller


import com.arduinodatabase.service.SerialPortService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

val log = KotlinLogging.logger {}

@RestController
@RequestMapping("/ports")
class PortController(private val portService: SerialPortService) {

    @GetMapping
    fun getAllPorts(): List<String> {
        return portService.getSerialPorts()?.map { it.systemPortName } ?: emptyList()
    }

    @PostMapping("/read")
    fun readPort(portName: String) {
        val port = portService.getSerialPorts()?.find { it.systemPortName == portName }
        log.info { if(port == null) "No port found" else "Reading from port ${port.systemPortName}" }
        port?.let { portService.setupAndReadPort(it) }
    }

    @PostMapping("/close")
    fun closePort() {
        portService.closePorts()
    }
}