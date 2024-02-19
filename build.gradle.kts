plugins {
    `java-library`
    kotlin("jvm") version "1.9.22"
    id("com.github.johnrengelman.shadow") version ("8.1.1")
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "kotlin")

    group = "kr.toxicity.inventory"
    version = "1.0"

    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
    }

    dependencies {
        testImplementation("org.jetbrains.kotlin:kotlin-test")

        compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    }

    tasks {
        compileJava {
            options.encoding = Charsets.UTF_8.name()
        }
        test {
            useJUnitPlatform()
        }
    }
}

dependencies {
    implementation(project(":dist", configuration = "shadow"))
}

tasks {
    jar {
        finalizedBy(shadowJar)
    }
    shadowJar {
        archiveClassifier.set("")
        fun prefix(pattern: String) {
            relocate(pattern, "${project.group}.shaded.$pattern")
        }
        prefix("kotlin")
        prefix("net.objecthunder.exp4j")
    }
}

val targetJavaVersion = 17

kotlin {
    jvmToolchain(targetJavaVersion)
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
}