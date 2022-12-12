object AppDependencies {
    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val junit = "junit:junit:${Versions.junit}"
    const val junitExtensions = "androidx.test.ext:junit:${Versions.junitExtensions}"
    const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espresso}"

    object Glide {
        const val core = "com.github.bumptech.glide:glide:${Versions.glide}"
        const val compiler = "com.github.bumptech.glide:compiler:${Versions.glide}"
    }

    object Coroutines {
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
        const val testing = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}"
        const val turbine = "app.cash.turbine:turbine:${Versions.turbine}"

        val allImplementations = arrayOf(core, android)
        val allTestImplementations = arrayOf(testing, turbine)
    }

    object Hilt {
        const val core = "com.google.dagger:hilt-android:${Versions.hilt}"
        const val compiler = "com.google.dagger:hilt-android-compiler:${Versions.hilt}"
        const val gradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}"
    }

    object Ktx {
        const val core = "androidx.core:core-ktx:${Versions.coreKtx}"
        const val fragment = "androidx.navigation:navigation-fragment-ktx:${Versions.fragmentKtx}"

        val allImplementations = arrayOf(core, fragment)
    }

    object Versions {
        const val coreKtx = "1.9.0"
        const val fragmentKtx = "2.5.1"
        const val appCompat = "1.5.1"
        const val material = "1.7.0"
        const val junit = "4.13.2"
        const val junitExtensions = "1.1.4"
        const val espresso = "3.5.0"
        const val glide = "4.13.2"
        const val coroutines = "1.6.4"
        const val turbine = "0.9.0"
        const val hilt = "2.42"
    }
}