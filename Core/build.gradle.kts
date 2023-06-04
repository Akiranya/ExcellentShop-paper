plugins {
    id("su.nightexpress.project-conventions")
    id("cc.mewcraft.publishing-conventions")
    alias(libs.plugins.indra)
    alias(libs.plugins.shadow)
}

description = "Core"

dependencies {
    // nms modules
    implementation(project(":NMS"))
    implementation(project(":V1_18_R2", configuration = "reobf"))
    implementation(project(":V1_19_R3", configuration = "reobf"))

    // my own libs
    compileOnly(libs.mewcore)

    // server api
    compileOnly(libs.server.paper)

    // libs present as other plugins
    compileOnly(libs.gemseconomy)
    compileOnly(libs.worldguard) {
        exclude("org.bukkit")
    }
    compileOnly(libs.citizens) {
        exclude("ch.ethz.globis.phtree")
    }

    // libs present as other plugins (not on Mewcraft server)
    compileOnly("su.nightexpress.gamepoints", "GamePoints", "1.3.4") {
        exclude("org.spigotmc")
    }
    compileOnly("com.github.TechFortress", "GriefPrevention", "16.17.1")
    compileOnly("com.github.angeschossen", "LandsAPI", "6.20.0")
    compileOnly("org.black_ixx", "playerpoints", "3.0.0")
}

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
            expand(
                mapOf(
                    "version" to "${project.version}",
                    "description" to project.description
                )
            )
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

indra {
    javaVersions().target(17)
}