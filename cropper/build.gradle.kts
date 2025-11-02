plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.parcelize")
    id("org.jetbrains.dokka")
    id("com.vanniktech.maven.publish")
    id("app.cash.licensee")
    id("app.cash.paparazzi")
}

licensee {
    allow("Apache-2.0")
}

// 🟢 Kotlin JVM Toolchain - Java 17
kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

// 🟢 Android settings
android {
    namespace = "com.canhub.cropper"
    compileSdk = 36

    defaultConfig {
        minSdk = 23
        targetSdk = 35
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}

// 🟢 Dependencies (toml references)
dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.exifinterface)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.androidx.fragment.testing)
    testImplementation(libs.androidx.test.junit)
    testImplementation(libs.junit)
    testImplementation(libs.mock)
    testImplementation(libs.robolectric)
}

// 🟢 Paparazzi Guava workaround
plugins.withId("app.cash.paparazzi") {
    afterEvaluate {
        dependencies.constraints {
            add("testImplementation", "com.google.guava:guava") {
                attributes {
                    attribute(
                        TargetJvmEnvironment.TARGET_JVM_ENVIRONMENT_ATTRIBUTE,
                        objects.named(TargetJvmEnvironment::class.java, TargetJvmEnvironment.STANDARD_JVM),
                    )
                }
                because(
                    "LayoutLib and sdk-common depend on Guava's -jre published variant. See https://github.com/cashapp/paparazzi/issues/906."
                )
            }
        }
    }
}
