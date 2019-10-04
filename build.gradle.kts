plugins {
    java
    idea
}

java {
    sourceCompatibility = JavaVersion.VERSION_12
    targetCompatibility = JavaVersion.VERSION_12
}

dependencies {
    compile(files("bin/jh.jar"))
    compile(files("bin/VisuTuring.jar"))
}