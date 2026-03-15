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
import io.github.jimbovm.isobel.common.AreaHeader

import io.github.jimbovm.sofia.viewmodel.AreaHeaderViewModel
import io.github.jimbovm.sofia.viewmodel.AreaViewModel
import javafx.collections.FXCollections
import javafx.scene.control.CheckBox

class EditorController(private val areaViewModel: AreaViewModel, private val areaHeaderViewModel: AreaHeaderViewModel) {

	@FXML
	val editor: TitledPane? = null

	@FXML
	val canvasContainer: Group? = null

	@FXML
	val editorToolbar: ToolBar? = null

	@FXML
	lateinit var autowalkCheckBox: CheckBox

	@FXML
	lateinit var environmentChoiceBox: ChoiceBox<Area.Environment>

	@FXML
	lateinit var fillChoiceBox: ChoiceBox<AreaHeader.Fill>

	@FXML
	lateinit var platformChoiceBox: ChoiceBox<AreaHeader.Platform>

	@FXML
	lateinit var sceneryChoiceBox: ChoiceBox<AreaHeader.Scenery>

	@FXML
	lateinit var startPositionChoiceBox: ChoiceBox<AreaHeader.StartPosition>

	@FXML
	lateinit var timeChoiceBox: ChoiceBox<Int>

	private var area: Area? = null

	@FXML
	fun initialize() {
		editor?.text = area?.familiarName

		environmentChoiceBox.items = FXCollections.observableArrayList(Area.Environment.values().toList())
		sceneryChoiceBox.items = FXCollections.observableArrayList(AreaHeader.Scenery.values().toList())
		platformChoiceBox.items = FXCollections.observableArrayList(AreaHeader.Platform.values().toList())
		fillChoiceBox.items = FXCollections.observableArrayList(AreaHeader.Fill.values().toList())
		startPositionChoiceBox.items =
			FXCollections.observableArrayList(
				listOf(
					AreaHeader.StartPosition.BOTTOM,
					AreaHeader.StartPosition.MIDDLE,
					AreaHeader.StartPosition.FALL
				)
			)
		timeChoiceBox.items = FXCollections.observableArrayList(0, 200, 300, 400)

		autowalkCheckBox.selectedProperty()?.bindBidirectional(areaHeaderViewModel.autowalk)
		environmentChoiceBox.valueProperty()?.bindBidirectional(areaViewModel.environment)
		fillChoiceBox.valueProperty()?.bindBidirectional(areaHeaderViewModel.fill)
		platformChoiceBox.valueProperty()?.bindBidirectional(areaHeaderViewModel.platform)
		sceneryChoiceBox.valueProperty()?.bindBidirectional(areaHeaderViewModel.scenery)
		startPositionChoiceBox.valueProperty()?.bindBidirectional(areaHeaderViewModel.startPosition)
		timeChoiceBox.valueProperty()?.bindBidirectional(areaHeaderViewModel.ticks)
	}
}