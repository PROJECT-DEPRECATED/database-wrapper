plugins {
    kotlin("jvm") version "1.5.31"
}

group = "net.projecttl"
version = "0.3.0"

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    repositories {
        mavenCentral()
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(11))
        }
    }
}

subprojects {
    repositories {
        mavenCentral()
    }

    dependencies {
        implementation(kotlin("stdlib"))
        implementation("org.xerial:sqlite-jdbc:3.36.0.3")
        implementation("mysql:mysql-connector-java:8.0.26")
        implementation("org.mongodb:mongo-java-driver:3.12.10")
        implementation("org.yaml:snakeyaml:1.29")
    }
}