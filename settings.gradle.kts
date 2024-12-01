pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)  // 변경된 부분
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "habitTracker"
include(":app")
