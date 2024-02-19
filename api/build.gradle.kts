plugins {
    `maven-publish`
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                from(components["java"])
            }
        }
    }
}