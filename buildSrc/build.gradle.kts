plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven(uri("${System.getenv("HOME")}/MewcraftRepository"))
}

dependencies {
    implementation("cc.mewcraft.repo-conventions", "cc.mewcraft.repo-conventions.gradle.plugin", "1.0.0")
    implementation("cc.mewcraft.java-conventions", "cc.mewcraft.java-conventions.gradle.plugin", "1.0.0")
    implementation("cc.mewcraft.publishing-conventions", "cc.mewcraft.publishing-conventions.gradle.plugin", "1.0.0")
    implementation("cc.mewcraft.deploy-conventions", "cc.mewcraft.deploy-conventions.gradle.plugin", "1.0.0")
    implementation("cc.mewcraft.paper-plugins", "cc.mewcraft.paper-plugins.gradle.plugin", "1.0.0")
}
