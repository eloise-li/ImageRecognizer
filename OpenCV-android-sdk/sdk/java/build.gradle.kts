plugins {
    id("com.android.library")
}

android {
    namespace = "org.opencv"
    compileSdk = 35
    sourceSets["main"].jniLibs.srcDirs("sdk/navite/libs")
    defaultConfig {
        minSdk = 26
        targetSdk = 35

        // 添加 NDK 配置
        ndk {
            abiFilters.addAll(setOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64"))
        }
    }

    // 修复源集配置
    sourceSets {
        getByName("main") {
            java.srcDirs("src")
            res.srcDirs("res")
            jniLibs.srcDirs("libs")  // 添加 JNI 库目录
            manifest.srcFile("AndroidManifest.xml")
        }
    }

    // 添加打包选项
    packagingOptions {
        jniLibs {
            pickFirsts.add("**/libopencv_*.so")
        }
    }
}
dependencies {
    implementation(libs.androidx.core)
    implementation(libs.androidx.core)
    implementation(libs.androidx.core)
}

