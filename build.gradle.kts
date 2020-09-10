plugins {
    `java-library`
    `maven-publish`
}

group = "com.github.ktgw0316"
version = "1.1.3.0"

dependencies {
    implementation(files("lib/mlibwrapper-jai-1.1.3.jar"))
    implementation("com.github.ktgw0316:image-codec-jpeg:6.1.13.1")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    maven("https://jitpack.io")
}
