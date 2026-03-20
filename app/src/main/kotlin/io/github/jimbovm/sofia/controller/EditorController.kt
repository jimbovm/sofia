/*
 * SPDX-License-Identifier: MIT-0
 *
 * This file is part of Sofia (https://github.com/jimbovm/sofia).
 */

package io.github.jimbovm.sofia.controller

import io.github.jimbovm.isobel.common.Area
import io.github.jimbovm.isobel.common.AreaHeader
import io.github.jimbovm.sofia.presenter.editor.AreaRenderer
import io.github.jimbovm.sofia.viewmodel.AreaHeaderViewModel
import io.github.jimbovm.sofia.viewmodel.AreaViewModel
import io.github.jimbovm.sofia.presenter.editor.Skin
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.Group
import javafx.scene.canvas.Canvas
import javafx.scene.control.CheckBox
import javafx.scene.control.ChoiceBox
import javafx.scene.control.TitledPane
import javafx.scene.control.ToolBar
import org.apache.logging.log4j.LogManager;

class EditorController(private val areaViewModel: AreaViewModel, private val areaHeaderViewModel: AreaHeaderViewModel) {

	private val log = LogManager.getRootLogger();

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

	private lateinit var areaRenderer: AreaRenderer

	private fun redraw() {
		log.info("Redrawing area ${areaViewModel.area}")
		areaRenderer.update()
	}

	@FXML
	fun initialize() {

		val skin = Skin(areaViewModel.area);
		areaRenderer = AreaRenderer(areaViewModel.area, skin, canvas)
		areaRenderer.update()

		editor.text = areaViewModel.familiarName.get()

		environmentChoiceBox.items = FXCollections.observableArrayList(Area.Environment.values().toList())
		environmentChoiceBox.value = areaViewModel.environment.get()

		sceneryChoiceBox.items = FXCollections.observableArrayList(AreaHeader.Scenery.values().toList())
		sceneryChoiceBox.value = areaHeaderViewModel.scenery.get()

		platformChoiceBox.items = FXCollections.observableArrayList(AreaHeader.Platform.values().toList())
		platformChoiceBox.value = areaHeaderViewModel.platform.get()

		fillChoiceBox.items = FXCollections.observableArrayList(AreaHeader.Fill.values().toList())
		fillChoiceBox.value = areaHeaderViewModel.fill.get()

		startPositionChoiceBox.items =
			FXCollections.observableArrayList(
				listOf(
					AreaHeader.StartPosition.BOTTOM,
					AreaHeader.StartPosition.MIDDLE,
					AreaHeader.StartPosition.FALL,
				),
			)
		startPositionChoiceBox.value = areaHeaderViewModel.startPosition.get()

		timeChoiceBox.items = FXCollections.observableArrayList(0, 200, 300, 400)
		timeChoiceBox.value = areaHeaderViewModel.ticks.get()

		autowalkCheckBox.selectedProperty().bindBidirectional(areaHeaderViewModel.autowalk)
		environmentChoiceBox.valueProperty().bindBidirectional(areaViewModel.environment)
		fillChoiceBox.valueProperty().bindBidirectional(areaHeaderViewModel.fill)
		platformChoiceBox.valueProperty().bindBidirectional(areaHeaderViewModel.platform)
		sceneryChoiceBox.valueProperty().bindBidirectional(areaHeaderViewModel.scenery)
		startPositionChoiceBox.valueProperty().bindBidirectional(areaHeaderViewModel.startPosition)
		timeChoiceBox.valueProperty().bindBidirectional(areaHeaderViewModel.ticks)

		environmentChoiceBox.valueProperty().addListener { _, _, _ -> this.redraw() }
	}
}
