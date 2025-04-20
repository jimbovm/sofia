/* SPDX-License-Identifier: MIT-0

Ⓒ 2025 Jimbo Brierley.

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
of the Software, and to permit persons to whom the Software is furnished to do
so.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE. */

package com.github.jimbovm.sofia.fxml

import java.io.IOException
import java.util.Properties

import javafx.fxml.FXML
import javafx.event.ActionEvent
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
	@FXML var aboutSofiaVersion: Text? = null
	/** Build info text box for the About Sofia dialog. */
	@FXML var aboutBuildInfo: Text? = null
	/** Close button for dialog. */
	@FXML var aboutCloseButton: Button? = null

	/**
	 * Execute configuration for the About Sofia pane.
	 */
	fun initialize() {

		val properties: Properties = Properties()

		try {
			properties.load(ClassLoader.getSystemResourceAsStream("build_info.properties"))
			aboutSofiaVersion?.text = properties.getProperty("sofiaVersion")

			val gradleVersion: String = properties.getProperty("gradleVersion")
			val buildJvmVersion: String = properties.getProperty("jvmVersion")
			val buildDateTime: String = properties.getProperty("buildDateTime")
			aboutBuildInfo?.text = "$gradleVersion, JVM $buildJvmVersion $buildDateTime UTC"
		}

		catch (e: IOException) {

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