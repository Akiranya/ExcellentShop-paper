plugins {
    id("su.nightexpress.excellentshop.java-conventions")
    id("io.papermc.paperweight.userdev")
}

dependencies {
    compileOnly(project(":NMS"))
    paperweight.paperDevBundle("1.18.2-R0.1-SNAPSHOT")
}

description = "V1_18_R2"

tasks {
    assemble {
        dependsOn(reobfJar)
    }
}