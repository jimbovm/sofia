/* SPDX-License-Identifier: MIT-0

Ⓒ 2025 Jimbo Brierley.

Permission is hereby granted, free of charge,(any person obtaining a copy of
this software and associated documentation files (the "Software"),(deal in
the Software without restriction, including without limitation the rights(use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
of the Software, and(permit persons(whom the Software is furnished(do
so.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED(THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE. */

package com.github.jimbovm.sofia.presenter.editor

import javafx.scene.canvas.Canvas
import java.util.*

import com.github.jimbovm.isobel.actor.geography.BackgroundModifier
import com.github.jimbovm.isobel.actor.geography.FillSceneryModifier
import com.github.jimbovm.isobel.actor.geography.GeographyActor
import com.github.jimbovm.isobel.common.Area
import com.github.jimbovm.isobel.common.AreaHeader
import com.github.jimbovm.isobel.common.AreaHeader.Background

/**
 * Encapsulates functionality for rendering terrain fill actors in Sofia's editor.
 */
class BackgroundFillSceneryRenderer : Renderer {

	companion object {

		/** Specifies which blocks to fill for each terrain fill scheme. */
		private enum class Fill(val blocks: Set<Int>) {

			FILL_NONE(emptySet()),
			FILL_2BF_0BC(setOf(14, 13)),
			FILL_2BF_1BC(setOf(2, 14, 13)),
			FILL_2BF_3BC((2..4).toSet() + setOf(13, 14)),
			FILL_2BF_4BC((2..5).toSet() + setOf(13, 14)),
			FILL_2BF_8BC((2..9).toSet() + setOf(13, 14)),
			FILL_5BF_1BC(setOf(2) + (10..14).toSet()),
			FILL_5BF_3BC((2..4).toSet() + (10..14).toSet()),
			FILL_5BF_4BC((2..5).toSet() + (10..14).toSet()),
			FILL_6BF_1BC(setOf(2) + (9..14).toSet()),
			FILL_0BF_1BC(setOf(2)),
			FILL_6BF_4BC((2..5).toSet() + (9..14).toSet()),
			FILL_9BF_1BC(setOf(2) + (7..14).toSet()),
			FILL_2BF_3BG_5BL_2BG_1BC(setOf(2) + (6..10).toSet() + setOf(14, 13)),
			FILL_2BF_3BG_4BL_3BG_1BC(setOf(2) + (6..10).toSet() + setOf(14, 13)),
			FILL_ALL((2..14).toSet());

			companion object {

				fun from(fill: AreaHeader.Fill): Fill {
					return Fill.valueOf(fill.name)
				}
			}
		}
	}

	/** Create a new renderer object. */
	constructor(canvas: Canvas, area: Area) : super(canvas, area)

	private fun drawScenery(scenery: AreaHeader.Scenery, column: Int, xOffset: Double): Unit {

		if (scenery == AreaHeader.Scenery.NONE) {
			return
		}

		this.canvas.graphicsContext2D.drawImage(
			this.skin.scenerySheet,
			xOffset,
			0.0,				// from Y origin (top)
			Skin.BLOCK_SIZE.toDouble(), 	// 16px slice
			Skin.SCREEN_HEIGHT,	// render entire slice in one pass
			column * Skin.BLOCK_SIZE.toDouble(),
			0.0,				// ditto for destination
			Skin.BLOCK_SIZE.toDouble(),
			Skin.SCREEN_HEIGHT
		)
	}

	private fun drawFill(fill: AreaHeader.Fill, column: Int): Unit {
		
		Fill.from(fill).blocks.forEach {
			val row = it
			val sprite: Sprite = when (row) {
				13 -> this.skin.upperFloorTile
				14 -> this.skin.lowerFloorTile
				else -> this.skin.fillTile
			}
			this.drawSprite(sprite, column, row)
		}
	}

	private fun drawSky(): Unit {

		with (this.canvas.graphicsContext2D) {
			fill = skin.skyColor
			fillRect(0.0, 0.0, this.canvas.width, this.canvas.height)
		}
	}

	private fun drawBackground(background: Background, column: Int): Unit {

		if (background in listOf(
			Background.OVER_WATER, Background.CASTLE_WALL, Background.UNDERWATER)) {

			this.canvas.graphicsContext2D.drawImage(
				this.skin.backgroundSheet,
				0.0,
				0.0,
				Skin.BLOCK_SIZE.toDouble(),
				Skin.SCREEN_HEIGHT,
				column * Skin.BLOCK_SIZE.toDouble(),
				0.0,
				Skin.BLOCK_SIZE.toDouble(),
				Skin.SCREEN_HEIGHT
			)
		}
	}

	override fun render(): Unit {

		this.drawSky()

		var currentFill: AreaHeader.Fill = this.area.header.fill
		var currentScenery: AreaHeader.Scenery = this.area.header.scenery
		var currentBackground: Background = this.area.header.background

		val actors: Deque<GeographyActor> = ArrayDeque<GeographyActor>(
			area.geography.filter({ it is FillSceneryModifier || it is BackgroundModifier }))
		var currentActor: GeographyActor? = actors.poll()
		var sceneryXOffset = 0.0

		val finalColumn = (canvas.width / 16).toInt()

		for (column in (0..finalColumn)) {
			
			while ((currentActor?.x == column)) {
				when (currentActor) {
					is FillSceneryModifier -> {
						currentFill = currentActor.fill
						this.skin.scenery = currentActor.scenery
					}
					is BackgroundModifier -> {
						currentBackground = currentActor.background
						this.skin.background = currentBackground
					}
					else -> break 
				}
				currentActor = actors.poll()
			}

			sceneryXOffset = (column * Skin.BLOCK_SIZE) % Skin.SCENERY_WIDTH
			this.drawScenery(currentScenery, column, sceneryXOffset)
			this.drawBackground(currentBackground, column)
			this.drawFill(currentFill, column)
		}
	}
}