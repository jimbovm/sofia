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

package io.github.jimbovm.sofia.fxml

import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.event.ActionEvent
import javafx.stage.FileChooser
import javafx.stage.Stage

class MainToolbarController {

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

	fun initialize() {
	}

	@FXML
	fun showOpenDialog(event: ActionEvent) {

		val fileChooser = FileChooser()
		val selectedFile =
			fileChooser.showOpenDialog(this.mainToolbarOpen?.scene?.window as Stage)
		println(selectedFile.toString())
	}
}