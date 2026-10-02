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
