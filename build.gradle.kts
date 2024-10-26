plugins {
    kotlin("jvm") version "1.9.20"
    id("org.jetbrains.compose") version "1.5.11"
    application
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("org.xerial:sqlite-jdbc:3.34.0")
    testImplementation(kotlin("test"))
}

application {
    mainClass.set("com.ceycourier.MainAppKt")
}

kotlin {
    jvmToolchain(11)
}