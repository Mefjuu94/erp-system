import java.nio.file.Paths

plugins {
    kotlin("jvm")
    id("org.jetbrains.kotlin.plugin.compose")
    application
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(project(":shared"))

    implementation("org.jetbrains.compose.ui:ui:1.5.10")
    implementation("org.jetbrains.compose.material:material:1.5.10")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.7.3")
    implementation("org.jetbrains.compose.desktop:desktop:1.5.10")

    runtimeOnly("org.jetbrains.skiko:skiko-awt-runtime-windows-x64:0.7.85")

    implementation("io.ktor:ktor-client-core:2.3.4")
    implementation("io.ktor:ktor-client-cio:2.3.4")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.4")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
}

application {
    mainClass.set("com.erp.client.ClientKt")
}

kotlin {
    jvmToolchain(21)
}



tasks.register("runWithServer") {
    dependsOn(":server:shadowJar")

    doLast {
        println("🔧 Uruchamiam serwer...")

        val serverJar = Paths.get(
            rootDir.absolutePath,
            "server", "build", "libs", "server-1.0-SNAPSHOT-all.jar"
        ).toFile().absolutePath

        val process = ProcessBuilder("java", "-jar", serverJar)
            .redirectErrorStream(true)
            .start()

        Thread {
            process.inputStream.bufferedReader().forEachLine {
                println("[SERWER] $it")
            }
        }.start()

        println("🚀 Uruchamiam klienta...")

        val sourceSets = project.extensions.getByType<org.gradle.api.tasks.SourceSetContainer>()
        val classpath = sourceSets["main"].runtimeClasspath.asPath

        exec {
            commandLine("java", "-cp", classpath, "com.erp.client.ClientKt")
        }

        println("🛑 Zatrzymuję serwer...")
        process.destroy()
    }
}
