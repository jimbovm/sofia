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
import javafx.scene.image.Image

import com.github.jimbovm.isobel.common.Area
import com.github.jimbovm.isobel.actor.geography.Staircase

import com.github.jimbovm.sofia.presenter.editor.Skin
import com.github.jimbovm.sofia.presenter.editor.Sprite
import com.github.jimbovm.sofia.presenter.editor.Sprite.Metatile
import sun.awt.X11.XRenderDirectFormat

/**
 * Encapsulates functionality for rendering terrain fill actors in Sofia's editor.
 */
class StaircaseRenderer : Renderer {

	/** Create a new renderer object. */
	constructor(canvas: Canvas, area: Area) : super(canvas, area)

	fun render(staircase: Staircase): Unit {

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