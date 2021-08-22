plugins {
    kotlin("jvm") version "1.5.21"
}

group = "net.shilu.neis"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("io.ktor:ktor-client-core:1.6.2")
    implementation("io.ktor:ktor-client-cio:1.6.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.12.4")
}
