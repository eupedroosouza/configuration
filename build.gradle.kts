plugins {
    java
    `java-library`
    `maven-publish`
    signing
    alias(libs.plugins.nexusPublishPlugin)
    alias(libs.plugins.spotless)
}

if (project.findProperty("snapshot") == "true") {
    version = "$version-SNAPSHOT"
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(8))

    withSourcesJar()
    withJavadocJar()

    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    mavenCentral()
}

dependencies {
    api(libs.configurateCore)

    testImplementation(platform("org.junit:junit-bom:${rootProject.property("junit_version")}"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(libs.configurateYaml)
}

tasks {
    test {
        useJUnitPlatform()
    }
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
}

indraSpotlessLicenser {
    licenseHeaderFile(rootProject.file("LICENSE"))
    newLine(true)
}

publishing {
    publications {
        create<MavenPublication>(project.name) {
            from(components["java"])

            pom {
                name.set("configuration");
                description.set("A simple configuration manager using configurate (by SpongePowered)")
                url.set("https://github.com/eupedroosouza/configuration")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("http://www.opensource.org/licenses/mit-license.php")
                    }
                }

                developers {
                    developer {
                        id.set("eupedroosouza")
                        name.set("Pedro Souza")
                        email.set("66704494+eupedroosouza@users.noreply.github.com")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/eupedroosouza/configuration.git")
                    developerConnection.set("scm:git:ssh://github.com/eupedroosouza/configuration.git")
                    url.set("https://github.com/eupedroosouza/configuration")
                }
            }
        }
    }
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://ossrh-staging-api.central.sonatype.com/service/local/"))
            snapshotRepositoryUrl.set(uri("https://central.sonatype.com/repository/maven-snapshots/"))
            username.set(project.findProperty("sonatype.username")?.toString() ?: System.getenv("SONATYPE_USERNAME"))
            password.set(project.findProperty("sonatype.password")?.toString() ?: System.getenv("SONATYPE_PASSWORD"));
        }
    }
}

signing {
    val signedKey = project.findProperty("signed.key")?.toString() ?: System.getenv("GPG_SECRET_KEY")
    val signedPassword = project.findProperty("signed.password")?.toString() ?: System.getenv("GPG_PASSPHRASE")

    if (signedKey != null && signedPassword != null) {
        useInMemoryPgpKeys(signedKey, signedPassword)
    } else {
        useGpgCmd()
    }

    sign(publishing.publications[project.name])
}