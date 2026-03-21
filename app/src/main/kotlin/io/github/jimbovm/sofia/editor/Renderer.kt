/*
 * SPDX-License-Identifier: MIT-0
 *
 * This file is part of Sofia (https://github.com/jimbovm/sofia).
 */

package io.github.jimbovm.sofia.editor

import io.github.jimbovm.isobel.actor.Actor
import io.github.jimbovm.isobel.common.Area
import javafx.scene.canvas.Canvas
import javafx.scene.image.Image
import kotlin.math.max

/**
 * Encapsulates functionality for rendering terrain fill actors in Sofia's editor.
 */
open class BaseRenderer {

	internal var area: Area = Area()
	internal var canvas: Canvas = Canvas()
	internal var skin: Skin = Skin(this.area)
	internal var pages: Int get() = this.calculatePages(this.area)

	/** Create a new renderer object. */
	constructor(canvas: Canvas, area: Area, skin: Skin) {

		this.canvas = canvas
		this.skin = skin
		this.area = area
		this.pages = this.calculatePages(area)

		canvas.width = 256.0 * this.pages
		canvas.height = 240.0
	}

	/** Draw a sprite at the given coordinates. */
	fun drawSprite(tile: Sprite, column: Int, row: Int, sheet: Image = this.skin.spriteSheet) {
		this.drawSprite(tile, column, row, 0.0, 0.0, sheet)
	}

	/** Draw a sprite at the given coordinates at an offset. */
	fun drawSprite(
		tile: Sprite,
		column: Int,
		row: Int,
		offsetX: Double,
		offsetY: Double,
		sheet: Image = this.skin.spriteSheet,
	) {
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
			tile.height,
		)
	}

	fun calculatePages(area: Area): Int {
		val rightmostGeographyActorX: Int = area.geography.fold(0) { acc: Int, a: Actor -> max(a.x, acc) }
		val rightmostPopulationActorX: Int = area.population.fold(0) { acc: Int, a: Actor -> max(a.x, acc) }
		val rightmostActor = intArrayOf(rightmostGeographyActorX, rightmostPopulationActorX).max()

		return (rightmostActor / 16) + 1
	}
}
