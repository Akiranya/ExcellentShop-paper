plugins {
    id("su.nightexpress.project-conventions")
    alias(libs.plugins.paperweight.userdev)
}

dependencies {
    compileOnly(project(":NMS"))
    paperweight.paperDevBundle("1.20.1-R0.1-SNAPSHOT")
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }
}
