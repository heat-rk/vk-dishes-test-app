// Top-level build file where you can add configuration options common to all sub-projects/modules.
import org.apache.tools.ant.taskdefs.condition.Os

buildscript {
    dependencies {
        classpath(AppDependencies.Hilt.gradlePlugin)
    }
}

plugins {
    id(AppPlugins.androidApplication) version AppPlugins.Versions.androidApplication apply false
    id(AppPlugins.androidLibrary) version AppPlugins.Versions.androidLibrary apply false
    id(AppPlugins.androidKotlin) version AppPlugins.Versions.androidKotlin apply false
}

subprojects {
    tasks.register<Copy>("installGitHook") {
        var suffix = "unix"

        if (Os.isFamily(Os.FAMILY_WINDOWS)) {
            suffix = "windows"
        }

        from(File(rootProject.rootDir, "scripts/pre-commit-$suffix"))
        into(File(rootProject.rootDir, ".git/hooks"))
        rename("pre-commit-$suffix", "pre-commit")

        fileMode = 0b111101101
    }

    tasks.whenTaskAdded {
        if (name == "preBuild") {
            dependsOn("installGitHook")
        }
    }
}