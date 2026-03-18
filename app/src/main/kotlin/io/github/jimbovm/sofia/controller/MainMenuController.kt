/*
 * SPDX-License-Identifier: MIT-0
 *
 * This file is part of Sofia (https://github.com/jimbovm/sofia).
 */

package io.github.jimbovm.sofia.controller

import io.github.jimbovm.sofia.io.GameIO
import io.github.jimbovm.sofia.viewmodel.GameViewModel
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Menu
import javafx.scene.control.MenuItem
import javafx.scene.image.Image
import javafx.scene.layout.HBox
import javafx.stage.FileChooser
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.Window
import java.util.ResourceBundle
import java.io.File

/**
 * Controller for both the main menu and main toolbar.
 */
class MainMenuController(private var gameViewModel: GameViewModel) {

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
			this.fileExtensions,
		)
		val selectedFile = fileChooser.showOpenDialog(this.mainToolbarOpen?.parent?.scene?.window)
		println(selectedFile ?: selectedFile.toString())
	}

	@FXML
	fun showSaveDialog(event: ActionEvent) {
		val fileChooser = FileChooser()
		fileChooser.extensionFilters.addAll(
			this.fileExtensions,
		)
		fileChooser.title = uiBundle?.getString("dialog_title_save")

		val saveFile = fileChooser.showSaveDialog(this.mainToolbarSave?.parent?.scene?.window)

		if (saveFile != null) {
			var saveFilePath = saveFile.absolutePath
			if (!saveFilePath.endsWith(".fia")) {
				saveFilePath += ".fia"
			}
			GameIO.save(gameViewModel.game, saveFilePath)
		}
	}

	/**
	 * Spawn and show the "About Sofia" dialog.
	 *
	 * param[event] A click event received from the associated menu item.
	 */
	fun showAboutDialog(event: ActionEvent) {
		val aboutLoader = FXMLLoader(ClassLoader.getSystemResource("fxml/about.fxml"), uiBundle)
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
