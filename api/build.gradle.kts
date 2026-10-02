plugins {
    id("com.vanniktech.maven.publish")
}

base {
    archivesName = rootProject.name + "API"
}

mavenPublishing {
    publishToMavenCentral(automaticRelease = true)
    signAllPublications()

    coordinates(group.toString(), "liriope-api", version.toString())

    pom {
        name = "Liriope"
        description = "API for Liriope."
        url = "https://github.com/Hana-Development/Liriope"

        licenses {
            license {
                name = "MIT License"
                url = "https://opensource.org/licenses/MIT"
            }
        }
        developers {
            developer {
                id = "Lotiny"
                name = "Lotiny"
                url = "https://github.com/Lotiny"
            }
        }
        scm {
            url = "https://github.com/Hana-Development/Liriope"
            connection = "scm:git:https://github.com/Hana-Development/Liriope.git"
            developerConnection = "scm:git:ssh://git@github.com/Hana-Development/Liriope.git"
        }
    }
}

if (!providers.gradleProperty("signingInMemoryKey").isPresent) {
    extensions.configure<SigningExtension> {
        useGpgCmd()
    }
}
