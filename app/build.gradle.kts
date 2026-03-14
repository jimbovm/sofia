import java.time.LocalDateTime
import java.time.ZoneOffset

group = "io.github.jimbovm"

plugins {
	application
	id("org.jetbrains.kotlin.jvm") version "2.3.10"
	id("org.openjfx.javafxplugin") version "0.1.0"
	id("com.palantir.git-version") version "4.3.0"
	id("com.gradleup.shadow") version "9.3.0"
}

val gitVersion: groovy.lang.Closure<String> by extra
version = gitVersion()

tasks.register("outputBuildInfo")

allprojects {
	repositories {
		mavenLocal()
		mavenCentral()
	}
}

application {
	mainClass = "io.github.jimbovm.sofia.SofiaKt"
}

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(21))
	}
}

tasks.withType<Jar> {
	archiveBaseName = rootProject.name
	manifest {
		attributes["Main-Class"] = "io.github.jimbovm.sofia.SofiaKt"
	}
}

javafx {
	version = "21.+"
	modules = mutableListOf("javafx.controls", "javafx.base", "javafx.graphics", "javafx.fxml", "javafx.web")
}

kotlin {
	sourceSets {
		dependencies {
			implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

			implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
			implementation("org.jetbrains.kotlinx:kotlinx-datetime:0+")

			testImplementation("org.jetbrains.kotlin:kotlin-test")
			testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
		}
	}
}

dependencies {
	implementation("org.openjfx:javafx:21.+")
	implementation("io.github.jimbovm:isobel:0.+")
	implementation("org.apache.logging.log4j:log4j-api:2.+")
	implementation("org.apache.logging.log4j:log4j-core:2.+")
}

tasks.named("outputBuildInfo") {
	group = "build"
	description = "Generate build information properties file."
	outputs.file(layout.buildDirectory.file("resources/main/build_info.properties"))

	doFirst {
		val sofiaVersion = version
		val jvmVersion = org.gradle.internal.jvm.Jvm.current().toString()
		val gradleVersion = GradleVersion.current().toString()
		val buildDateTime = LocalDateTime.now(ZoneOffset.UTC).toString()

		val propertiesText = """|
			|sofiaVersion=$sofiaVersion
			|gradleVersion=$gradleVersion
			|jvmVersion=$jvmVersion
			|buildDateTime=$buildDateTime
		|""".trimMargin()

		if (outputs.files.singleFile.exists()) {
			outputs.files.singleFile.delete()
		}

		outputs.files.singleFile.writeText(propertiesText)
	}
	finalizedBy("jar")
}

tasks.named("run") {
	dependsOn("outputBuildInfo")
}

tasks.named("build") {
	dependsOn("outputBuildInfo")
}

tasks.named("shadowJar") {
	dependsOn("outputBuildInfo")
}