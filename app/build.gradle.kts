plugins {
    id(AppPlugins.androidApplication)
    id(AppPlugins.androidKotlin)
    id(AppPlugins.kotlinKapt)
    id(AppPlugins.hilt)
    id(AppPlugins.detekt) version AppPlugins.Versions.detekt
}

android {
    namespace = AppConfig.applicationId
    compileSdk = AppConfig.Sdk.compile

    defaultConfig {
        applicationId = AppConfig.applicationId
        minSdk = AppConfig.Sdk.min
        targetSdk = AppConfig.Sdk.target
        versionCode = AppConfig.Version.code
        versionName = AppConfig.Version.name

        testInstrumentationRunner = AppConfig.testInstrumentationRunner
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

    detekt {
        buildUponDefaultConfig = true
        allRules = false
        autoCorrect = true
        config = files("${rootProject.rootDir}/detekt-config.yml")
    }

    tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        jvmTarget = "1.8"
    }

    tasks.withType<io.gitlab.arturbosch.detekt.DetektCreateBaselineTask>().configureEach {
        jvmTarget = "1.8"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(AppDependencies.Ktx.allImplementations)
    implementation(AppDependencies.appCompat)
    implementation(AppDependencies.material)
    implementation(AppDependencies.Glide.core)
    implementation(AppDependencies.Coroutines.allImplementations)
    implementation(AppDependencies.Hilt.core)

    compileOnly(AppDependencies.Glide.compiler)

    kapt(AppDependencies.Hilt.compiler)
}