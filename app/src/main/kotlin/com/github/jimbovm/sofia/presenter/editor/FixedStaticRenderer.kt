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

import javafx.scene.canvas.Canvas
import java.util.*

import com.github.jimbovm.isobel.actor.geography.FixedStatic
import com.github.jimbovm.isobel.common.Area

class FixedStaticRenderer : Renderer {

	constructor(canvas: Canvas, area: Area) : super(canvas, area)

	fun render(fixedStatic: FixedStatic): Unit {

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

	private fun renderFlagpole(flagpole: FixedStatic): Unit {

		val flagY = 3

		for (yCursor in 12 downTo 2) {
			if (yCursor == 12) this.drawSprite(Sprite.Metatile.BLOCK.sprite, flagpole.x, yCursor)
			else if (yCursor >= 3) this.drawSprite(
				Sprite.Metatile.FLAGPOLE_STAFF.sprite,
				flagpole.x,
				yCursor
			)
			else if (yCursor == 2) this.drawSprite(
				Sprite.Metatile.FLAGPOLE_FINIAL.sprite,
				flagpole.x,
				yCursor
			)
		}
		drawSprite(Sprite.Metatile.FLAGPOLE_FLAG.sprite, flagpole.x, flagY, -8.0, 1.0)
	}

	private fun renderBowserBridge(bowserBridge: FixedStatic): Unit {

		val y = 10

		for (x in bowserBridge.x..<bowserBridge.x + 13) {
			this.drawSprite(Sprite.Metatile.BOWSER_BRIDGE.sprite, x, y)
		}
	}

	private fun renderTeePipe(teePipe: FixedStatic): Unit {

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