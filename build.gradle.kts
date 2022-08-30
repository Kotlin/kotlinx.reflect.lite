plugins {
    kotlin("jvm") version "1.7.10"
    `java-gradle-plugin`
    `maven-publish`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    mavenLocal()
}

dependencies {
    implementation("com.google.protobuf:protobuf-java:2.6.1")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    implementation("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.4.2")
    testImplementation("junit:junit:4.13")
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

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}
