/*
 * SPDX-License-Identifier: MIT-0
 *
 * This file is part of Sofia (https://github.com/jimbovm/sofia).
 */

package io.github.jimbovm.sofia.editor

import javafx.scene.canvas.Canvas

import io.github.jimbovm.isobel.actor.geography.UprightPipe
import io.github.jimbovm.isobel.common.Area

class UprightPipeRenderer : BaseRenderer {

	constructor(canvas: Canvas, area: Area, skin: Skin) : super(canvas, area, skin)

	fun render(pipe: UprightPipe) {
		this.drawSprite(Sprite.Metatile.UPRIGHT_PIPE_MOUTH.sprite, pipe.x + 1, pipe.y)

		val pipeBodyTop = pipe.y + 1
		val pipeBase = (pipe.y + pipe.extent)

		for (yCursor in pipeBodyTop..pipeBase) {
			this.drawSprite(Sprite.Metatile.UPRIGHT_PIPE_BODY_LEFT.sprite, pipe.x, yCursor)
			this.drawSprite(Sprite.Metatile.UPRIGHT_PIPE_BODY_RIGHT.sprite, pipe.x + 1, yCursor)
		}
	}
}
