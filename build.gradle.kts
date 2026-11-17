plugins {
    id("com.vanniktech.maven.publish") version "0.37.0" apply false
}

subprojects {
    pluginManager.apply("java-library")

    extensions.configure<JavaPluginExtension> {
        toolchain.languageVersion = JavaLanguageVersion.of(21)
    }

    dependencies {
        "compileOnly"("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
    }
}

val resourcePack by tasks.registering(Zip::class) {
    from("resourcepack")
    archiveFileName = "Liriope-ResourcePack.zip"
    destinationDirectory = layout.buildDirectory
}

tasks.register("build") {
    dependsOn(resourcePack)
}
