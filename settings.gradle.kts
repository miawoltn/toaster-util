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
        maven {
            url = uri("http://localhost:8081/nexus/content/repositories/verxid-face/")
            isAllowInsecureProtocol = true
        }
        maven {
            url = uri("http://localhost:8081/nexus/content/repositories/verxid-finger/")
            isAllowInsecureProtocol = true
        }
        maven {
            url = uri("http://localhost:8081/nexus/content/repositories/verxid-ocr/")
            isAllowInsecureProtocol = true
        }
        maven("https://dl.bintray.com/guardian/android")
        maven ("https://jitpack.io")
        maven( "https://plugins.gradle.org/m2/")
    }
}

rootProject.name = "ToasterUtil"
include(":app")
include(":ToasterLibrary")
