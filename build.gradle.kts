plugins { java
    idea
}

java {
    sourceCompatibility = JavaVersion.VERSION_12
    targetCompatibility = JavaVersion.VERSION_12
}

repositories {
    mavenCentral()
}

dependencies {
    runtime("ch.qos.logback:logback-classic:1.3.0-alpha5")
    compile("javax.help:javahelp:2.0.02")
    compile("org.slf4j:slf4j-api:2.0.0-alpha1")
}