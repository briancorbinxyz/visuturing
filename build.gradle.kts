plugins {
    java
    idea
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
    runtimeOnly("ch.qos.logback:logback-classic:1.3.0-alpha5")
    implementation("javax.help:javahelp:2.0.02")
    implementation("org.slf4j:slf4j-api:2.0.0-alpha1")
}

// https://docs.gradle.org/current/dsl/org.gradle.api.tasks.wrapper.Wrapper.html
// Update here and run `gradlew wrapper` to update the distribution
tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL
    gradleVersion = "7.2"
}