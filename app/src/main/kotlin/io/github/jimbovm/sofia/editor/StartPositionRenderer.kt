/*
 * SPDX-License-Identifier: MIT-0
 *
 * This file is part of Sofia (https://github.com/jimbovm/sofia).
 */

package io.github.jimbovm.sofia.editor

import io.github.jimbovm.isobel.common.Area
import io.github.jimbovm.isobel.common.AreaHeader.StartPosition
import io.github.jimbovm.sofia.editor.Skin
import io.github.jimbovm.sofia.editor.Sprite
import io.github.jimbovm.sofia.editor.Sprite.Metatile
import javafx.scene.canvas.Canvas
import javafx.scene.image.Image

/**
 * Encapsulates functionality for rendering terrain fill actors in Sofia's editor.
 */
class StartPositionRenderer : BaseRenderer {

	private val brosSheet: Image = Image("img/graphics/foreground/smb_bros.png")
	private val marioStand = Sprite(0.0, 0.0, 15.0, 16.0)
	private val marioFall = Sprite(16.0, 0.0, 15.0, 16.0)

	/** Create a new renderer object. */
	constructor(canvas: Canvas, area: Area, skin: Skin) : super(canvas, area, skin)

	fun render() {
		when (this.area.header.startPosition) {
			StartPosition.FALL -> this.drawSprite(marioFall, 2, 1, brosSheet)
			StartPosition.BOTTOM -> this.drawSprite(marioStand, 2, 12, brosSheet)
			StartPosition.MIDDLE -> this.drawSprite(marioStand, 2, 5, brosSheet)
			else -> this.drawSprite(marioFall, 2, 2, brosSheet)
		}
	}
}
