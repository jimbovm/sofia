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

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.Button
import javafx.scene.control.Menu
import javafx.scene.control.MenuItem
import javafx.scene.image.Image
import javafx.scene.layout.HBox
import javafx.scene.Scene
import javafx.stage.FileChooser
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.Window

/**
 * Controller for both the main menu and main toolbar.
 */
class MainMenuController {

	@FXML
	var menuFileNew: MenuItem? = null

	@FXML
	var menuFileOpen: MenuItem? = null

	@FXML
	val mainToolbarNew: Button? = null

	@FXML
	var mainToolbarOpen: Button? = null

	@FXML
	val mainToolbarSave: Button? = null

	@FXML
	val mainToolbarExportBytecode: Button? = null

	@FXML
	val mainToolbarCut: Button? = null

	@FXML
	val mainToolbarCopy: Button? = null

	@FXML
	val mainToolbarPaste: Button? = null

	@FXML
	val mainToolbarUndo: Button? = null

	@FXML
	val mainToolbarRedo: Button? = null

	@FXML
	val mainToolbarAddArea: Button? = null

	@FXML
	val mainToolbarMoveAreaDown: Button? = null

	@FXML
	val mainToolbarMoveAreaUp: Button? = null

	@FXML
	val mainToolbarDeleteArea: Button? = null

	@FXML
	val mainToolbarPreferences: Button? = null

	private val uiBundle: ResourceBundle? = ResourceBundle.getBundle("i18n/sofia_ui")
	private val aboutLoader = FXMLLoader(ClassLoader.getSystemResource("fxml/about.fxml"), uiBundle)

	internal val extensionDescription = uiBundle?.getString("extension_description")
	internal val fileExtensions =
		listOf(FileChooser.ExtensionFilter(extensionDescription, "fia", "sofia", "isobel"))

	fun initialize() {}

	@FXML
	fun createNewGame(event: ActionEvent) {
		System.err.println("New game action; not implemented yet")
	}

	@FXML
	fun showOpenDialog(event: ActionEvent) {

		val fileChooser = FileChooser()
		fileChooser.title = uiBundle?.getString("dialog_title_open")
		fileChooser.extensionFilters.addAll(
			this.fileExtensions
		)
		val selectedFile = fileChooser.showOpenDialog(this.mainToolbarOpen?.parent?.scene?.window)
		println(selectedFile ?: selectedFile.toString())
	}

	@FXML
	fun showSaveDialog(event: ActionEvent) {

		val fileChooser = FileChooser()
		fileChooser.extensionFilters.addAll(
			this.fileExtensions
		)
		fileChooser.title = uiBundle?.getString("dialog_title_save")
		val saveFileName = fileChooser.showSaveDialog(this.mainToolbarSave?.parent?.scene?.window)
		println(saveFileName ?: saveFileName.toString())
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