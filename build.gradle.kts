plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.1"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web:3.3.2")
    implementation("com.google.firebase:firebase-admin:9.4.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.1")
    implementation("com.fazecast:jSerialComm:2.11.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect:2.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect:2.0.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.3.1")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:1.9.25")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.10.3")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
