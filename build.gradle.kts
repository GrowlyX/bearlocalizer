import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "7.1.2"
    kotlin("jvm") version "1.9.0"
}

group = "io.liftgate.localize"
version = "1.2-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(kotlin("stdlib"))
    testImplementation(kotlin("test"))

    compileOnly("com.amihaiemil.web:eo-yaml:7.0.8")
    testImplementation("com.amihaiemil.web:eo-yaml:7.0.8")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.javaParameters = true
    kotlinOptions.jvmTarget = "17"
}

tasks.withType<ShadowJar> {
    archiveClassifier.set("")
    archiveFileName.set(
        "bearlocalizer.jar"
    )
}

publishing {
    publications {
        register(
            name = "mavenJava",
            type = MavenPublication::class,
            configurationAction = shadow::component
        )
    }
}
