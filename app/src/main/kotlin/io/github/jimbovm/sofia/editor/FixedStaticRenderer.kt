/*
 * SPDX-License-Identifier: MIT-0
 *
 * This file is part of Sofia (https://github.com/jimbovm/sofia).
 */

package io.github.jimbovm.sofia.editor

import javafx.scene.canvas.Canvas
import java.util.*

import io.github.jimbovm.isobel.actor.geography.FixedStatic
import io.github.jimbovm.isobel.common.Area

class FixedStaticRenderer : BaseRenderer {

	constructor(canvas: Canvas, area: Area, skin: Skin) : super(canvas, area, skin)

	fun render(fixedStatic: FixedStatic) {
		val axeY = 8
		val chainY = 9

		when (fixedStatic.type) {
			FixedStatic.Type.AXE -> drawSprite(Sprite.Metatile.AXE.sprite, fixedStatic.x, axeY)
			FixedStatic.Type.CHAIN -> drawSprite(Sprite.Metatile.CHAIN.sprite, fixedStatic.x, chainY)
			FixedStatic.Type.FLAGPOLE -> this.renderFlagpole(fixedStatic)
			FixedStatic.Type.BOWSER_BRIDGE -> this.renderBowserBridge(fixedStatic)
			FixedStatic.Type.TEE_PIPE -> this.renderTeePipe(fixedStatic)
			else -> return
		}
	}

	private fun renderFlagpole(flagpole: FixedStatic) {
		val flagY = 3

		for (yCursor in 12 downTo 2) {
			if (yCursor == 12) {
				this.drawSprite(Sprite.Metatile.BLOCK.sprite, flagpole.x, yCursor)
			} else if (yCursor >= 3) {
				this.drawSprite(
					Sprite.Metatile.FLAGPOLE_STAFF.sprite,
					flagpole.x,
					yCursor,
				)
			} else if (yCursor == 2) {
				this.drawSprite(
					Sprite.Metatile.FLAGPOLE_FINIAL.sprite,
					flagpole.x,
					yCursor,
				)
			}
		}
		drawSprite(Sprite.Metatile.FLAGPOLE_FLAG.sprite, flagpole.x, flagY, -8.0, 1.0)
	}

	private fun renderBowserBridge(bowserBridge: FixedStatic) {
		val y = 10

		for (x in bowserBridge.x..<bowserBridge.x + 13) {
			this.drawSprite(Sprite.Metatile.BOWSER_BRIDGE.sprite, x, y)
		}
	}

	private fun renderTeePipe(teePipe: FixedStatic) {
		val teePipeBase = 12
		val teePipeTop = 9

		for (y in teePipeBase downTo (teePipeTop + 1)) {
			this.drawSprite(Sprite.Metatile.UPRIGHT_PIPE_BODY_RIGHT.sprite, teePipe.x + 4, y)
		}
		this.drawSprite(Sprite.Metatile.UPRIGHT_PIPE_BODY_LEFT.sprite, teePipe.x + 3, teePipeTop + 1)
		this.drawSprite(Sprite.Metatile.UPRIGHT_PIPE_MOUTH.sprite, teePipe.x + 4, teePipeTop)
		this.drawSprite(Sprite.Metatile.TEE_PIPE_MOUTH.sprite, teePipe.x + 3, teePipeBase)
	}
}
