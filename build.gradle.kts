plugins {
    kotlin("jvm") version "1.8.21"
    id("com.github.johnrengelman.shadow") version ("7.1.2")
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("br.com.guiabolso:FixedLengthFileHandler:1.0.0")

    implementation("io.github.microutils:kotlin-logging:2.1.23")
    runtimeOnly("org.apache.logging.log4j:log4j-slf4j18-impl:2.18.0")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}

application {
    mainClass.set("MainKt")
}
