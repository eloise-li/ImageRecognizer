pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        flatDir {
            dirs("libs")
        }
        plugins {
            id("com.android.application") version "8.8.2"
            id("org.jetbrains.kotlin.android") version "2.0.21"
            id("org.jetbrains.kotlin.plugin.compose") version "2.0.21"
        }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        flatDir {
            dirs("libs")
        }
    }
}


rootProject.name = "ImageRecognizer"

include(":app")
include(":opencv")
project(":opencv").projectDir = File("OpenCV-android-sdk/sdk/java")
//include(":opencv")

//project(":opencv").projectDir = File("/Users/youguxuanhe/Desktop/OpenCV/OpenCV-android-sdk/sdk/java")

