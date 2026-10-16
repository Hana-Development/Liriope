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
