plugins {
    id("su.nightexpress.project-conventions")
    alias(libs.plugins.paperweight.userdev)
    alias(libs.plugins.indra)
}

description = "V1_18_R2"

dependencies {
    compileOnly(project(":NMS"))
    paperweight.paperDevBundle("1.18.2-R0.1-SNAPSHOT")
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }
}

indra {
    javaVersions().target(17)
}