pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

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
    }
}

rootProject.name = "ToasterUtil"
include(":app")
include(":ToasterLibrary")
