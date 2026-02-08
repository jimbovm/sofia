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

package com.github.jimbovm.sofia.presenter.editor

import kotlin.math.max

import javafx.scene.canvas.Canvas
import javafx.scene.image.Image

import com.github.jimbovm.isobel.actor.Actor
import com.github.jimbovm.isobel.common.Area

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

		this.drawSprite(tile, column, row, 0.0, 0.0, sheet)
	}

	/** Draw a sprite at the given coordinates at an offset. */
	fun drawSprite(tile: Sprite, column: Int, row: Int, offsetX: Double, offsetY: Double, sheet: Image = this.skin.spriteSheet): Unit {

		val cursorX = (16.0 * column) + offsetX
		val cursorY = (row * 16.0) + offsetY

		this.canvas.getGraphicsContext2D().drawImage(
			sheet,
			tile.x,
			tile.y,
			tile.width,
			tile.height,
			cursorX - (tile.width - 16.0),
			cursorY - (tile.height - 16.0),
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

	abstract fun render(): Unit
}