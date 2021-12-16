import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.31"
    kotlin("plugin.serialization") version "1.5.31"
    id("org.jetbrains.compose") version "1.0.0"
}

group = "me.palander"
version = "1.0"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.1")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.1")

}

tasks.withType<JavaCompile> {
    targetCompatibility = "16"
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "16"
    kotlinOptions.freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
}

compose.desktop {
    application {
        mainClass = "com.antiafk.app.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "AntiAfk"
            packageVersion = "1.0.0"
        }
    }
}