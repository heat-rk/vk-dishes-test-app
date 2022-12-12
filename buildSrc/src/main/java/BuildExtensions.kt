import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.kapt(dependencies: Array<String>) {
    dependencies.forEach { dependency ->
        add("kapt", dependency)
    }
}

fun DependencyHandler.implementation(dependencies: Array<String>) {
    dependencies.forEach { dependency ->
        add("implementation", dependency)
    }
}

fun DependencyHandler.androidTestImplementation(dependencies: Array<String>) {
    dependencies.forEach { dependency ->
        add("androidTestImplementation", dependency)
    }
}

fun DependencyHandler.testImplementation(dependencies: Array<String>) {
    dependencies.forEach { dependency ->
        add("testImplementation", dependency)
    }
}