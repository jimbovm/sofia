package com.github.jimbovm.sofia.presenter.editor

import javafx.scene.canvas.Canvas
import javafx.scene.image.Image

import com.github.jimbovm.isobel.common.Area

import com.github.jimbovm.sofia.presenter.editor.Sprite

class TextRenderer : Renderer {

	var xOrigin = 0.0
	var yOrigin = 0.0
	var text = "TEST"

	constructor(canvas: Canvas, area: Area) : super(canvas, area)

	companion object {
		private const val GLYPH_WIDTH = 8.0
		private const val GLYPH_HEIGHT = 8.0
		private val GLYPH_SHEET: Image = Image("img/graphics/smb_glyphs.png")

		enum class Glyph(val sprite: Sprite) {
			NUM_0(Sprite(0.0, 0.0, GLYPH_WIDTH, GLYPH_HEIGHT)),
			NUM_1(Sprite(8.0, 0.0, GLYPH_WIDTH, GLYPH_HEIGHT)),
			NUM_2(Sprite(16.0, 0.0, GLYPH_WIDTH, GLYPH_HEIGHT)),
			NUM_3(Sprite(24.0, 0.0, GLYPH_WIDTH, GLYPH_HEIGHT)),
			NUM_4(Sprite(32.0, 0.0, GLYPH_WIDTH, GLYPH_HEIGHT)),
			NUM_5(Sprite(40.0, 0.0, GLYPH_WIDTH, GLYPH_HEIGHT)),
			NUM_6(Sprite(48.0, 0.0, GLYPH_WIDTH, GLYPH_HEIGHT)),
			NUM_7(Sprite(56.0, 0.0, GLYPH_WIDTH, GLYPH_HEIGHT)),
			NUM_8(Sprite(64.0, 0.0, GLYPH_WIDTH, GLYPH_HEIGHT)),
			NUM_9(Sprite(72.0, 0.0, GLYPH_WIDTH, GLYPH_HEIGHT)),
			A(Sprite(80.0, 0.0, GLYPH_WIDTH, GLYPH_HEIGHT)),
			B(Sprite(88.0, 0.0, GLYPH_WIDTH, GLYPH_HEIGHT)),
			C(Sprite(96.0, 0.0, GLYPH_WIDTH, GLYPH_HEIGHT)),
			D(Sprite(104.0, 0.0, GLYPH_WIDTH, GLYPH_HEIGHT)),
			E(Sprite(112.0, 0.0, GLYPH_WIDTH, GLYPH_HEIGHT)),
			F(Sprite(120.0, 0.0, GLYPH_WIDTH, GLYPH_HEIGHT)),
			G(Sprite(0.0, 8.0, GLYPH_WIDTH, GLYPH_HEIGHT)),
			H(Sprite(8.0, 8.0, GLYPH_WIDTH, GLYPH_HEIGHT)),
			I(Sprite(16.0, 8.0, GLYPH_WIDTH, GLYPH_HEIGHT)),
			J(Sprite(24.0, 8.0, GLYPH_WIDTH, GLYPH_HEIGHT)),
			K(Sprite(32.0, 8.0, GLYPH_WIDTH, GLYPH_HEIGHT)),
			L(Sprite(40.0, 8.0, GLYPH_WIDTH, GLYPH_HEIGHT)),
			M(Sprite(48.0, 8.0, GLYPH_WIDTH, GLYPH_HEIGHT)),
			N(Sprite(56.0, 8.0, GLYPH_WIDTH, GLYPH_HEIGHT)),
			O(Sprite(64.0, 8.0, GLYPH_WIDTH, GLYPH_HEIGHT)),
			P(Sprite(72.0, 8.0, GLYPH_WIDTH, GLYPH_HEIGHT)),
			Q(Sprite(80.0, 8.0, GLYPH_WIDTH, GLYPH_HEIGHT)),
			R(Sprite(88.0, 8.0, GLYPH_WIDTH, GLYPH_HEIGHT)),
			S(Sprite(96.0, 8.0, GLYPH_WIDTH, GLYPH_HEIGHT)),
			T(Sprite(104.0, 8.0, GLYPH_WIDTH, GLYPH_HEIGHT)),
			U(Sprite(112.0, 8.0, GLYPH_WIDTH, GLYPH_HEIGHT)),
			V(Sprite(120.0, 8.0, GLYPH_WIDTH, GLYPH_HEIGHT)),
			W(Sprite(0.0, 16.0, GLYPH_WIDTH, GLYPH_HEIGHT)),
			X(Sprite(8.0, 16.0, GLYPH_WIDTH, GLYPH_HEIGHT)),
			Y(Sprite(16.0, 16.0, GLYPH_WIDTH, GLYPH_HEIGHT)),
			Z(Sprite(24.0, 16.0, GLYPH_WIDTH, GLYPH_HEIGHT)),
			HYPHEN(Sprite(32.0, 16.0, GLYPH_WIDTH, GLYPH_HEIGHT)),
			TIMES(Sprite(40.0, 16.0, GLYPH_WIDTH, GLYPH_HEIGHT)),
			EXCLAMATION(Sprite(48.0, 16.0, GLYPH_WIDTH, GLYPH_HEIGHT)),
			FULL_STOP(Sprite(56.0, 16.0, GLYPH_WIDTH, GLYPH_HEIGHT)),
			COPYRIGHT(Sprite(64.0, 16.0, GLYPH_WIDTH, GLYPH_HEIGHT)),
			DOWN(Sprite(72.0, 16.0, GLYPH_WIDTH, GLYPH_HEIGHT)),
			UP(Sprite(80.0, 16.0, GLYPH_WIDTH, GLYPH_HEIGHT)),
			LEFT(Sprite(88.0, 16.0, GLYPH_WIDTH, GLYPH_HEIGHT)),
			RIGHT(Sprite(96.0, 16.0, GLYPH_WIDTH, GLYPH_HEIGHT)),
			HEART(Sprite(104.0, 16.0, GLYPH_WIDTH, GLYPH_HEIGHT)),
			QUESTION(Sprite(112.0, 16.0, GLYPH_WIDTH, GLYPH_HEIGHT)),
			CROWN(Sprite(120.0, 16.0, GLYPH_WIDTH, GLYPH_HEIGHT));
		}
	}

	private fun getGlyph(glyph: Char): Glyph {

		if (glyph in '0'..'9') {
			return Glyph.valueOf("NUM_$glyph")
		}

		if (glyph in 'A'..'Z') {
			return Glyph.valueOf(glyph.toString())
		}

		when (glyph) {
			'-' -> return Glyph.HYPHEN
			'×' -> return Glyph.TIMES
			'!' -> return Glyph.EXCLAMATION
			'.' -> return Glyph.FULL_STOP
			'Ⓒ' -> return Glyph.COPYRIGHT
			'↓' -> return Glyph.DOWN
			'↑' -> return Glyph.UP
			'←' -> return Glyph.LEFT
			'→' -> return Glyph.RIGHT
			'♥' -> return Glyph.HEART
			'?' -> return Glyph.QUESTION
			'♕' -> return Glyph.CROWN
		}

		return Glyph.QUESTION
	}

	override fun render() {
		
		var dx = xOrigin
		var dy = yOrigin
		
		for (char in this.text) {
			when (char) {
				'\n' -> {
					dx = xOrigin
					dy += GLYPH_HEIGHT
					continue
				}
				' ' -> {
					dx += GLYPH_WIDTH
					continue
				}
			}
			
			val glyph = this.getGlyph(char)
			
			this.canvas.graphicsContext2D.drawImage(
				GLYPH_SHEET,
				glyph.sprite.x,
				glyph.sprite.y,
				GLYPH_WIDTH,
				GLYPH_HEIGHT,
				dx,
				dy,
				GLYPH_WIDTH,
				GLYPH_HEIGHT
			)

			dx += GLYPH_WIDTH
		}
	}
}