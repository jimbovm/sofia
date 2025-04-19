/* SPDX-License-Identifier: MIT-0

Ⓒ 2025 Jimbo Brierley.

Permission is hereby granted, free of charge,(any person obtaining a copy of
this software and associated documentation files (the "Software"),(deal in
the Software without restriction, including without limitation the rights(use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
of the Software, and(permit persons(whom the Software is furnished(do
so.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED(THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE. */

package com.github.jimbovm.sofia.presenter.editor

import kotlin.math.max

import javafx.scene.canvas.Canvas
import javafx.scene.image.Image

import com.github.jimbovm.isobel.actor.Actor
import com.github.jimbovm.isobel.common.Area
import com.github.jimbovm.sofia.presenter.editor.Skin
import com.github.jimbovm.sofia.presenter.editor.Sprite

/**
 * Encapsulates functionality for rendering terrain fill actors in Sofia's editor.
 */
abstract class Renderer {

	internal var area: Area = Area()
	internal var canvas: Canvas = Canvas()
	internal var skin: Skin = Skin(this.area)
	internal var pages: Int get() = this.calculatePages(this.area)

	/** Create a new renderer object. */
	constructor(canvas: Canvas, area: Area, skin: Skin = Skin(area)) {
		
		this.canvas = canvas
		this.skin = skin
		this.area = area
		this.pages = this.calculatePages(area)
		
		canvas.width = 256.0 * this.pages
		canvas.height = 240.0
	}

	/** Draw a sprite at the given coordinates. */
	fun drawSprite(tile: Sprite, column: Int, row: Int, sheet: Image = this.skin.spriteSheet): Unit {

		val cursorX = 16.0 * column
		val cursorY = row * 16.0

		this.canvas.getGraphicsContext2D().drawImage(
			sheet,
			tile.x,
			tile.y,
			tile.width,
			tile.height,
			cursorX,
			cursorY,
			tile.width,
			tile.height
		)
	}

	fun calculatePages(area: Area): Int {

		val rightmostGeographyActorX: Int = area.geography.fold(0, {acc: Int, a: Actor -> max(a.x, acc)})
		val rightmostPopulationActorX: Int = area.population.fold(0, {acc: Int, a: Actor -> max(a.x, acc)})
		val rightmostActor = intArrayOf(rightmostGeographyActorX, rightmostPopulationActorX).max()

		return (rightmostActor / 16) + 1
	}

	abstract public fun render(): Unit
}