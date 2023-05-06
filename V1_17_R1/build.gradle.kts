plugins {
    id("su.nightexpress.excellentshop.java-conventions")
    id("io.papermc.paperweight.userdev")
}

dependencies {
    compileOnly(project(":NMS"))
    paperweight.paperDevBundle("1.17.1-R0.1-SNAPSHOT")
}

description = "V1_17_R1"

tasks {
    assemble {
        dependsOn(reobfJar)
    }
}