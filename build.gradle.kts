buildscript {
    val agp_version by extra("7.4.2")
    val agp_version1 by extra("8.1.3")
    val agp_version2 by extra("8.2.0")
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
    }

    extra.apply {
        set("nav_version", "2.5.3")
        set("room_version", "2.5.1")
        set("compose_version", "1.0.5")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id("com.google.gms.google-services") version "4.4.0" apply false
    id ("com.google.dagger.hilt.android") version "2.48.1" apply false
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false


}