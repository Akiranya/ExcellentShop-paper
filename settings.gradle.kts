rootProject.name = "ExcellentShop"

include(":Core")
include(":NMS")
include(":V1_18_R2")
include(":V1_19_R3")
include(":V1_20_R1")

apply(from = "${System.getenv("HOME")}/MewcraftGradle/mirrors.settings.gradle.kts")