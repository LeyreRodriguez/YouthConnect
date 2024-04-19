import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")

    kotlin("kapt")
    id("com.google.dagger.hilt.android")

}

android {
    namespace = "com.example.youthconnect"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.youthconnect"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}


val firestore = "24.9.1"
val auth = "22.2.0"
val storage = "20.3.0"
val firebase = "32.5.0"
val analytics = "21.5.0"
val scanner = "16.1.0"
val barcode = "18.3.0"
val playServicesAuth = "19.2.0"
val activityCompose = "1.8.0"
val composeMaterial = "1.2.1"
val composeMaterial2 = "1.4.2"
val icons = "1.5.4"
val hilt = "1.0.0"
val viewmodelCompose = "2.6.1"
val viewmodelCompose2 = "2.6.0"
val navigationCompose = "2.4.1"
val bom = "2023.03.00"
val livedata = "1.3.2"
val camera = "1.0.2"
val cameraView = "1.0.0-alpha31"
val ktor = "2.3.6"
val appCompact = "1.6.1"
val constraintLayout = "2.1.4"
val material = "1.10.0"
val coil = "2.4.0"
val pager = "0.32.0"
val permission = "0.31.3-beta"
val imageCropper = "4.5.0"
val jUnit = "4.13.2"
val mockito = "3.11.2"
val jUnit2 = "1.1.5"
val espresso = "3.5.1"
val testng = "6.9.6"
val zxing = "3.4.1"
val zxing2 = "4.3.0"
val ktx = "1.9.0"
val lifeCycle = "2.6.2"
val lifeCycle2 = "2.4.0"
val navigation = "2.7.5"
val navigationCompose2 = "2.5.3"
val daggerHilt = "2.48.1"
val activityCompose2 = "1.5.0"
val activityKtx= "1.4.0"

dependencies {


    //Firebase
    implementation("com.google.firebase:firebase-firestore:$firestore")
    implementation("com.google.firebase:firebase-auth:$auth")
    implementation("com.google.firebase:firebase-storage:$storage")
    implementation("com.google.firebase:firebase-database:$storage")
    implementation(platform("com.google.firebase:firebase-bom:$firebase"))
    implementation("com.google.firebase:firebase-analytics")
    implementation ("com.google.firebase:firebase-analytics:$analytics")
    implementation("com.google.firebase:firebase-messaging")

    //GooglePlay Services
    implementation("com.google.android.gms:play-services-code-scanner:$scanner")
    implementation("com.google.android.gms:play-services-mlkit-barcode-scanning:$barcode")
    implementation("com.google.android.gms:play-services-auth:$playServicesAuth")


    //Jetpack Compose
    implementation("androidx.activity:activity-compose:$activityCompose")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.wear.compose:compose-material:$composeMaterial")
    implementation("androidx.compose.material:material-icons-extended:$icons")   //Icons
    implementation ("androidx.compose.material:material:$composeMaterial2")  //Navigation

    implementation("androidx.hilt:hilt-navigation-compose:$hilt")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$viewmodelCompose")
    implementation ("androidx.navigation:navigation-compose:$navigationCompose") // Usa la última versión disponible
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:$viewmodelCompose2")
    implementation ("androidx.lifecycle:lifecycle-runtime-compose:$viewmodelCompose2")
    implementation(platform("androidx.compose:compose-bom:$bom"))
    implementation("androidx.compose.runtime:runtime-livedata:$livedata")


    // CameraX
    implementation ("androidx.camera:camera-camera2:$camera")
    implementation ("androidx.camera:camera-lifecycle:$camera")
    implementation ("androidx.camera:camera-view:$cameraView")


    // Networking

    implementation ("io.ktor:ktor-client-android:$ktor")
    implementation ("io.ktor:ktor-client-json-jvm:$ktor")
    implementation ("io.ktor:ktor-client-serialization-jvm:$ktor")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor")


    // UI Enchancements

    implementation("androidx.appcompat:appcompat:$appCompact")
    implementation("androidx.constraintlayout:constraintlayout:$constraintLayout")
    implementation("com.google.android.material:material:$material")
    implementation("io.coil-kt:coil-compose:$coil")
    implementation ("com.google.accompanist:accompanist-pager:$pager")
    implementation ("com.google.accompanist:accompanist-permissions:$permission")
    implementation ("com.vanniktech:android-image-cropper:$imageCropper")



    // QR & Barcode Scanning
    implementation("com.google.zxing:core:$zxing")
    implementation("com.journeyapps:zxing-android-embedded:$zxing2")


    //AndroidX Core y lifecycle

    implementation("androidx.core:core-ktx:$ktx")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifeCycle")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:$lifeCycle2")



    //Navigation

    implementation("androidx.navigation:navigation-fragment-ktx:$navigation")
    implementation("androidx.navigation:navigation-ui-ktx:$navigation")
    implementation ("androidx.navigation:navigation-compose:$navigationCompose2")

    //Dagger Hilt
    implementation ("com.google.dagger:hilt-android:$daggerHilt")


    // Activity Compose

    implementation("androidx.activity:activity-compose:$activityCompose2")
    implementation ("androidx.activity:activity-ktx:$activityKtx")
    implementation ("androidx.activity:activity-ktx:$activityKtx")


    // Testing
    testImplementation("junit:junit:$jUnit")
    testImplementation("org.mockito:mockito-core:$mockito")
    testImplementation("org.mockito:mockito-inline:$mockito")
    androidTestImplementation("androidx.test.ext:junit:$jUnit2")
    androidTestImplementation("androidx.test.espresso:espresso-core:$espresso")
    androidTestImplementation(platform("androidx.compose:compose-bom:$bom"))
    androidTestImplementation("org.testng:testng:$testng")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")



    kapt ("com.google.dagger:hilt-compiler:$daggerHilt")

}


kapt {
    correctErrorTypes = true
}
hilt {
    enableExperimentalClasspathAggregation = true
}
