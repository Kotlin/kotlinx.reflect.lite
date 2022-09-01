/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

rootProject.name = "kotlinx.reflect.lite"

pluginManagement {
    val kotlin_version: String by settings
    val binary_compatibility_validator_version: String by settings
    plugins {
        id("org.jetbrains.kotlin.jvm") version kotlin_version
        id("org.jetbrains.kotlinx.binary-compatibility-validator") version binary_compatibility_validator_version
    }
}
