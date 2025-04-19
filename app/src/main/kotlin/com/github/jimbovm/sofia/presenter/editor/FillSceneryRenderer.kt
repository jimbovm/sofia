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

import kotlin.Int

import java.util.ArrayDeque
import java.util.ArrayList
import java.util.Deque

import javafx.scene.canvas.Canvas
import javafx.scene.image.Image
import javafx.scene.paint.Color

import com.github.jimbovm.isobel.actor.PageSkip
import com.github.jimbovm.isobel.actor.geography.FillSceneryModifier
import com.github.jimbovm.isobel.actor.Actor
import com.github.jimbovm.isobel.actor.geography.GeographyActor
import com.github.jimbovm.isobel.actor.population.PopulationActor
import com.github.jimbovm.isobel.common.Area
import com.github.jimbovm.isobel.common.Area.Environment
import com.github.jimbovm.isobel.common.AreaHeader
import com.github.jimbovm.isobel.common.AreaHeader.Fill
import com.github.jimbovm.isobel.common.AreaHeader.Scenery

import com.github.jimbovm.sofia.presenter.editor.Skin

/**
 * Encapsulates functionality for rendering terrain fill actors in Sofia's editor.
 */
public class FillSceneryRenderer : Renderer {

	companion object {

		/** Specifies which blocks to fill for each terrain fill scheme. */
		private final enum class Fill(val blocks: Set<Int>) {

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

				public fun from(fill: AreaHeader.Fill): Fill {
					return Fill.valueOf(fill.name.toString())
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
			Skin.SCREEN_HEIGHT.toDouble(),	// render entire slice in one pass
			column * Skin.BLOCK_SIZE.toDouble(),
			0.0,				// ditto for destination
			Skin.BLOCK_SIZE.toDouble(),
			Skin.SCREEN_HEIGHT.toDouble()
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

	public override fun render(): Unit {

		var currentFill: AreaHeader.Fill = this.area.header.fill
		var currentScenery: AreaHeader.Scenery = this.area.header.scenery

		val actors: Deque<GeographyActor> = ArrayDeque<GeographyActor>(area.geography.filter({ it is FillSceneryModifier }))
		var currentActor: GeographyActor? = actors.poll()
		var sceneryXOffset: Double = 0.0

		val finalColumn = (canvas.width / 16).toInt()

		for (column in (0..finalColumn)) {
			
			if ((currentActor is FillSceneryModifier) && (currentActor?.x == column)) {
				currentFill = currentActor.fill
				
				val newScenery = currentActor.scenery
				
				if (newScenery != currentScenery) {
					sceneryXOffset = 0.0
					this.skin.scenery = newScenery
					currentScenery = newScenery
				}
				currentActor = actors.poll()
			}

			sceneryXOffset = (column * Skin.BLOCK_SIZE) % Skin.SCENERY_WIDTH
			this.drawScenery(currentScenery, column, sceneryXOffset)
			this.drawFill(currentFill, column)
		}
	}
}