plugins {
    id("com.github.johnrengelman.shadow") version ("8.1.1")
}

tasks {
    processResources {
        filteringCharset = Charsets.UTF_8.name()
        val props = mapOf(
            "version" to project.version
        )
        inputs.properties(props)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}

dependencies {
    implementation("net.objecthunter:exp4j:0.4.8")
    implementation(project(":api"))
}