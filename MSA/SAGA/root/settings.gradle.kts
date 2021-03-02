pluginManagement {
    repositories {
        mavenCentral()
        maven { url = uri("https://repo.spring.io/milestone") }
        maven { url = uri("https://repo.spring.io/snapshot") }
        gradlePluginPortal()
    }
}
rootProject.name = "ddd-study"
include("user-service")
include("order-service")
include("payment-service")
include("shop-service")
include("delivery-service")
include("common")
