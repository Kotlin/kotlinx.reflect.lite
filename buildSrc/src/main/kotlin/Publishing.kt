/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

@file:Suppress("UnstableApiUsage")

import org.gradle.api.*
import org.gradle.api.file.*
import org.gradle.api.publish.*
import org.gradle.api.publish.maven.*
import org.gradle.jvm.tasks.*
import org.gradle.kotlin.dsl.*
import org.gradle.plugins.signing.*
import java.net.*

fun PublishingExtension.configureMavenCentralMetadata() {
    publications.withType(MavenPublication::class) {
        pom {
            if (!name.isPresent) {
                name.set(artifactId)
            }
            description.set("Experimental lightweight library that replaces existing 'kotlin-reflect' implementation")
            url.set("https://github.com/Kotlin/kotlinx.reflect.lite")
            licenses {
                license {
                    name.set("The Apache Software License, Version 2.0")
                    url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                    distribution.set("repo")
                }
            }
            developers {
                developer {
                    id.set("JetBrains")
                    name.set("JetBrains Team")
                    organization.set("JetBrains")
                    organizationUrl.set("https://www.jetbrains.com")
                }
            }
            scm {
                url.set("https://github.com/Kotlin/kotlinx.reflect.lite")
            }
        }
    }
}

fun MavenPublication.mavenCentralArtifacts(project: Project, sources: SourceDirectorySet) {
    val sourcesJar by project.tasks.creating(Jar::class) {
        archiveClassifier.set("sources")
        from(sources)
    }
    val javadocJar by project.tasks.creating(Jar::class) {
        archiveClassifier.set("javadoc")
        // contents are deliberately left empty
    }
    artifact(sourcesJar)
    artifact(javadocJar)
}

fun PublishingExtension.mavenRepositoryPublishing(project: Project) {
    repositories {
        maven {
            url = URI("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = project.getSensitiveProperty("libs.sonatype.user")
                password = project.getSensitiveProperty("libs.sonatype.password")
            }
        }
    }
}

fun Project.signPublicationIfKeyPresent(publication: MavenPublication) {
    val keyId = project.getSensitiveProperty("libs.sign.key.id")
    val signingKey = project.getSensitiveProperty("libs.sign.key.private")
    val signingKeyPassphrase = project.getSensitiveProperty("libs.sign.passphrase")
    if (!signingKey.isNullOrBlank()) {
        extensions.configure<SigningExtension>("signing") {
            useInMemoryPgpKeys(keyId, signingKey, signingKeyPassphrase)
            sign(publication)
        }
    }
}

fun Project.getSensitiveProperty(name: String): String? {
    return project.findProperty(name) as? String ?: System.getenv(name)
}
