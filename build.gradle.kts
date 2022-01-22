plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("org.jetbrains.dokka") version "1.6.10"
    kotlin("jvm") version "1.6.10"

    `maven-publish`
    signing
}

group = "net.projecttl"
version = "0.5.0"


java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.xerial:sqlite-jdbc:3.36.0.3")
    implementation("mysql:mysql-connector-java:8.0.26")
    implementation("org.mongodb:mongo-java-driver:3.12.10")
    // implementation("org.yaml:snakeyaml:1.29")
}

tasks {
    javadoc {
        options.encoding = "UTF-8"
    }

    compileKotlin {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
    }

    create<Jar>("sourcesJar") {
        archiveClassifier.set("sources")
        from(sourceSets["main"].allSource)
    }

    create<Jar>("javadocJar") {
        archiveClassifier.set("javadoc")
        dependsOn("dokkaHtml")
        from("$buildDir/dokka/html")
    }
}

publishing {
    publications {
        create<MavenPublication>(rootProject.name) {
            artifactId = "database-wrapper"
            from(components["java"])
            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"])

            repositories {
                maven {
                    name = "ProjectCentral"
                    val releasesRepoUrl = "https://repo.projecttl.net/repository/maven-releases/"
                    val snapshotsRepoUrl = "https://repo.projecttl.net/repository/maven-snapshots/"
                    url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)

                    credentials.runCatching {
                        val nexusUsername: String by project
                        val nexusPassword: String by project
                        username = nexusUsername
                        password = nexusPassword
                    }
                }

                pom {
                    name.set(rootProject.name)
                    description.set("This is database wrapper")
                    url.set("https://github.com/DEVProject04/database-wrapper")
                    licenses {
                        license {
                            name.set("MIT License")
                            url.set("https://github.com/DEVProject04/database-wrapper/blob/master/LICENSE")
                        }
                    }
                    developers {
                        developer {
                            id.set("DevProject04")
                            name.set("Dev_Project")
                            email.set("me@projecttl.net")
                        }
                    }
                    scm {
                        connection.set("scm:git:https://github.com/DEVProject04/database-wrapper.git")
                        developerConnection.set("scm:git:https://github.com/DEVProject04/database-wrapper.git")
                        url.set("https://github.com/DEVProject04/database-wrapper.git")
                    }
                }
            }
        }
    }
}

signing {
    isRequired = true
    sign(publishing.publications[rootProject.name])
}