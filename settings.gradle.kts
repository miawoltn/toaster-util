pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://dl.bintray.com/guardian/android")
        maven("https://jitpack.io")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven( "https://identy.jfrog.io/identy/identy-face-release") {
            credentials {
                username = "barnksfortegroup"
                password = "cHv16Vt%8fYG8j^4*xcT3"
            }
        }
        maven( "https://identy.jfrog.io/identy/identy-finger-release") {
            credentials {
                username = "barnksfortegroup"
                password = "cHv16Vt%8fYG8j^4*xcT3"
            }
        }
        maven( "https://identy.jfrog.io/identy/identy-ocr-release") {
            credentials {
                username = "barnksfortegroup"
                password = "cHv16Vt%8fYG8j^4*xcT3"
            }
        }
        maven("https://dl.bintray.com/guardian/android")
        maven ("https://jitpack.io")
    }
}

rootProject.name = "ToasterUtil"
include(":app")
include(":ToasterLibrary")
