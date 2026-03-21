/*
 * SPDX-License-Identifier: MIT-0
 *
 * This file is part of Sofia (https://github.com/jimbovm/sofia).
 */

package io.github.jimbovm.sofia.controller

import java.io.IOException
import java.util.Properties

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.text.Text
import javafx.stage.Stage

/**
 * Controller for the About Sofia pane.
 *
 * Loads dynamic build data from system resources into the view.
 */
class AboutController {

	/** Version text box in the About Sofia dialog. */
	@FXML
	var aboutSofiaVersion: Text? = null

	/** Build info text box for the About Sofia dialog. */
	@FXML
	var aboutBuildInfo: Text? = null

	/** Close button for dialog. */
	@FXML
	var aboutCloseButton: Button? = null

	/**
	 * Execute configuration for the About Sofia pane.
	 */
	fun initialize() {
		val properties = Properties()

		try {
			properties.load(ClassLoader.getSystemResourceAsStream("build_info.properties"))
			aboutSofiaVersion?.text = properties.getProperty("sofiaVersion")

			val gradleVersion: String = properties.getProperty("gradleVersion")
			val buildJvmVersion: String = properties.getProperty("jvmVersion")
			val buildDateTime: String = properties.getProperty("buildDateTime")
			aboutBuildInfo?.text = "$gradleVersion, JVM $buildJvmVersion $buildDateTime UTC"
		} catch (e: IOException) {
			e.printStackTrace()
		}
	}

	/**
	 * Respond to the close button being clicked by closing the parent window.
	 *
	 * param[event] An ActionEvent originating from the About dialog's close button.
	 */
	fun close(event: ActionEvent) {
		val parentWindow: Stage = aboutCloseButton?.scene?.window as Stage
		parentWindow.close()
	}
}
