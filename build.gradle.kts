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
    jcenter()
}

dependencies {
    runtimeOnly("ch.qos.logback:logback-classic:1.3.0-alpha5")
    implementation("javax.help:javahelp:2.0.02")
    implementation("org.slf4j:slf4j-api:2.0.0-alpha1")
}

tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL

}