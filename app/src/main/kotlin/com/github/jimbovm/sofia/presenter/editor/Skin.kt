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
import com.github.jimbovm.isobel.common.AreaHeader.Background
import com.github.jimbovm.isobel.common.AreaHeader.Platform

import com.github.jimbovm.sofia.presenter.editor.Sprite

/**
 * A skin is a collection of assets for rendering an area based on that 
 * area's properties.
 */
class Skin(
		val backgroundColor: Color,
		val background: Sprite?,
		val scenery: Sprite?,
		val lowerFloorTile: Sprite,
		val upperFloorTile: Sprite,
		val fillTile: Sprite,
		val liftTile: Sprite,
		val spriteSheet: Image = Image(ClassLoader.getSystemResourceAsStream("img/smb_sprite.png")),
		val backgroundSheet: Image = Image(ClassLoader.getSystemResourceAsStream("img/smb_background.png")),
		val scenerySheet: Image = Image(ClassLoader.getSystemResourceAsStream("img/smb_scenery.png"))
	) {

	public final enum class Palette0VariantOffset(val x: Double) {
		DEFAULT(0.0),
		MUSHROOM(144.0),
		SNOW(288.0)
	}

	public final enum class Palette1Offset(val y: Double) {
		UNDERWATER(0.0),
		OVERWORLD(64.0),
		UNDERGROUND(128.0),
		CASTLE(192.0)
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
		OVER_WATER(0.0),
		UNDERWATER(Skin.SCREEN_HEIGHT as Double),
		CASTLE_WALL(Skin.SCREEN_HEIGHT * 2.0),
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
		HILLS(0.0),
		FENCES(SCREEN_HEIGHT),
		CLOUDS(SCREEN_HEIGHT * 2),
	}

	companion object {

		public val SCREEN_WIDTH = 256.0
		public val SCREEN_HEIGHT = 240.0
		public val BLOCK_SIZE = 16
		public val SPRITE_FOREGROUND_PALETTE_1_WIDTH = 160.0
		public val SPRITE_FOREGROUND_PALETTE_1_HEIGHT = 64.0
		public val SPRITE_FOREGROUND_PALETTE_0_WIDTH = 144.0
		public val SPRITE_FOREGROUND_PALETTE_0_HEIGHT = 64.0
		public val SCENERY_WIDTH = 768.0
		
		private val SPRITE_SHEET_WIDTH = 1330.0
		private val SPRITE_SHEET_HEIGHT = 64.0

		private val BLUE_SKY = "#6c6aff"

		fun of(area: Area): Skin {

			val isNight: Boolean = area.header.background in listOf(Background.NIGHT, Background.NIGHT_SNOW)
			val useSnowPalette: Boolean = area.header.background in listOf(Background.DAY_SNOW, Background.NIGHT_SNOW)
			val useMushroomPalette: Boolean = area.header.platform == Platform.MUSHROOM
			val useMonochromePalette: Boolean = area.header.background == Background.MONOCHROME

			val backgroundColor = when {
				isNight || useMonochromePalette -> Color.BLACK
				else -> Skin.BackgroundColor.valueOf(area.environment.name.toString()).color
			}

			val backgroundSceneryXOffset = when {
				useMushroomPalette -> Skin.BackgroundSceneryOffset.MUSHROOM.x
				useSnowPalette -> Skin.BackgroundSceneryOffset.SNOW.x
				useMonochromePalette -> Skin.BackgroundSceneryOffset.MONOCHROME.x
				else -> SCENERY_WIDTH * area.environment.id
			}

			val backgroundYOffset = when (area.header.background) {
				Background.UNDERWATER,
				Background.CASTLE_WALL,
				Background.OVER_WATER -> Skin.BackgroundOffset.valueOf(area.header.background.toString()).y
				else -> 0.0
			}

			val background: Sprite? = when (area.header.background) {
				Background.UNDERWATER,
				Background.CASTLE_WALL,
				Background.OVER_WATER -> Sprite(
					backgroundSceneryXOffset,
					backgroundYOffset,
					SCREEN_HEIGHT,
					SCENERY_WIDTH)
				else -> null
			}

			val sceneryYOffset: Double = when (area.header.scenery) {
				AreaHeader.Scenery.CLOUDS -> Skin.SceneryOffset.CLOUDS.y
				AreaHeader.Scenery.HILLS -> Skin.SceneryOffset.HILLS.y
				AreaHeader.Scenery.FENCES -> Skin.SceneryOffset.FENCES.y
				else -> 0.0
			}

			val scenery: Sprite? = when (area.header.platform) {
				AreaHeader.Platform.CLOUD -> null
				else ->	Sprite(
						backgroundSceneryXOffset,
						sceneryYOffset,
						SCREEN_HEIGHT,
						SCENERY_WIDTH
					)
			}

			return Skin(
				backgroundColor,
				background,
				scenery,
				Sprite.lowerFloorTileOf(area),
				Sprite.upperFloorTileOf(area),
				Sprite.fillTileOf(area),
				Sprite.liftTileOf(area),
			)
		}
	}
}