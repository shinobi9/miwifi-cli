plugins {
    kotlin("jvm") version "1.4.10"
    application
    id("com.github.johnrengelman.shadow") version "6.0.0"
}

group = "shinobi9"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.github.ajalt.clikt:clikt:3.0.1")
    implementation("io.github.rybalkinsd:kohttp-jackson:0.12.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.9") {
        exclude("org.jetbrains.kotlin", "kotlin-reflect")
    }
    implementation("com.jakewharton.picnic:picnic:0.5.0")
}

application {
    mainClassName = "shinobi9.miwifi.CliKt"
}

tasks.withType<Jar> {
    manifest {
        attributes(
            mapOf(
                "Main-Class" to application.mainClassName
            )
        )
    }
}

tasks.withType<CreateStartScripts> {
    applicationName = "miwifi"
}

// tasks.withType<InstallD

// tasks.withType<Install>(){
//
// }
// distributions {
//    main {
//        distributionBaseName.set("miwifi")
//
//    }
// }

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}
