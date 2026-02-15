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
import javafx.scene.control.ToolBar
import javafx.stage.Stage
import java.util.*

import com.github.jimbovm.isobel.common.Game
import com.github.jimbovm.isobel.common.Area
import com.github.jimbovm.isobel.common.AreaHeader
import com.github.jimbovm.isobel.actor.population.Character
import com.github.jimbovm.sofia.presenter.editor.BackgroundFillSceneryRenderer
import com.github.jimbovm.sofia.presenter.editor.CharacterRenderer
import com.github.jimbovm.sofia.fxml.EditorController
import com.github.jimbovm.sofia.presenter.editor.ExtensiblePlatformRenderer
import com.github.jimbovm.sofia.presenter.editor.FixedStaticRenderer
import com.github.jimbovm.sofia.presenter.editor.SingletonObjectRenderer
import com.github.jimbovm.sofia.presenter.editor.StartPositionRenderer
import com.github.jimbovm.sofia.presenter.editor.TextRenderer
import javafx.scene.control.Accordion
import javafx.scene.control.TitledPane

import com.github.jimbovm.isobel.common.AreaHeader.Background
import com.github.jimbovm.sofia.presenter.editor.AreaRenderer
import com.github.jimbovm.sofia.presenter.editor.StaircaseRenderer

/**
 * Main launcher for the Sofia GUI.
 */
class Main() : Application() {

	private val uiBundle: ResourceBundle? = ResourceBundle.getBundle("i18n/sofia_ui")
	private val mainLoader = FXMLLoader(ClassLoader.getSystemResource("fxml/main.fxml"), this.uiBundle)
	private val mainMenuLoader = FXMLLoader(ClassLoader.getSystemResource("fxml/main_menu.fxml"), this.uiBundle)
	private val mainToolbarLoader =
		FXMLLoader(ClassLoader.getSystemResource("fxml/main_toolbar.fxml"), this.uiBundle)
	private val fileMenuLoader = FXMLLoader(ClassLoader.getSystemResource("fxml/file_menu.fxml"), this.uiBundle)
	private val mainPane: Pane = mainLoader.load()
	private val scene = Scene(mainPane)

	private val mainToolbar: ToolBar = mainToolbarLoader.load()

	/**
	 * The current game being worked on in this session.
	 */
	private var game = Game()

	fun setUpCanvas(area: Area) {

		val editorLoader = FXMLLoader(ClassLoader.getSystemResource("fxml/editor.fxml"), this.uiBundle)
		val editor: TitledPane = editorLoader.load()

		editor.text = area.familiarName

		val canvas = Canvas(256.0, 240.0)

		val editorController: EditorController =
			editorLoader.getController<EditorController>() as EditorController
		val canvasContainer: Group = editorLoader.namespace["canvasContainer"] as Group
		canvasContainer.id = "canvasContainer_" + area.id

		canvasContainer.children?.add(canvas)

		val areaRenderer = AreaRenderer(area, canvas)
		areaRenderer.update()

		val areaList: Accordion = scene.lookup("#areaList") as Accordion
		areaList.panes.add(editor)
	}

	override fun start(primaryStage: Stage?) {

		primaryStage?.apply {
			icons?.add(Image("img/icon_128x128.png"))
			icons?.add(Image("img/icon_64x64.png"))
			icons?.add(Image("img/icon_32x32.png"))
			icons?.add(Image("img/icon_16x16.png"))
		}

		primaryStage?.title = "Sofia"
		primaryStage?.scene = scene
		displayAreas(scene)
		primaryStage?.show()
	}

	fun displayAreas(scene: Scene) {

		this.game.atlas.areas.add(Area())
		this.game.atlas.areas.add(Area())
		this.game.atlas.areas.add(Area())

		this.game.atlas.areas[0].apply {
			familiarName = "Some area"
			header.background = AreaHeader.Background.NIGHT
			header.scenery = AreaHeader.Scenery.HILLS
			population = mutableListOf(
				Character.create(13, 4, Character.Type.GOOMBA, false)
			)
		}
		this.game.atlas.areas[1].apply {
			familiarName = "Some other area"
			header.scenery = AreaHeader.Scenery.CLOUDS
			population = mutableListOf(
				Character.create(12, 3, Character.Type.GREEN_PARATROOPA_HOP, false)
			)
		}
		this.game.atlas.areas[2].apply {
			familiarName = "Some other other area"
			header.platform = AreaHeader.Platform.MUSHROOM
			header.scenery = AreaHeader.Scenery.HILLS
			population = mutableListOf(
				Character.create(6, 3, Character.Type.LAKITU, false)
			)
		}

		this.game.atlas.areas.forEach {
			setUpCanvas(it)
		}
	}

	companion object {

		fun run(args: Array<String>): Unit = launch(Main::class.java, *args)
	}
}