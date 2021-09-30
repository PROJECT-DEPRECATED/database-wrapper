plugins {
    groovy
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = rootProject.group
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.codehaus.groovy:groovy-all:3.0.5")
    implementation(project(":wrapper"))
}

tasks {
    compileGroovy {
        groovyOptions.encoding = "UTF-8"
    }

    shadowJar {
        archiveBaseName.set(rootProject.name)
        archiveVersion.set("")
        archiveClassifier.set("test")

        manifest {
            attributes["Main-Class"] = "net.projecttl.test.program.TestProgram"
        }
    }
}