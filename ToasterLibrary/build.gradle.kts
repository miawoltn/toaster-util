import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.include

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
}


group = "com.github.miawoltn"
version = "0.1.32"

android {
    namespace = "com.miawoltn.toasterlibrary"
    compileSdk = 32

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        renderscriptTargetApi = 19
        renderscriptSupportModeEnabled = true
        consumerProguardFiles("consumer-rules.pro")
        externalNativeBuild {
            cmake {
                cppFlags("")
            }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    publishing {
//        singleVariant("release") {
//            withSourcesJar()
//            withJavadocJar()
//        }

        multipleVariants {
            withSourcesJar()
            withJavadocJar()
        }
    }

    packagingOptions {
        jniLibs.pickFirsts.add("**/*.so")
    }

}

dependencies {
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // also, if using ICAO, enrollment, verification or match funcionality, you need to
    // import either face_icao or face_local_matcher as explained below
//    implementation("com.identy.face:face:5.1.0@aar")
//    implementation("com.identy.face:face_liveness:5.2.0@aar")
//    implementation("com.identy.face.localmatcher:face_local_matcher:4.12.0@aar")
//    implementation("com.identy.face.icao:face_icao:4.12.1@aar")
    implementation(group = "com.identy.face", name= "face", version= "5.2.0", ext= "aar")
    implementation(group = "com.identy.face.icao", name= "face_icao", version= "4.12.1", ext= "aar")
    implementation(group = "com.identy.face.localmatcher", name = "face_local_matcher", version = "4.12.0", ext = "aar")
    implementation(group = "com.identy.face", name= "face_liveness", version= "5.2.0", ext= "aar")

    /* SDK additional libraries required */
    implementation("com.vmadalin:easypermissions-ktx:1.0.0")
//    implementation("com.android.volley:volley:1.2.1")
//    val room_version = "2.6.1"
//    implementation("android.arch.persistence.room:runtime:$room_version")
//    annotationProcessor("android.arch.persistence.room:compiler:$room_version")
//    compileOnly("android.arch.lifecycle:livedata:1.1.1") {
//        isForce = true;
//    }
//    compileOnly("android.arch.lifecycle:viewmodel:1.1.1") {
//        isForce = true;
//    }
}

afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("release") {
                groupId = group.toString()
                artifactId = "ToasterLibrary"
                version = version

                afterEvaluate {
                    from(components["release"])
//                    artifact(tasks.getByName("bundleReleaseAar"))
                }
//                artifact(tasks.getByName("bundleReleaseAar")) {
//                    builtBy(tasks.getByName("bundleReleaseAar"))
//                }
            }
        }
    }
}