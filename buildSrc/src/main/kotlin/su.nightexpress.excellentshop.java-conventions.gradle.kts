plugins {
    `java-library`
    `maven-publish`
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        content {
            includeGroup("io.papermc.paper")
            includeGroup("net.md-5")
        }
    }
    maven("https://jitpack.io") {
        content {
            includeGroup("com.github.TechFortress")
            includeGroup("com.github.angeschossen")
            includeGroup("com.github.Ghost-chu")
            includeGroup("com.github.justisr")
        }
    }
    maven("https://maven.enginehub.org/repo/") {
        content {
            includeGroup("com.sk89q.worldguard")
            includeGroup("com.sk89q.worldguard.worldguard-libs")
            includeGroup("com.sk89q.worldedit")
            includeGroup("com.sk89q.worldedit.worldedit-libs")
        }
    }
    maven("https://repo.citizensnpcs.co/#/") {
        content {
            includeGroup("net.citizensnpcs")
        }
    }
    maven("https://repo.rosewooddev.io/repository/public/") {
        content {
            includeGroup("org.black_ixx")
        }
    }
    maven("https://repo.rosewooddev.io/repository/public/") {
        content {
            includeGroup("com.plotsquared")
        }
    }
}

dependencies {
    compileOnly(("cc.mewcraft:MewCore:5.13.1"))
    compileOnly("su.nexmedia:NexEngine:2.2.10")
    implementation("org.jetbrains:annotations:23.1.0")
}

group = "su.nightexpress.excellentshop"
version = "4.3.12"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }
}
