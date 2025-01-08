package com.arduinodatabase.service

import com.arduinodatabase.model.Temperature
import com.fazecast.jSerialComm.SerialPort
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.springframework.stereotype.Service

private val log = KotlinLogging.logger {}

@Service
class SerialPortService(val firebaseService: FirebaseService) {

    private var scope: CoroutineScope? = null
    private val ports = mutableListOf<SerialPort>()
    private val mutex = Mutex()

    fun getSerialPorts(): Array<out SerialPort>? {
        return SerialPort.getCommPorts().also {
            log.info { this.getSerialPorts() }
        }
    }

    fun setupAndReadPort(port: SerialPort) {
        scope?.let {
            if (it.isActive) {
                log.info { "Coroutine is already active. Cannot start a new one." }
                return
            }
        }

        scope = CoroutineScope(Dispatchers.IO)
        ports.add(port)

        val baudRate = 9600
        val dataBits = 8
        val stopBits = SerialPort.ONE_STOP_BIT
        val parity = SerialPort.NO_PARITY

        // Sets all serial port parameters at one time
        with(ports[0]) {
            setComPortParameters(baudRate, dataBits, stopBits, parity)
            setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 1000, 0)
            openPort()
        }
        SerialPort.getCommPorts().find { it.isOpen }?.run {
            log.info { "Port ${this.systemPortName} is open." }
        }

        scope?.launch {
            mutex.withLock {
                try {
                    var tempTemp = 0.0
                    while (isActive) {
                        val readBuffer = ByteArray(100)
                        val numRead: Int = port.readBytes(readBuffer, readBuffer.size)
                        log.info { "Read $numRead bytes." }
                        // Convert bytes to String
                        if (numRead > 0) {
                            val text = String(readBuffer, 0, numRead, charset("UTF-8"))
                            log.info { "Received -> $text" }
                            val pattern = Regex("""\d+\.\d{2}""")
                            text.split(";").filter { it.isNotEmpty() && pattern.matches(it) }
                                .forEach {
                                    val temperature = it.toDouble()
                                    if (temperature in (tempTemp - 0.3)..(tempTemp + 0.3)) {
                                        log.info { "Temperature $temperature is the same as the previous one. Skipping value." }
                                        return@forEach
                                    } else {
                                        tempTemp = temperature
                                        firebaseService.saveTemperature(Temperature(temperature))
                                    }
                                }
                        }
                    }
                } catch (e: Exception) {
                    log.info { "Problem occurred. Closing ports and coroutine." }
                    e.printStackTrace()
                } finally {
                    port.closePort() // Close the port
                    if (ports.size > 0) ports.clear() else log.info { "No ports to remove." }
                    scope?.coroutineContext?.cancel() ?: log.info { "Coroutine context already canceled." }
                    log.info { "Port ${port.systemPortName} is closed." }
                }
            }
        }
    }

    fun closePorts() {
        scope?.let {
            if (it.isActive) {
                log.info { "Coroutine is active. Stopping coroutine." }
                it.coroutineContext.cancel()
            } else {
                log.info { "Coroutine is not active." }
            }
        }
        ports.onEach { it.closePort() }
        ports.removeAll { true }
        scope = null
        log.info { "Ports closed, coroutine stopped." }
    }
}