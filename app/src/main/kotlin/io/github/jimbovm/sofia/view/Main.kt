/*
 * SPDX-License-Identifier: MIT-0
 *
 * This file is part of Sofia (https://github.com/jimbovm/sofia).
 */

package io.github.jimbovm.sofia.view

import io.github.jimbovm.isobel.common.Area
import io.github.jimbovm.isobel.common.AreaHeader
import io.github.jimbovm.isobel.common.Game
import io.github.jimbovm.sofia.controller.EditorController
import io.github.jimbovm.sofia.controller.MainMenuController
import io.github.jimbovm.sofia.controller.MainToolbarController
import io.github.jimbovm.sofia.io.GameIO
import io.github.jimbovm.sofia.presenter.editor.AreaRenderer
import io.github.jimbovm.sofia.viewmodel.AreaHeaderViewModel
import io.github.jimbovm.sofia.viewmodel.AreaViewModel
import io.github.jimbovm.sofia.viewmodel.GameViewModel
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.control.Accordion
import javafx.scene.control.MenuBar
import javafx.scene.control.TitledPane
import javafx.scene.control.ToolBar
import javafx.scene.image.Image
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import java.util.*
import io.github.jimbovm.sofia.presenter.editor.Skin


/**
 * Main launcher for the Sofia GUI.
 */
class Main : Application() {

	private val uiBundle: ResourceBundle? = ResourceBundle.getBundle("i18n/sofia_ui")
	private val mainLoader = FXMLLoader(ClassLoader.getSystemResource("fxml/main.fxml"), this.uiBundle)

	private val fileMenuLoader = FXMLLoader(ClassLoader.getSystemResource("fxml/file_menu.fxml"), this.uiBundle)
	private val actorPaneLoader = FXMLLoader(ClassLoader.getSystemResource("fxml/actor_pane.fxml"), this.uiBundle)
	private val mainPane: Pane = mainLoader.load()
	private val scene = Scene(mainPane)

	/**
	 * The current game being worked on in this session.
	 */
	private var gameViewModel = GameViewModel(Game())

	fun setUpCanvas(areaViewModel: AreaViewModel, areaHeaderViewModel: AreaHeaderViewModel) {
		val editorLoader = FXMLLoader(ClassLoader.getSystemResource("fxml/editor.fxml"), this.uiBundle)
		editorLoader.setController(EditorController(areaViewModel, areaHeaderViewModel))

		val editor: TitledPane = editorLoader.load()
		editor.text = areaViewModel.familiarName.get()

		val canvasContainer: Group = editorLoader.namespace["canvasContainer"] as Group
		canvasContainer.id = "canvasContainer_" + areaViewModel.id.get()

		val canvas = canvasContainer.children.first() as Canvas
		canvas.id = "canvas_" + areaViewModel.id.get()

		val areaRenderer = AreaRenderer(areaViewModel.area, Skin(areaViewModel.area), canvas)
		areaRenderer.update()

		val areaList: Accordion = scene.lookup("#areaList") as Accordion
		areaList.panes.add(editor)
	}

	fun setUpMenus() {
		val mainMenuLoader =
			FXMLLoader(ClassLoader.getSystemResource("fxml/main_menu_bar.fxml"), this.uiBundle)
		val mainMenuController = MainMenuController(this.gameViewModel)
		val mainToolbarLoader =
			FXMLLoader(ClassLoader.getSystemResource("fxml/main_toolbar.fxml"), this.uiBundle)
		val mainToolbarController = MainToolbarController(this.gameViewModel)

		mainMenuLoader.setController(mainMenuController)
		mainToolbarLoader.setController(mainToolbarController)

		val mainMenu: MenuBar = mainMenuLoader.load() as MenuBar
		val mainToolbar: ToolBar = mainToolbarLoader.load() as ToolBar

		val mainMenuContainer: StackPane = mainLoader.namespace["mainMenuContainer"] as StackPane
		val mainToolbarContainer: StackPane = mainLoader.namespace["mainToolbarContainer"] as StackPane

		mainMenuContainer.children.add(mainMenu)
		mainToolbarContainer.children.add(mainToolbar)
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

		setUpMenus()
		displayAreas(scene)
		primaryStage?.show()
	}

	fun displayAreas(scene: Scene) {
		val game = Game()
		game.atlas.add(Area());
		game.atlas.add(Area());
		game.atlas.add(Area());
		game.atlas.add(Area());
		game.atlas.add(Area());

		game.atlas.areas.forEach {
			setUpCanvas(AreaViewModel(it), AreaHeaderViewModel(it.header))
		}
	}

	companion object {

		fun run(args: Array<String>): Unit = launch(Main::class.java, *args)
	}
}
