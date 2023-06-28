plugins {
    id("su.nightexpress.project-conventions")
    alias(libs.plugins.paperweight.userdev)
}

dependencies {
    compileOnly(project(":NMS"))
    paperweight.paperDevBundle("1.18.2-R0.1-SNAPSHOT")
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }
}
