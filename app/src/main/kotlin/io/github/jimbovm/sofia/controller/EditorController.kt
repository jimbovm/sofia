/*
 * SPDX-License-Identifier: MIT-0
 *
 * This file is part of Sofia (https://github.com/jimbovm/sofia).
 */

package io.github.jimbovm.sofia.controller

import io.github.jimbovm.isobel.common.Area
import io.github.jimbovm.isobel.common.AreaHeader
import io.github.jimbovm.sofia.viewmodel.AreaHeaderViewModel
import io.github.jimbovm.sofia.viewmodel.AreaViewModel
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.Group
import javafx.scene.canvas.Canvas
import javafx.scene.control.CheckBox
import javafx.scene.control.ChoiceBox
import javafx.scene.control.TitledPane
import javafx.scene.control.ToolBar

class EditorController(private val areaViewModel: AreaViewModel, private val areaHeaderViewModel: AreaHeaderViewModel) {

	@FXML
	lateinit var editor: TitledPane

	@FXML
	lateinit var canvas: Canvas

	@FXML
	lateinit var canvasContainer: Group

	@FXML
	lateinit var editorToolbar: ToolBar

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

	@FXML
	fun initialize() {
		editor.text = areaViewModel.familiarName.get()

		environmentChoiceBox.items = FXCollections.observableArrayList(Area.Environment.values().toList())
		sceneryChoiceBox.items = FXCollections.observableArrayList(AreaHeader.Scenery.values().toList())
		platformChoiceBox.items = FXCollections.observableArrayList(AreaHeader.Platform.values().toList())
		fillChoiceBox.items = FXCollections.observableArrayList(AreaHeader.Fill.values().toList())
		startPositionChoiceBox.items =
			FXCollections.observableArrayList(
				listOf(
					AreaHeader.StartPosition.BOTTOM,
					AreaHeader.StartPosition.MIDDLE,
					AreaHeader.StartPosition.FALL,
				),
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
