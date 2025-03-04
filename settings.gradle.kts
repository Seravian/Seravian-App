pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}

rootProject.name = "Seravian"
include(":app")
include(":network:core-network")
include(":network:feat-network")
include(":navigation:core-navigation")
include(":navigation:feat-navigation")
include(":auth:core-auth")
include(":auth:feat-auth")
include(":home:core-home")
include(":home:feat-home")
include(":onboarding:core-onboarding")
include(":onboarding:feat-onboarding")
include(":validation")
include(":core-ui")