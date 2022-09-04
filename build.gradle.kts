/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    kotlin("jvm")
    `java-gradle-plugin`
    `maven-publish`
    signing
    id("org.jetbrains.kotlinx.binary-compatibility-validator")
    id("me.champeau.jmh") version "0.6.7"
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    mavenLocal()
}

dependencies {
    implementation("com.google.protobuf:protobuf-java:2.6.1")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")

    val kotlinx_metadata_version: String by project
    implementation("org.jetbrains.kotlinx:kotlinx-metadata-jvm:$kotlinx_metadata_version")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("junit:junit:4.13")
    jmh("org.jetbrains.kotlin:kotlin-reflect:1.7.10")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.compileKotlin {
    kotlinOptions {
        freeCompilerArgs += listOf("-no-reflect")
    }
}

kotlin {
    explicitApi()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            configureMavenCentralMetadata()
            mavenCentralArtifacts(project, project.sourceSets.main.get().allSource)
        }

        mavenRepositoryPublishing(project)
        configureMavenCentralMetadata()
    }

    publications.withType(MavenPublication::class).all {
        signPublicationIfKeyPresent(this)
    }
}
