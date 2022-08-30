/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

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
