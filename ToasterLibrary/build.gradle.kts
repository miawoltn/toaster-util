
plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.kezong.fat-aar")
    id("maven-publish")
}


group = "com.github.miawoltn"
version = "0.1.20"

//buildscript {
//    repositories {
//        mavenCentral()
//    }
//    dependencies {
//        classpath("com.github.kezong:fat-aar:1.3.8")
//    }
//}


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
            packagingOptions {
                jniLibs.pickFirsts.add("**/*.so")
            }
        }

        packagingOptions {
            jniLibs.pickFirsts.add("**/*.so")
        }
    }

    packagingOptions {
        jniLibs.pickFirsts.add("**/*.so")
    }

//    packagingOptions {
//        resources {
//            excludes += "lib/x86/*.so"
//            excludes += "lib/x86_64/*.so"
//            excludes += "lib/armeabi/*.so"
//            excludes += "lib/arm64-v8a/*.so"
//        }
//    }
}

//fataar {
//    /**
//     * If transitive is true, local jar module and remote library's dependencies will be embed.
//     * If transitive is false, just embed first level dependency
//     * Local aar project does not support transitive, always embed first level
//     * Default value is false
//     * @since 1.3.0
//     */
//    transitive = true
//}

//allprojects {
//    repositories {
//        google()
//        mavenCentral()
//        maven( "https://identy.jfrog.io/identy/identy-face-release") {
//            credentials {
//                username = "barnksfortegroup"
//                password = "cHv16Vt%8fYG8j^4*xcT3"
//            }
//        }
//        maven( "https://identy.jfrog.io/identy/identy-finger-release") {
//            credentials {
//                username = "barnksfortegroup"
//                password = "cHv16Vt%8fYG8j^4*xcT3"
//            }
//        }
//        maven( "https://identy.jfrog.io/identy/identy-ocr-release") {
//            credentials {
//                username = "barnksfortegroup"
//                password = "cHv16Vt%8fYG8j^4*xcT3"
//            }
//        }
//        maven("https://dl.bintray.com/guardian/android")
//        maven ("https://jitpack.io")
//        maven ("https://plugins.gradle.org/m2/")
////        maven {
////            setUrl("https://jitpack.io")
////            content {
////                includeGroup("com.github.aasitnikov")
////            }
////        }
//    }
//}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


//    embed(group = "com.identy.app", name = "finger", version = "5.7.2", ext = "aar") {
//        exclude("com.identy.app", "**/*.so")
//    }
//    embed(group = "com.identy.face", name=  "face", version = "4.11.2", ext = "aar")
//    embed(group = "com.identy.docscan", name = "ocr", version = "2.16.0", ext = "aar") {
//        exclude("com.identy.docscan", "**/*.so")
//    }

    implementation("com.identy.app:finger:5.7.2@aar"){
        isTransitive = true // Ensure transitive dependencies are included
    }
    implementation("com.identy.face:face:4.11.2@aar"){
        isTransitive = true // Ensure transitive dependencies are included
    }
    implementation("com.identy.docscan:ocr:2.16.0@aar"){
        isTransitive = true // Ensure transitive dependencies are included
    }

//    implementation("com.github.kezong:fat-aar:1.3.8")
}

afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("release") {
                groupId = group.toString()
                artifactId = "ToasterLibrary"
                version = version

//                afterEvaluate {
//                    from(components["release"])
//                    artifact(tasks.getByName("bundleReleaseAar"))
//                }
                artifact(tasks.getByName("bundleReleaseAar")) {
                    builtBy(tasks.getByName("bundleReleaseAar"))
                }
            }
        }
    }
}