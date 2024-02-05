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
    }
}

rootProject.name = "Spark Music Player"
include(":app")
include(":core:player")
include(":core:content_reader")
include(":core:model")
include(":feature:onboarding")
include(":core:design_system")
include(":core:network")
include(":core:datastore")
