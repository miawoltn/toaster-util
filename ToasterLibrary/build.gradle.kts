
plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
}

group = "com.github.miawoltn"
version = "0.1.15"

android {
    namespace = "com.miawoltn.toasterlibrary"
    compileSdk = 33

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation(project(mapOf("path" to ":Verxid")))
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


    implementation(group = "com.identy.app", name = "finger", version = "5.7.2", ext = "aar")
    implementation(group = "com.identy.face", name=  "face", version = "4.11.2", ext = "aar")
    implementation(group = "com.identy.docscan", name = "ocr", version = "2.16.0", ext = "aar")
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
                }
            }
        }
    }
}