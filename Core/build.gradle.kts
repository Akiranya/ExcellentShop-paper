plugins {
    id("su.nightexpress.excellentshop.java-conventions")
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("net.kyori.indra.git") version "2.1.1"
}

dependencies {
    // NMS modules
    implementation(project(":NMS"))
    implementation(project(":V1_18_R2", configuration = "reobf"))
    implementation(project(":V1_19_R3", configuration = "reobf"))

    // The server API
    compileOnly("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")

    // 3rd party plugins
    compileOnly("su.nightexpress.gamepoints:GamePoints:1.3.4")
    compileOnly("me.xanium.gemseconomy:GemsEconomy:1.3.5")
    compileOnly("com.github.TechFortress:GriefPrevention:16.17.1")
    compileOnly("com.github.angeschossen:LandsAPI:6.20.0")
    compileOnly("org.black_ixx:playerpoints:3.0.0")
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.6") {
        exclude("org.bukkit")
    }
    compileOnly("net.citizensnpcs:citizensapi:2.0.29-SNAPSHOT") {
        exclude("ch.ethz.globis.phtree")
    }
}

description = "Core"
version = "$version".decorateVersion()

fun lastCommitHash(): String = indraGit.commit()?.name?.substring(0, 7) ?: error("Could not determine commit hash")
fun String.decorateVersion(): String = if (endsWith("-SNAPSHOT")) "$this-${lastCommitHash()}" else this

tasks {
    build {
        dependsOn(shadowJar)
    }
    jar {
        archiveClassifier.set("noshade")
    }
    shadowJar {
        minimize {
            exclude(dependency("su.nightexpress.excellentshop:.*:.*"))
        }
        archiveFileName.set("ExcellentShop-${project.version}.jar")
        archiveClassifier.set("")
        destinationDirectory.set(file("$rootDir"))
    }
    processResources {
        filesMatching("**/paper-plugin.yml") {
            expand(mapOf(
                "version" to "${project.version}",
                "description" to project.description
            ))
        }
    }
    register("deployJar") {
        doLast {
            exec {
                commandLine("rsync", shadowJar.get().archiveFile.get().asFile.absoluteFile, "dev:data/dev/jar")
            }
        }
    }
    register("deployJarFresh") {
        dependsOn(build)
        finalizedBy(named("deployJar"))
    }
}

java {
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
