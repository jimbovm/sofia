import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.Files

group = "com.github.jimbovm"

plugins {
	// Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
	kotlin("jvm") version "2.0.0"

	// Apply the application plugin to add support for building a CLI application in Java.
	application
  	id("org.openjfx.javafxplugin") version "0.1.0"
}

repositories {
	// Use Maven Central for resolving dependencies.
	mavenCentral()
}

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(21))
	}
}

javafx {
    version = "21"
    modules = mutableListOf("javafx.controls", "javafx.base", "javafx.graphics", "javafx.fxml")
}

kotlin {
	sourceSets {
		dependencies {
			// Align versions of all Kotlin components
			implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

			// Use the Kotlin JDK 8 standard library.
			implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

			// Use the Kotlin test library.
			testImplementation("org.jetbrains.kotlin:kotlin-test")

			// Use the Kotlin JUnit integration.
			testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
		}
	}
}

dependencies {
		// https://mvnrepository.com/artifact/org.openjfx/javafx
		implementation("org.openjfx:javafx:21")
}

application {
	// Define the main class for the application.
	mainClass.set("com.github.jimbovm.sofia.SofiaKt")
}

tasks.register("setVersion") {

	doFirst {
		val command = "git describe --tags --long"

		val processBuilder: ProcessBuilder = ProcessBuilder(command.split(' '))
		processBuilder.redirectErrorStream(true)
		processBuilder.directory(rootProject.projectDir)

		val shell: Process = processBuilder.start()
		val shellReader: BufferedReader = BufferedReader(InputStreamReader(shell.getInputStream()))

		val gitVersionString = shellReader.readLine()

		println("$command gives: $gitVersionString")

		val versionElements: List<String> = gitVersionString.split("-")
		val versionProperty = if (versionElements[1] == "0") versionElements[0] else gitVersionString

		println("Using version string: " + versionProperty)

		for (outputFilename: String in arrayOf(
			rootProject.projectDir.getAbsolutePath() + "/app/gradle.properties",
			rootProject.projectDir.getAbsolutePath() + "/app/src/main/resources/version.properties"
			)) {

			val outputFile: Path = Paths.get(outputFilename)
			if (Files.exists(outputFile)) {
				Files.delete(outputFile)
			}
			
			val propertiesFile = Files.createFile(outputFile)
			val property = "version=$versionProperty"
			Files.write(propertiesFile, property.toByteArray())
		}
	}
	dependsOn(tasks.compileKotlin)
}