plugins {
    id("su.nightexpress.project-conventions")
    id("cc.mewcraft.publishing-conventions")
    id("cc.mewcraft.deploy-conventions")
    id("cc.mewcraft.paper-plugins")
}

project.ext.set("name", "ExcellentShop")

dependencies {
    // nms modules
    implementation(project(":NMS"))
    implementation(project(":V1_18_R2", configuration = "reobf"))
    implementation(project(":V1_19_R3", configuration = "reobf"))
    implementation(project(":V1_20_R1", configuration = "reobf"))

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
    compileOnly("com.github.TechFortress", "GriefPrevention", "16.17.1")
    compileOnly("com.github.angeschossen", "LandsAPI", "6.20.0")

    // adds support for custom items from various plugins
    compileOnly(libs.spatula.item)
}
