plugins {
    id("org.hanayori.aster") version "1.0.0"
}

base {
    archivesName = rootProject.name
}

dependencies {
    implementation(project(":api"))
    compileOnly("org.hanayori:aster-command")
}

tasks {
    jar {
        dependsOn(configurations.runtimeClasspath)
        from(configurations.runtimeClasspath.map { it.map(::zipTree) })
    }

    processResources {
        val props = mapOf("version" to version)
        filesMatching("paper-plugin.yml") {
            expand(props)
        }
    }
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}

dependencies {
    testImplementation("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
    testImplementation("org.junit.jupiter:junit-jupiter:6.1.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:6.1.3")
}

tasks.test {
    useJUnitPlatform()
}
