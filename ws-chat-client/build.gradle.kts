/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    id("learninggradle.kotlin-library-conventions")
    // Support convention plugins written in Kotlin. Convention plugins are build scripts in 'src/main' that automatically become available as plugins in the main build.
    `kotlin-dsl`
}
dependencies {
    implementation(libs.kotlin.gradle.plugin)
    api(project(":ws-chat-server"))
}

//dependencies {
//    implementation(libs.kotlin.gradle.plugin)
//}
