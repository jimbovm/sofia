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

import java.util.ResourceBundle

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.event.ActionEvent
import javafx.scene.Scene
import javafx.scene.control.Menu
import javafx.scene.control.MenuItem
import javafx.scene.image.Image
import javafx.scene.layout.HBox
import javafx.stage.FileChooser
import javafx.stage.Modality
import javafx.stage.Stage

class MainMenuController {

	@FXML
	var menuFileNew: MenuItem? = null

	@FXML
	var menuFileOpen: MenuItem? = null

	private val uiBundle: ResourceBundle? = ResourceBundle.getBundle("i18n/sofia_ui")
	private val aboutLoader = FXMLLoader(ClassLoader.getSystemResource("fxml/about.fxml"), uiBundle)

	fun initialize() {}

	@FXML
	fun showOpenDialog(event: ActionEvent) {

		val fileChooser = FileChooser()
		val selectedFile = fileChooser.showOpenDialog(this.menuFileOpen?.parentPopup?.scene?.window as Stage)
		println(selectedFile.toString())
	}

	/**
	 * Spawn and show the "About Sofia" dialog.
	 *
	 * param[event] A click event received from the associated menu item.
	 */
	fun showAboutDialog(event: ActionEvent) {

		val aboutPane: HBox = aboutLoader.load()

		val aboutStage = Stage().apply {
			title = uiBundle?.getString("about_title")
			scene = Scene(aboutPane)
			this.isResizable = false
			this.initModality(Modality.APPLICATION_MODAL)
			this.icons?.add(Image("img/icon_128x128.png"))
			this.icons?.add(Image("img/icon_64x64.png"))
			this.icons?.add(Image("img/icon_32x32.png"))
			this.icons?.add(Image("img/icon_16x16.png"))
		}.show()
	}
}