/*
 * SPDX-License-Identifier: MIT-0
 *
 * This file is part of Sofia (https://github.com/jimbovm/sofia).
 */

package io.github.jimbovm.sofia.editor

import sun.awt.X11.XRenderDirectFormat

import javafx.scene.canvas.Canvas
import javafx.scene.image.Image

import io.github.jimbovm.isobel.actor.geography.Staircase
import io.github.jimbovm.isobel.common.Area

import io.github.jimbovm.sofia.editor.Skin
import io.github.jimbovm.sofia.editor.Sprite
import io.github.jimbovm.sofia.editor.Sprite.Metatile

/**
 * Encapsulates functionality for rendering terrain fill actors in Sofia's editor.
 */
class StaircaseRenderer : BaseRenderer {

	/** Create a new renderer object. */
	constructor(canvas: Canvas, area: Area, skin: Skin) : super(canvas, area, skin)

	fun render(staircase: Staircase) {
		for ((height, xCursor) in (0..staircase.extent).withIndex()) {
			drawStep(staircase.x + xCursor, minOf(7, height))
		}
	}

	private fun drawStep(x: Int, height: Int) {
		for (yCursor in 12 downTo (12 - height)) {
			this.drawSprite(Sprite.Metatile.BLOCK.sprite, x, yCursor)
		}
	}
}
