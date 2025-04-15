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

package com.github.jimbovm.sofia.presenter.editor;

import javafx.scene.canvas.Canvas
import javafx.scene.image.Image
import javafx.scene.paint.Color
import javafx.scene.paint.Paint

import com.github.jimbovm.isobel.common.Area
import com.github.jimbovm.isobel.common.Area.Environment
import com.github.jimbovm.isobel.common.AreaHeader
import com.github.jimbovm.isobel.common.AreaHeader.Fill
import com.github.jimbovm.isobel.common.AreaHeader.Scenery

import com.github.jimbovm.sofia.presenter.editor.Sprite

/**
 * A skin is a collection of assets for rendering an area based on that 
 * area's properties.
 */
final data class Skin(
		val backgroundColor: Color,
		val background: Sprite?,
		val foregroundA: Sprite,
		val foregroundB: Sprite,
		val scenery: Sprite?,
		val spriteSheet: Image = Image(ClassLoader.getSystemResourceAsStream("img/smb_sprite.png")),
		val backgroundScenerySheet: Image = Image(ClassLoader.getSystemResourceAsStream("img/smb_scenery.png"))
	) {
	
	/**
	 * Origin coordinates of the sprite sets for each environment (palette A).
	 */
	private final enum class ForegroundA(val data: Sprite) {
		UNDERWATER(Sprite(0.0, 0.0, Skin.SPRITE_FOREGROUND_A_WIDTH, Skin.SPRITE_FOREGROUND_A_HEIGHT)),
		OVERWORLD(Sprite(0.0, 64.0, Skin.SPRITE_FOREGROUND_A_WIDTH, Skin.SPRITE_FOREGROUND_A_HEIGHT)),
		UNDERGROUND(Sprite(0.0, 128.0, Skin.SPRITE_FOREGROUND_A_WIDTH, Skin.SPRITE_FOREGROUND_A_HEIGHT)),
		CASTLE(Sprite(0.0, 192.0, Skin.SPRITE_FOREGROUND_A_WIDTH, Skin.SPRITE_FOREGROUND_A_HEIGHT)),
		MONOCHROME(Sprite(0.0, 192.0, Skin.SPRITE_FOREGROUND_A_WIDTH, Skin.SPRITE_FOREGROUND_A_HEIGHT))
	}

	/**
	 * Origin coordinates of the sprite sets for each environment (palette B, including variants)
	 **/
	private final enum class ForegroundB(val data: Sprite) {
		UNDERWATER(Sprite(208.0, 0.0, SPRITE_FOREGROUND_B_WIDTH, SPRITE_FOREGROUND_B_HEIGHT)),
		OVERWORLD(Sprite(208.0, 64.0, SPRITE_FOREGROUND_B_WIDTH, SPRITE_FOREGROUND_B_HEIGHT)),
		UNDERGROUND(Sprite(208.0, 128.0, SPRITE_FOREGROUND_B_WIDTH, SPRITE_FOREGROUND_B_HEIGHT)),
		CASTLE(Sprite(208.0, 192.0, SPRITE_FOREGROUND_B_WIDTH, SPRITE_FOREGROUND_B_HEIGHT)),
		MUSHROOM(Sprite(352.0, 64.0, SPRITE_FOREGROUND_B_WIDTH, SPRITE_FOREGROUND_B_HEIGHT)),
		SNOW(Sprite(496.0, 64.0, SPRITE_FOREGROUND_B_WIDTH, SPRITE_FOREGROUND_B_HEIGHT)),
		MONOCHROME(Sprite(208.0, 192.0, SPRITE_FOREGROUND_B_WIDTH, SPRITE_FOREGROUND_B_HEIGHT)),
	}

	/**
	 * Default colour of the "sky", or blank background before any
	 * sprites are rendered.
	 */
	private final enum class BackgroundColor(val color: Color) {
		UNDERWATER(Color.web(BLUE_SKY)),
		OVERWORLD(Color.web(BLUE_SKY)),
		UNDERGROUND(Color.BLACK),
		CASTLE(Color.BLACK)
	}

	/**
	 * Offsets from the Y origin for the style in which to render the background.
	 */
	private final enum class BackgroundOffset(val y: Double) {
		UNDERWATER(1200.0),
		CASTLE_WALL(992.0),
		OVER_WATER(752.0)
	}

	/**
	 * Offsets from the X origin for background and scenery palette variants.
	 */
	private final enum class BackgroundSceneryOffset(val x: Double) {
		UNDERWATER(0.0),
		OVERWORLD(SCENERY_WIDTH),
		UNDERGROUND(SCENERY_WIDTH * 2),
		CASTLE(SCENERY_WIDTH * 3),
		MUSHROOM(SCENERY_WIDTH * 4),
		SNOW(SCENERY_WIDTH * 5),
		MONOCHROME(SCENERY_WIDTH * 3)
	}

	/**
	 * Offsets from the Y origin for the type of scenery to render.
	 */
	private final enum class SceneryOffset(val y: Double) {
		CLOUDS(448.0),
		HILLS(0.0),
		FENCES(272.0),
	}

	companion object {

		private final val BLOCK_SIZE = 16
		private final val SPRITE_SHEET_WIDTH = 1330.0
		private final val SPRITE_SHEET_HEIGHT = 64.0
		private final val SPRITE_FOREGROUND_A_WIDTH = 160.0
		private final val SPRITE_FOREGROUND_A_HEIGHT = 64.0
		private final val SPRITE_FOREGROUND_B_WIDTH = 144.0
		private final val SPRITE_FOREGROUND_B_HEIGHT = 64.0
		private final val SCENERY_WIDTH = 768.0
		private final val SCENERY_HEIGHT = 272.0

		private final val BLUE_SKY = "#6c6aff"

		fun of(area: Area): Skin {

			val environment = area.environment.name.toString()
			val headerBackground = area.header.background.name.toString()
			val night = headerBackground.contains("NIGHT")
			val useMushroomPalette = area.header.platform == AreaHeader.Platform.MUSHROOM
			val useSnowPalette = headerBackground.contains("SNOW")
			val useMonochromePalette = headerBackground == "MONOCHROME"

			val foregroundA = if (useMonochromePalette) {
				Skin.ForegroundA.valueOf(headerBackground).data
			} else {
				Skin.ForegroundA.valueOf(environment).data
			}
			
			val foregroundB = if (useMushroomPalette) {
				ForegroundB.MUSHROOM.data
			} else if (useSnowPalette) {
				ForegroundB.SNOW.data
			} else {
				ForegroundB.valueOf(environment).data
			}

			val backgroundColor = if (night || useMonochromePalette) {
				Color.BLACK
			} else {
				Skin.BackgroundColor.valueOf(environment).color
			}

			val backgroundSceneryXOffset = if (useMushroomPalette) {
				Skin.BackgroundSceneryOffset.MUSHROOM.x
			} else if (useSnowPalette) {
				Skin.BackgroundSceneryOffset.SNOW.x
			} else if (useMonochromePalette) {
				Skin.BackgroundSceneryOffset.MONOCHROME.x
			} else {
				0.0
			}
			val backgroundYOffset = when (headerBackground) {
				"UNDERWATER", "CASTLE_WALL", "OVER_WATER" -> Skin.BackgroundOffset.valueOf(headerBackground).y
				else -> 0.0
			}

			val background: Sprite? = when (headerBackground) {
				"UNDERWATER", "CASTLE_WALL", "OVER_WATER" -> Sprite(
					backgroundSceneryXOffset,
					backgroundYOffset,
					SCENERY_HEIGHT,
					SCENERY_WIDTH)
				else -> null
			}

			val sceneryYOffset: Double = when (area.header.scenery) {
				AreaHeader.Scenery.CLOUDS -> Skin.SceneryOffset.CLOUDS.y
				AreaHeader.Scenery.HILLS -> Skin.SceneryOffset.HILLS.y
				AreaHeader.Scenery.FENCES -> Skin.SceneryOffset.HILLS.y
				else -> 0.0
			}

			val scenery: Sprite? = when (area.header.scenery) {
				AreaHeader.Scenery.NONE -> null
				else ->	Sprite(
					backgroundSceneryXOffset,
					sceneryYOffset,
					SCENERY_HEIGHT,
					SCENERY_WIDTH
				)
			}

			return Skin(
				backgroundColor,
				background,
				foregroundA,
				foregroundB,
				scenery
			)
		}
	}
}