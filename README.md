# configuration
A simple configuration manager using [configurate](https://github.com/SpongePowered/Configurate) by [SpongePowered](https://github.com/SpongePowered).

# Use/Install

### Maven
```xml
<dependencies>
    <dependency>
        <groupId>io.github.eupedroosouza</groupId>
        <artifactId>configuration</artifactId>
        <version>x.y.z</version>
    </dependency>
</dependencies>
```
If you want to use snapshots of the new versions not yet released:
```xml
<repositories>
    <repository>
        <id>sonatype-snapshots</id>
        <name>Sonatype Snapshot Repository</name>
        <url>https://central.sonatype.com/repository/maven-snapshots/</url>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>io.github.eupedroosouza</groupId>
        <artifactId>configuration</artifactId>
        <version>x.y.z-SNASPHOT</version>
    </dependency>
</dependencies>
```

### Gradle

#### Gradle With Groovy
````groovy
dependencies {
    implementation "io.github.eupedroosouza:configuration:x.y.z"
}
````
If you want to use snapshots of the new versions not yet released:
```groovy
repositories {
    maven { url 'https://central.sonatype.com/repository/maven-snapshots/' }
}
dependencies {
    implementation "io.github.eupedroosouza:configuration:x.y.z-SNAPSHOT"
}
```

#### Gradle with Kotlin DSL
```kotlin
dependencies {
    implementation("io.github.eupedroosouza:configuration:x.y.z")
}
```
If you want to use snapshots of the new versions not yet released:
```kotlin
repositories {
    maven("https://central.sonatype.com/repository/maven-snapshots/")
}
dependencies {
    implementation("io.github.eupedroosouza:configuration:x.y.z-SNAPSHOT")
}
```

