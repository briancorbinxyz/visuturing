plugins {
    java
    idea
    application
    id("com.diffplug.spotless") version "5.14.3"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(12))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("javax.help:javahelp:2.0.02")
    implementation("org.slf4j:slf4j-api:2.0.0-alpha1")
    implementation("com.google.code.findbugs:jsr305:3.0.2")
    runtimeOnly("ch.qos.logback:logback-classic:1.3.0-alpha5")
}

// https://docs.gradle.org/current/dsl/org.gradle.api.tasks.wrapper.Wrapper.html
// Update here and run `gradlew wrapper` to update the distribution
tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL
    gradleVersion = "7.2"
}

// https://docs.gradle.org/current/userguide/application_plugin.html
// Update here and run `gradlew run` to run the application
application {
    mainClass.set("org.keiosu.visuturing.gui.VisuTuring")
}

// https://plugins.gradle.org/plugin/com.diffplug.spotless
// https://github.com/diffplug/spotless/tree/main/plugin-gradle
// Use `gradlew build` to view issues, `gradlew spotlessApply` to apply changes
spotless {
    java {
        googleJavaFormat().aosp()
    }
}