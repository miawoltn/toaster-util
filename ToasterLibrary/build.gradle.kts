import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.include

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.kezong.fat-aar")
    id("maven-publish")
}


group = "com.github.miawoltn"
version = "0.1.24"

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
    implementation("com.google.android.material:material:1.9.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("com.identy.app:finger:5.7.2@aar")
    implementation("com.identy.face:face:4.11.2@aar")
    implementation("com.identy.docscan:ocr:2.16.0@aar")
    implementation("com.identy.face.localmatcher:face_local_matcher: 4.11.0@aar")

//    implementation("com.github.kezong:fat-aar:1.3.8")
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