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
class Skin {

	constructor(area: Area) {
		this.environment = area.environment
		this.scenery = area.header.scenery
		this.background = area.header.background
		this.platform = area.header.platform
	}

	var environment: Environment = Environment.OVERWORLD
	var scenery: Scenery = Scenery.HILLS
	var background: Background = Background.NONE
	var platform: Platform = Platform.TREE

	/**
	 * Return the correct scenery sheet given the scenery
	 * and environment.
	 *
	 * @return An Image of scenery.
	 */
	var scenerySheet: Image? = null
	get(): Image? {
		val fileSource = when {
			this.scenery == Scenery.NONE -> return null
			this.platform == Platform.MUSHROOM -> "img/graphics/scenery/smb_MUSHROOM_${this.scenery.name.toString()}.png"
			this.background in listOf(Background.DAY_SNOW,
						 Background.NIGHT_SNOW) -> "img/graphics/scenery/smb_SNOW_${this.scenery.name.toString()}.png"
			else -> "img/graphics/scenery/smb_${this.environment.name.toString()}_${this.scenery.name.toString()}.png"
		}
		return Image(ClassLoader.getSystemResourceAsStream(fileSource))
	}

	var backgroundSheet: Image? = null
	get(): Image? {
		val fileSource = when {
			this.background == Background.NONE -> return null
			this.environment in listOf(
				Environment.CASTLE,
				Environment.UNDERWATER) -> "img/graphics/background/smb_${this.environment.name.toString()}_${this.background.name.toString()}.png"
			else -> "img/graphics/background/smb_OTHER_${this.background.name.toString()}.png"
		}
		return Image(ClassLoader.getSystemResourceAsStream(fileSource))
	}

	/**
	 * Return the sprite sheet for rendering foreground objects given the 
	 * environment and background settings.
	 *
	 * @return An Image of a sprite sheet of foreground objects.
	 */
	var spriteSheet: Image = Image("img/graphics/foreground/smb_sprites_OVERWORLD.png")
	get(): Image {
		// check for variant overworld palettes
		val fileSource = if (this.environment == Environment.OVERWORLD) {
			when {
				this.platform == Platform.MUSHROOM -> "img/graphics/foreground/smb_sprites_MUSHROOM.png"
				this.background == Background.DAY_SNOW -> "img/graphics/foreground/smb_sprites_SNOW.png"
				this.background == Background.NIGHT_SNOW -> "img/graphics/foreground/smb_sprites_SNOW.png"
				else -> "img/graphics/foreground/smb_sprites_OVERWORLD.png"
			}
		} else {
			"img/graphics/foreground/smb_sprites_${this.environment.name.toString()}.png"
		}

		return Image(ClassLoader.getSystemResourceAsStream(fileSource))
	}

	/**
	 * Return the correct tile for rendering terrain fill higher than
	 * floor level given the environment.
	 *
	 * @return A Sprite.
	 */
	var fillTile: Sprite = Sprite.Metatile.BLANK.sprite
	get(): Sprite { 
		val tile = when (this.environment) {
			Environment.UNDERWATER -> Sprite.Metatile.SEAFLOOR.sprite
			Environment.OVERWORLD -> Sprite.Metatile.GROUND.sprite
			Environment.UNDERGROUND -> Sprite.Metatile.BRICK.sprite
			Environment.CASTLE -> Sprite.Metatile.CASTLE_BRICK.sprite
		}
		return tile
	}

	/**
	 * Return the correct tile for rendering the upper floor tile
	 * (at Y=13) given the environment.
	 *
	 * @return A Sprite specifying a metatile.
	 */
	var upperFloorTile: Sprite = Sprite.Metatile.BLANK.sprite
	get(): Sprite {  
		val tile = if (this.platform == Platform.CLOUD) {
			Sprite.Metatile.CLOUD.sprite
		}
		else {
			this.lowerFloorTile
		}
		return tile
	}

	/**
	 * Return the correct tile for rendering the lower floor tile
	 * (at Y=14) given the environment.
	 *
	 * @return A Sprite specifying a metatile.
	 */
	var lowerFloorTile: Sprite = Sprite.Metatile.BLANK.sprite
	get(): Sprite {
		val tile = if (this.platform == Platform.CLOUD) {
			Sprite.Metatile.CLOUD.sprite
		}
		else when (this.environment) {
			Environment.UNDERWATER -> Sprite.Metatile.SEAFLOOR.sprite
			Environment.OVERWORLD -> Sprite.Metatile.GROUND.sprite
			Environment.UNDERGROUND -> Sprite.Metatile.GROUND.sprite
			Environment.CASTLE -> Sprite.Metatile.CASTLE_BRICK.sprite
		}
		return tile
	}

	/**
	 * Return the correct tile for rendering the "column of bricks"
	 * actor given the environment.
	 *
	 * @return A Sprite specifying a metatile.
	 */
	var brickTile: Sprite = Sprite.Metatile.BLANK.sprite
	get(): Sprite {
		val tile = when (this.environment) {
			Environment.UNDERWATER -> Sprite.Metatile.CORAL.sprite
			else -> Sprite.Metatile.BRICK.sprite
		}
		return tile
	}

	/**
	 * Return the correct "sky" colour (base screen colour before scenery
	 * or background is rendered on top) given the environment and 
	 * background settings.
	 *
	 * @return A Color.
	 */
	var skyColor: Color = Color.BLACK
	get(): Color { 
		val color = when {
			environment in listOf(Environment.UNDERGROUND, Environment.CASTLE) -> Color.BLACK
			background in listOf(Background.NIGHT, Background.NIGHT_SNOW, Background.MONOCHROME) -> Color.BLACK
			else -> Color.web(Skin.BLUE_SKY)
		}
		return color
	}

	companion object {

		public val SCREEN_WIDTH = 256.0
		public val SCREEN_HEIGHT = 240.0
		public val BLOCK_SIZE = 16
		public val SCENERY_WIDTH = 768.0
		
		private val SPRITE_SHEET_WIDTH = 1330.0
		private val SPRITE_SHEET_HEIGHT = 64.0

		private val BLUE_SKY = "#6c6aff"
	}
}