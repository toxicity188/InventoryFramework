dependencies {
    compileOnly(project(":api"))
}

tasks {
    jar {
        archiveFileName.set("InventoryFramework-ExamplePlugin.jar")
    }
}