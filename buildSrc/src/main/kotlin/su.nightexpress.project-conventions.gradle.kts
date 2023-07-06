plugins {
    id("cc.mewcraft.java-conventions")
    id("cc.mewcraft.repository-conventions")
}

group = "su.nightexpress.excellentshop"
version = "4.4.1"

repositories {
    maven("https://jitpack.io") {
        content {
            includeGroup("com.github.TechFortress")
            includeGroup("com.github.angeschossen")
            includeGroup("com.github.justisr")
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
    compileOnly("su.nexmedia:NexEngine:2.2.10")
}
