/*
 * SPDX-License-Identifier: MIT-0
 *
 * This file is part of Sofia (https://github.com/jimbovm/sofia).
 */

package io.github.jimbovm.sofia.editor

import java.util.*

import javafx.scene.canvas.Canvas

import io.github.jimbovm.isobel.actor.geography.Column
import io.github.jimbovm.isobel.actor.geography.FixedExtensible
import io.github.jimbovm.isobel.common.Area

class ColumnRenderer : BaseRenderer {

	constructor(canvas: Canvas, area: Area, skin: Skin) : super(canvas, area, skin)

	private fun renderSprites(sprite: Sprite, x: Int, y: Int, extent: Int) {
		this.drawSprite(sprite, x, y)

		for (yCursor in (y + 1)..(y + extent)) {
			val bottomOfScreen = yCursor >= Skin.SCREEN_HEIGHT
			this.drawSprite(sprite, x, yCursor)
			if (bottomOfScreen) break
		}
	}
	
	fun render(column: Column) {
		val sprite = when (column.type) {
			Column.Type.BRICK -> skin.brickTile
			Column.Type.BLOCK -> Sprite.Metatile.BLOCK.sprite
		}
		renderSprites(sprite, column.x, column.y, column.extent)
	}
}
