import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

object Version {
    const val serialization = "1.3.1"
    const val datetime = "0.3.1"
    const val jvm = "16"
    const val project = "1.0"
    const val packageVersion = "1.0.0"
}

plugins {
    kotlin("jvm") version "1.5.31"
    kotlin("plugin.serialization") version "1.5.31"
    id("org.jetbrains.compose") version "1.0.0"
}

group = "me.palander"
version = Version.project

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${Version.serialization}")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:${Version.datetime}")

}

tasks.withType<JavaCompile> {
    targetCompatibility = Version.jvm
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = Version.jvm
    kotlinOptions.freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
}

compose.desktop {
    application {
        mainClass = "com.antiafk.app.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "AntiAfk"
            packageVersion = Version.packageVersion
        }
    }
}