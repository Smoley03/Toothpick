import org.cadixdev.gradle.licenser.LicenseExtension

plugins {
  `maven-publish`
  `kotlin-dsl`
  kotlin("plugin.serialization") version "1.4.20"
  id("net.kyori.indra.license-header") version "1.3.1"
}

group = "xyz.jpenilla"
version = "1.0.0-SNAPSHOT"

repositories {
  mavenCentral()
  jcenter()
  maven("https://plugins.gradle.org/m2/")
}

dependencies {
  implementation("org.jetbrains.kotlinx", "kotlinx-serialization-json", "1.0.1")
  implementation("org.jetbrains.kotlinx", "kotlinx.dom", "0.0.10")
  implementation("com.github.jengelman.gradle.plugins", "shadow", "6.1.0")
}

java {
  sourceCompatibility = JavaVersion.VERSION_1_8
  targetCompatibility = JavaVersion.VERSION_1_8
  withSourcesJar()
}

kotlin {
  explicitApi()
}

tasks {
  compileKotlin {
    kotlinOptions.apiVersion = "1.4"
    kotlinOptions.jvmTarget = "1.8"
  }
}

gradlePlugin {
  plugins {
    create("Toothpick") {
      id = "xyz.jpenilla.toothpick"
      implementationClass = "xyz.jpenilla.toothpick.Toothpick"
    }
  }
}

extensions.getByType<PublishingExtension>().publications.withType<MavenPublication>().configureEach {
  pom {
    name.set(project.name)
    description.set(project.description)
    url.set("https://github.com/jpenilla/Toothpick")

    developers {
      developer {
        id.set("jmp")
        timezone.set("America/Los Angeles")
      }
    }

    licenses {
      license {
        name.set("MIT")
        url.set("https://github.com/jpenilla/Toothpick/raw/master/license.txt")
        distribution.set("repo")
      }
    }
  }
}

extensions.configure<LicenseExtension> {
  exclude("**/ModifiedLog4j2PluginsCacheFileTransformer.kt")
}

publishing.repositories.maven {
  url = if (project.version.toString().endsWith("-SNAPSHOT")) {
    uri("https://repo.jpenilla.xyz/snapshots")
  } else {
    uri("https://repo.jpenilla.xyz/releases")
  }
  credentials(PasswordCredentials::class)
}
