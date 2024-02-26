pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
    }
}

rootProject.name = "Spark Music Player"
include(":app")
include(":core:content_reader")
include(":core:model")
include(":feature:onboarding")
include(":core:design_system")
include(":core:network")
include(":core:datastore")
include(":core:datastore_proto")
include(":feature:music_player")
include(":core:database")
include(":core:data")
include(":core:player")
include(":core:notification")
include(":core:service")
