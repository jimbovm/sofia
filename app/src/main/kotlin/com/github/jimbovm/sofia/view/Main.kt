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

package com.github.jimbovm.sofia.view

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.image.Image
import javafx.scene.layout.Pane
import javafx.stage.Stage
import java.util.*

import com.github.jimbovm.isobel.actor.geography.BackgroundModifier
import com.github.jimbovm.isobel.actor.geography.FillSceneryModifier
import com.github.jimbovm.isobel.common.Area
import com.github.jimbovm.isobel.common.AreaHeader
import com.github.jimbovm.isobel.common.AreaHeader.Background
import com.github.jimbovm.sofia.presenter.editor.BackgroundFillSceneryRenderer

/**
 * Main launcher for the Sofia GUI.
 */
class Main() : Application() {

	private val uiBundle: ResourceBundle? = ResourceBundle.getBundle("i18n/sofia_ui")
	private val fxmlLoader = FXMLLoader(ClassLoader.getSystemResource("fxml/main.fxml"), this.uiBundle)
	private val mainPane: Pane = fxmlLoader.load()
	private val scene = Scene(mainPane)

	private fun setUpCanvas(container: Group) {

		val canvas = Canvas(256.0, 240.0)

		container.getChildren().add(canvas)

		val area = Area().apply {
			header = AreaHeader().apply {
				background = Background.NONE
				fill = AreaHeader.Fill.FILL_2BF_0BC
				platform = AreaHeader.Platform.MUSHROOM
				scenery = AreaHeader.Scenery.CLOUDS
				startPosition = AreaHeader.StartPosition.BOTTOM
				ticks = 400
			}
			environment = Area.Environment.OVERWORLD
			geography = listOf(
				FillSceneryModifier.create(12, AreaHeader.Fill.FILL_NONE, AreaHeader.Scenery.CLOUDS),
				BackgroundModifier.create(12, Background.OVER_WATER),
				FillSceneryModifier.create(14, AreaHeader.Fill.FILL_2BF_0BC, AreaHeader.Scenery.CLOUDS),
				BackgroundModifier.create(14, Background.NONE),
				BackgroundModifier.create(17, Background.CASTLE_WALL),
				BackgroundModifier.create(23, Background.NONE),
				FillSceneryModifier.create(35, AreaHeader.Fill.FILL_2BF_0BC, AreaHeader.Scenery.HILLS),
				FillSceneryModifier.create(59, AreaHeader.Fill.FILL_2BF_0BC, AreaHeader.Scenery.FENCES),
				BackgroundModifier.create(61, Background.CASTLE_WALL),
				BackgroundModifier.create(63, Background.NONE),
				FillSceneryModifier.create(80, AreaHeader.Fill.FILL_ALL, AreaHeader.Scenery.HILLS)
			)
		}

		val renderer = BackgroundFillSceneryRenderer(canvas, area)
		renderer.render()
	}

	override fun start(primaryStage: Stage?) {

		val canvasContainer: Group = fxmlLoader.namespace.get("canvasContainer") as Group
		setUpCanvas(canvasContainer)

		primaryStage?.apply {
			getIcons()?.add(Image("img/icon_128x128.png"))
			getIcons()?.add(Image("img/icon_64x64.png"))
			getIcons()?.add(Image("img/icon_32x32.png"))
			getIcons()?.add(Image("img/icon_16x16.png"))
		}

		primaryStage?.title = "Sofia"
		primaryStage?.scene = scene
		primaryStage?.show()
	}

	companion object {
		
		fun run(args: Array<String>): Unit = Application.launch(Main::class.java, *args)
	}
}