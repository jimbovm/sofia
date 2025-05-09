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

import javafx.scene.image.Image
import javafx.scene.paint.Color

import com.github.jimbovm.isobel.common.Area
import com.github.jimbovm.isobel.common.Area.Environment
import com.github.jimbovm.isobel.common.AreaHeader.Scenery
import com.github.jimbovm.isobel.common.AreaHeader.Background
import com.github.jimbovm.isobel.common.AreaHeader.Platform

/**
 * A skin is a collection of assets for rendering an area based on that 
 * area's properties.
 */
class Skin {

	/** Create a new Skin from the properties of an Area object. */
	constructor(area: Area) {
		this.environment = area.environment
		this.scenery = area.header.scenery
		this.background = area.header.background
		this.defaultBackground = area.header.background
		this.platform = area.header.platform
	}

	/** The area environment. Influences, along with the background and platform settings, how elements are rendered. */
	var environment: Environment = Environment.OVERWORLD

	/** The current type of scenery to render. */
	var scenery: Scenery = Scenery.HILLS
	set(scenery) {
		field = scenery
		this.scenerySheet = pickScenerySheet()
	}

	/** The current background style to render. */
	var background: Background = Background.NONE
	set(background) {
		field = if (background in listOf(Background.CASTLE_WALL, Background.UNDERWATER, Background.OVER_WATER)) {
			background
		} else {
			defaultBackground
		}
	}

	/** Caches the default background from the header. */
	var defaultBackground: Background = this.background
	private set

	/** How extensible platforms should be rendered. */
	var platform: Platform = Platform.TREE

	/**
	 * Return the correct scenery sheet given the scenery
	 * and environment settings.
	 *
	 * @return An Image of scenery.
	 */
	var scenerySheet: Image? = null
	private set
	get(): Image? {
		return pickScenerySheet()
	}

	/**
	 * Return the correct background sheet given the background
	 * and environment settings.
	 *
	 * @return An Image of background imagery.
	 */
	var backgroundSheet: Image? = null
	private set
	get(): Image? {
		val fileSource = when {
			this.background in listOf(
				Background.NONE,
				Background.NIGHT,
				Background.MONOCHROME,
				Background.DAY_SNOW,
				Background.NIGHT_SNOW) -> return null
			else -> "img/graphics/background/smb_${this.environment.name}_${this.background.name}.png"
		}
		return Image(ClassLoader.getSystemResourceAsStream(fileSource))
	}

	/**
	 * Retrieve the correct scenery imagery with regard to the current settings.
	 *
	 * @return An Image of scenery imagery.
	 */
	private fun pickScenerySheet(): Image? {
		val fileSource = when {
			this.scenery == Scenery.NONE -> return null
			this.defaultBackground == Background.DAY_SNOW ->
				"img/graphics/scenery/smb_SNOW_${this.scenery.name}.png"
			this.defaultBackground == Background.NIGHT_SNOW ->
				"img/graphics/scenery/smb_SNOW_${this.scenery.name}.png"
			(this.environment == Environment.OVERWORLD) && (this.platform == Platform.MUSHROOM) ->
				"img/graphics/scenery/smb_MUSHROOM_${this.scenery.name}.png"
			else -> "img/graphics/scenery/smb_${this.environment.name}_${this.scenery.name}.png"
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
	private set
	get(): Image {
		// check for variant overworld palettes
		val fileSource = if (this.environment == Environment.OVERWORLD) {
			when {
				this.platform == Platform.MUSHROOM ->
					"img/graphics/foreground/smb_sprites_MUSHROOM.png"
				this.defaultBackground == Background.DAY_SNOW ->
					"img/graphics/foreground/smb_sprites_SNOW.png"
				this.defaultBackground == Background.NIGHT_SNOW ->
					"img/graphics/foreground/smb_sprites_SNOW.png"
				else -> "img/graphics/foreground/smb_sprites_OVERWORLD.png"
			}
		} else {
			"img/graphics/foreground/smb_sprites_${this.environment.name}.png"
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
	private set
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
	private set
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
	private set
	get(): Sprite {
		val tile = if (this.platform == Platform.CLOUD) {
			Sprite.Metatile.BLANK.sprite
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
	private set
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
	private set
	get(): Color {
		val color = when {
			environment in listOf(Environment.UNDERGROUND, Environment.CASTLE) -> Color.BLACK
			defaultBackground in listOf(Background.NIGHT, Background.NIGHT_SNOW, Background.MONOCHROME) -> Color.BLACK
			else -> Color.web(Skin.BLUE_SKY)
		}
		return color
	}

	companion object {

		/** The width in pixels of a page of the play screen. */
		const val SCREEN_WIDTH = 256.0

		/** The height in pixels of the play screen. */
		const val SCREEN_HEIGHT = 240.0

		/** The height of a single "block", or metatile. */
		const val BLOCK_SIZE = 16

		/** The width in pixels of a sheet of scenery imagery. */
		const val SCENERY_WIDTH = 768.0

		/** The colour of the day sky. */
		private const val BLUE_SKY = "#6c6aff"
	}
}