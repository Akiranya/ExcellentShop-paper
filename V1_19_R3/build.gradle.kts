plugins {
    id("su.nightexpress.excellentshop.java-conventions")
    id("io.papermc.paperweight.userdev")
}

dependencies {
    compileOnly(project(":NMS"))
    paperweight.paperDevBundle("1.19.4-R0.1-SNAPSHOT")
}

description = "V1_19_R2"

tasks {
    assemble {
        dependsOn(reobfJar)
    }
}