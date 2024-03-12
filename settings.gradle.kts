pluginManagement {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenLocal()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}

rootProject.name = "Task1"
include(":app")
