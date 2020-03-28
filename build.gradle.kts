plugins {
    `java-library`
    `maven-publish`
}

group = "com.github.ktgw0316"
version = "1.1.3.0"

dependencies {
    implementation(files("lib/mlibwrapper-jai-1.1.3.jar"))
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
