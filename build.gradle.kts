// Top-level build file where you can add configuration options common to all sub-projects/modules.

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