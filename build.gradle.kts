plugins {
    kotlin("jvm") version "1.5.10"
}

group = "net.projecttl"
version = "0.1.0"

allprojects {
    javaToolChain()

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    javaToolChain()

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

fun javaToolChain() {
    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(11))
        }
    }
}