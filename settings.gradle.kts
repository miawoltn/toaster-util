pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://dl.bintray.com/guardian/android")
        maven("https://jitpack.io")
        maven( "https://plugins.gradle.org/m2/")
    }
}

//buildscript {
//    repositories {
//        mavenCentral()
//        google()
//        maven( "https://plugins.gradle.org/m2/")
//    }
//    dependencies {
////        classpath("com.android.tools.build:gradle:7.1.0")
////        classpath("com.github.kezong:fat-aar:1.3.8")
////        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin")
//    }
//}

dependencyResolutionManagement {
//    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://a146-169-150-218-73.ngrok-free.app/nexus/content/repositories/verxid-face/")
        }
        maven {
            url = uri("https://a146-169-150-218-73.ngrok-free.app/nexus/content/repositories/verxid-finger/")
        }
        maven {
            url = uri("https://a146-169-150-218-73.ngrok-free.app/nexus/content/repositories/verxid-ocr/")
        }
        maven("https://dl.bintray.com/guardian/android")
        maven ("https://jitpack.io")
        maven( "https://plugins.gradle.org/m2/")
    }
}

rootProject.name = "ToasterUtil"
include(":app")
include(":ToasterLibrary")
