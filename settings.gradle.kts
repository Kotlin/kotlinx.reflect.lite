rootProject.name = "kotlinx.reflect.lite"

pluginManagement {
    resolutionStrategy {
        val kotlin_version: String by settings
        eachPlugin {
            if (requested.id.namespace?.startsWith("org.jetbrains.kotlin") == true) {
                useVersion(kotlin_version)
            }
        }
    }
}