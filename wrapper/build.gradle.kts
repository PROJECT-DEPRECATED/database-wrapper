plugins {
    id("com.github.johnrengelman.shadow") version "7.0.0"
    id("org.jetbrains.dokka") version "1.5.0"
    `maven-publish`
    signing
}

group = rootProject.group
version = rootProject.version

tasks {
    javadoc {
        options.encoding = "UTF-8"
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