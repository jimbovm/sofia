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
import javafx.scene.Group
import javafx.scene.control.ChoiceBox
import javafx.scene.control.TitledPane
import javafx.scene.control.ToolBar

import io.github.jimbovm.isobel.common.Area

class EditorController {

	@FXML
	val editor: TitledPane? = null

	@FXML
	val canvasContainer: Group? = null

	@FXML
	val editorToolbar: ToolBar? = null

	@FXML
	val environmentChoiceBox: ChoiceBox<Area.Environment>? = null

	private var area: Area? = null

	fun initialize() {
		editor?.text = area?.familiarName
	}
}