plugins {
    id("su.nightexpress.project-conventions")
    alias(libs.plugins.indra)
}

description = "NMS"

dependencies {
    compileOnlyApi(libs.mewcore)
    compileOnly(libs.server.paper)
}

indra {
    javaVersions().target(17)
}