/* SPDX-License-Identifier: MIT-0

Copyright 2025 Jimbo Brierley.

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

package com.github.jimbovm.sofia;

import javafx.application.Application
import javafx.beans.property.ReadOnlyListWrapper
import javafx.collections.ObservableList
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.scene.layout.BorderPane
import javafx.stage.Stage
import javafx.fxml.FXMLLoader

class Main() : Application() {

	override fun start(primaryStage: Stage?) {

		val fxmlLoader = FXMLLoader(ClassLoader.getSystemResource("fxml/main.fxml"))

		val mainPane: BorderPane = fxmlLoader.load()
		val scene = Scene(mainPane)

		primaryStage?.getIcons()?.add(Image("img/icon_128x128.png"))
		primaryStage?.getIcons()?.add(Image("img/icon_64x64.png"))
		primaryStage?.getIcons()?.add(Image("img/icon_32x32.png"))
		primaryStage?.getIcons()?.add(Image("img/icon_16x16.png"))

		primaryStage?.title = "Sofia"
		primaryStage?.scene = scene
		primaryStage?.show()
	}

	companion object {
		
		fun run(args: Array<String>): Unit = Application.launch(Main::class.java, *args)
	}
}