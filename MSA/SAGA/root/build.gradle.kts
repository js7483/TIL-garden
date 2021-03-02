
buildscript {
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-noarg:1.4.20")
        classpath("org.jetbrains.kotlin:kotlin-allopen:1.4.20")
    }
}

plugins {
    id("org.springframework.boot") version "2.4.1"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    kotlin("jvm") version "1.4.21"
    kotlin("plugin.spring") version "1.4.21"
}

allprojects {
    repositories {
        mavenCentral()
        jcenter()
    }
}

subprojects {

    apply(plugin = "kotlin")
    apply(plugin = "kotlin-kapt")
    apply(plugin = "kotlin-jpa")
    apply(plugin = "kotlin-allopen")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
//    apply(plugin = "com.palantir.docker")
//    apply(plugin = "com.palantir.docker-compose")

    group = "com.example"
    version = "0.0.1-SNAPSHOT"

    java.sourceCompatibility = JavaVersion.VERSION_11

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-web")
        developmentOnly("org.springframework.boot:spring-boot-devtools")
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation("com.h2database:h2")
        testImplementation("org.springframework.boot:spring-boot-starter-test") {
            exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        }
        implementation("io.github.microutils:kotlin-logging:2.0.4")

        implementation("io.eventuate.tram.sagas:eventuate-tram-sagas-spring-orchestration-simple-dsl:0.14.0.RELEASE")
        implementation("io.eventuate.tram.sagas:eventuate-tram-sagas-spring-participant:0.14.0.RELEASE")

        implementation("io.eventuate.tram.core:eventuate-tram-spring-messaging:0.26.1.RELEASE")
        implementation("io.eventuate.tram.core:eventuate-tram-aggregate-domain-events:0.26.1.RELEASE")
        implementation("io.eventuate.tram.core:eventuate-tram-spring-jdbc-kafka:0.26.1.RELEASE")
//        implementation("io.eventuate.tram.core:eventuate-tram-spring-commands:0.26.1.RELEASE")
//        implementation("io.eventuate.tram.core:eventuate-tram-spring-events:0.26.1.RELEASE")

    }

    tasks {
        compileKotlin {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
                jvmTarget = "11"
            }
        }

        compileTestKotlin {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
                jvmTarget = "11"
            }
        }

        test {
            useJUnitPlatform()
        }

        allOpen {
            annotation("javax.persistence.Entity")
            annotation("javax.persistence.Access")
            annotation("javax.persistence.MappedSuperclass")
            annotation("javax.persistence.Embeddable")
        }
    }

}

project("order-service") {
    dependencies {
        implementation(project(":common"))
    }
}

project("shop-service") {
    dependencies {
        implementation(project(":common"))
    }
}

project("payment-service") {
    dependencies {
        implementation(project(":common"))
    }
}

project("delivery-service") {
    dependencies {
        implementation(project(":common"))
    }
}

project("user-service") {
    dependencies {
        implementation(project(":common"))
    }
}

project("common") {
    tasks {
        bootJar {
            enabled = false
        }

        jar {
            enabled = true
        }
    }
}