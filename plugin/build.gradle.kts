base {
    archivesName = rootProject.name
}

dependencies {
    implementation(project(":api"))
    implementation("io.github.revxrsal:lamp.common:4.0.0-rc.18")
    implementation("io.github.revxrsal:lamp.bukkit:4.0.0-rc.18")
    implementation("io.github.revxrsal:lamp.brigadier:4.0.0-rc.18")
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
