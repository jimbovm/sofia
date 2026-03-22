/*
 * SPDX-License-Identifier: MIT-0
 *
 * This file is part of Sofia (https://github.com/jimbovm/sofia).
 */

package io.github.jimbovm.sofia.editor

import java.util.*

import javafx.scene.canvas.Canvas

import io.github.jimbovm.isobel.actor.geography.FixedExtensible
import io.github.jimbovm.isobel.actor.geography.Row
import io.github.jimbovm.isobel.common.Area

class RowRenderer : BaseRenderer {

	constructor(canvas: Canvas, area: Area, skin: Skin) : super(canvas, area, skin)

	private fun renderSprites(sprite: Sprite, x: Int, y: Int, extent: Int) {
		this.drawSprite(sprite, x, y)

		for (xCursor in (x + 1)..(x + extent)) {
			val pageBoundary = (xCursor % 15) == 0
			this.drawSprite(sprite, xCursor, y)
			if (pageBoundary) break
		}
	}

	private fun renderRow(row: Row) {
		val sprite = when (row.type) {
			Row.Type.BRICK -> Sprite.Metatile.BRICK_BLOCK.sprite
			Row.Type.BLOCK -> Sprite.Metatile.BLOCK.sprite
			Row.Type.COIN -> Sprite.Metatile.COIN.sprite
		}

		renderSprites(sprite, row.x, row.y, row.extent)
	}

	private fun renderFixedExtensible(fixedExtensible: FixedExtensible) {
		if (fixedExtensible.type.name.contains("QUESTION_BLOCK_RUN")) {
			val y = fixedExtensible.type.name.substring(fixedExtensible.type.name.indexOf('Y') + 1).toInt()
			renderSprites(Sprite.Metatile.QUESTION_BLOCK.sprite, fixedExtensible.x, y, fixedExtensible.extent)
		} else if (fixedExtensible.type.name.contains("BRIDGE")) {
			val y = fixedExtensible.type.name.substring(fixedExtensible.type.name.indexOf('Y') + 1).toInt()
			renderSprites(Sprite.Metatile.BRIDGE.sprite, fixedExtensible.x, y, fixedExtensible.extent)
		}
	}

	fun render(row: Row) {
		renderRow(row)
	}

	fun render(fixedExtensible: FixedExtensible) {
		renderFixedExtensible(fixedExtensible)
	}
}
