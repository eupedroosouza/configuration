plugins {
    java
    `java-library`
    `maven-publish`
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
    repositories {
        maven("https://maven.pkg.github.com/eupedroosouza/configuration") {
            credentials {
                username = System.getenv("PACKAGES_REPOSITORY_USERNAME")
                password = System.getenv("PACKAGES_REPOSITORY_TOKEN")
            }
        }
    }
    publications {
        create<MavenPublication>(project.name) {
            from(components["java"])

            groupId = rootProject.group.toString()
            artifactId = rootProject.name
            version = rootProject.version.toString()
        }
    }
}