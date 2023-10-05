plugins {
    id("su.nightexpress.project-conventions")
    alias(libs.plugins.paperdev)
}

dependencies {
    compileOnly(project(":NMS"))
    paperweight.paperDevBundle("1.19.4-R0.1-SNAPSHOT")
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }
}
