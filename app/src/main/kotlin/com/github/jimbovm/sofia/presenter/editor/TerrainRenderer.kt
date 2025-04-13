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

import java.util.ArrayDeque
import java.util.ArrayList
import java.util.Deque

import javafx.scene.canvas.Canvas
import javafx.scene.image.Image

import com.github.jimbovm.isobel.actor.PageSkip
import com.github.jimbovm.isobel.actor.geography.FillSceneryModifier
import com.github.jimbovm.isobel.actor.geography.GeographyActor
import com.github.jimbovm.isobel.common.Area
import com.github.jimbovm.isobel.common.Area.Environment
import com.github.jimbovm.isobel.common.AreaHeader
import com.github.jimbovm.isobel.common.AreaHeader.Fill
import com.github.jimbovm.isobel.common.AreaHeader.Scenery

/**
 * Encapsulates functionality for rendering terrain fill actors in Sofia's editor.
 */
public class TerrainRenderer {

	companion object {
		public final val SPRITES = mapOf(
			"overgroundTerrain" to Pair(0.0, 64.0),
			"overgroundBrick" to Pair(32.0, 64.0),
			"undergroundTerrain" to Pair(0.0, 128.0),
			"undergroundBrick" to Pair(32.0, 128.0),
			"castleBrick" to Pair(0.0, 240.0),
			"underwaterTerrain" to Pair(0.0, 32.0),
			"cloud" to Pair(144.0, 64.0)
		)

		public final val FILL_METATILES: Map<Environment, Map<String, Pair<Double, Double>?>> = mapOf(
			Environment.OVERWORLD to mapOf(
				"ground" to SPRITES["overgroundTerrain"],
				"fill" to SPRITES["overgroundTerrain"]),
			Environment.UNDERGROUND to mapOf(
				"ground" to SPRITES["undergroundTerrain"],
				"fill" to SPRITES["undergroundBrick"]),
			Environment.UNDERWATER to mapOf(
				"ground" to SPRITES["underwaterTerrain"],
				"fill" to SPRITES["underwaterTerrain"]),
			Environment.CASTLE to mapOf(
				"ground" to SPRITES["castleBrick"],
				"fill" to SPRITES["castleBrick"])
			)
	}

	/** The Isobel area class to render. */
	private var area: Area = Area()

	/** The canvas onto which level data is rendered. */
	private final var canvas: Canvas?

	/** The number of pages (16-block wide screens) to be rendered. */
	private var pages: Int = 0

	/**
	 * Create a new renderer object.
	 * 
	 */
	constructor(canvas: Canvas?, pages: Int) {
		this.canvas = canvas
		this.pages = pages
	}

	/**
	 * Draw a block sprite at the given coordinates.
	 */
	private fun drawBlock(spriteSheet: Image, spriteX: Double, spriteY: Double, column: Int, row: Int): Unit {

		val cursorX = 16.0 * column
		val cursorY = row * 16.0

		this.canvas?.getGraphicsContext2D()?.drawImage(
			spriteSheet,
			spriteX,
			spriteY,
			16.0,
			16.0,
			cursorX,
			cursorY,
			16.0,
			16.0
		)
	}

	private fun drawColumn(
		spriteSheet: Image,
		fill: Fill,
		renderSpec: Map<Fill, Set<Int>>,
		column: Int): Unit {

		// get set of blocks to draw per column for this fill type, then draw them
		val metatilesToDraw = renderSpec[fill]
		metatilesToDraw?.forEach {

			val row = it

			val groundSprite = FILL_METATILES?.getValue(area.environment)?.getValue("ground") ?: Pair(0.0, 0.0)
			val fillSprite = FILL_METATILES?.getValue(area.environment)?.getValue("fill") ?: Pair(0.0, 0.0)

			val sprite: Pair<Double, Double>? = when (row) {
				13, 14 -> groundSprite
				else -> fillSprite
			}

			val spriteX = sprite?.first ?: 0.0
			val spriteY = sprite?.second ?: 0.0

			this.drawBlock(spriteSheet, spriteX, spriteY, column, row)
		}
	}

	public fun render(spriteSheet: Image, initialFill: Fill): Unit {

		val top = 2
		val bottom = 14
		
		val renderSpec = mapOf<Fill, Set<Int>>(
			Fill.FILL_NONE to emptySet(),
			Fill.FILL_2BF_0BC to setOf(bottom, bottom-1),
			Fill.FILL_2BF_1BC to setOf(top, bottom, bottom-1),
			Fill.FILL_2BF_3BC to
				(top..top+2).toSet() +
				setOf(bottom-1, bottom),
			Fill.FILL_2BF_4BC to
				(top..top+3).toSet() +
				setOf(bottom-1, bottom),
			Fill.FILL_2BF_8BC to
				(top..top+7).toSet() +
				setOf(bottom-1, bottom),
			Fill.FILL_5BF_1BC to
				setOf(top) +
				(bottom-4..bottom).toSet(),
			Fill.FILL_5BF_3BC to
				(top..top+2).toSet() + 
				(bottom-4..bottom).toSet(),
			Fill.FILL_5BF_4BC to 
				(top..top+3).toSet() +
				(bottom-4..bottom).toSet(),
			Fill.FILL_6BF_1BC to
				setOf(top) +
				(bottom-5..bottom).toSet(),
			Fill.FILL_0BF_1BC to setOf(top),
			Fill.FILL_6BF_4BC to 
				(top..top+3).toSet() +
				(bottom-5..bottom).toSet(),
			Fill.FILL_9BF_1BC to
				setOf(top) +
				(bottom-7..bottom).toSet(),
			Fill.FILL_2BF_3BG_5BL_2BG_1BC to
				setOf(top) +
				(top+4..top+8).toSet() + 
				setOf(bottom, bottom-1),
			Fill.FILL_2BF_3BG_4BL_3BG_1BC to
				setOf(top) +
				(bottom-8..bottom-4).toSet() +
				setOf(bottom, bottom-1),
			Fill.FILL_ALL to (top..bottom).toSet()
		)

		// draw first column with initial fill
		var currentFill: Fill = this.area.header.fill
		drawColumn(spriteSheet, currentFill ?: Fill.FILL_2BF_0BC, renderSpec, 0)

		// pop first actor
		val actors: Deque<GeographyActor> = ArrayDeque<GeographyActor>(area.geography)
		var currentActor: GeographyActor? = actors.poll()

		val finalColumn = (this.pages * 16)

		for (column in (1..finalColumn)) {

			if ((currentActor is FillSceneryModifier) && (currentActor?.x == column)) {
				currentFill = currentActor?.fill ?: Fill.FILL_ALL
				currentActor = actors.poll()
			}

			drawColumn(spriteSheet, currentFill, renderSpec, column)
		}
	}

	public fun demo() {
		val area: Area = Area()
		area.header = AreaHeader().apply {
			background = AreaHeader.Background.NONE
			fill = Fill.FILL_ALL
			platform = AreaHeader.Platform.MUSHROOM
			scenery = AreaHeader.Scenery.NONE
			startPosition = AreaHeader.StartPosition.BOTTOM
			ticks = 400
		}
		area.apply {
			environment = Area.Environment.OVERWORLD
			geography = ArrayList<GeographyActor>().apply {
				add(FillSceneryModifier.create(1, Fill.FILL_2BF_0BC, AreaHeader.Scenery.NONE))
				add(FillSceneryModifier.create(5, Fill.FILL_2BF_1BC, AreaHeader.Scenery.NONE))
				add(FillSceneryModifier.create(11, Fill.FILL_0BF_1BC, AreaHeader.Scenery.NONE))
				add(FillSceneryModifier.create(13, Fill.FILL_2BF_1BC, AreaHeader.Scenery.NONE))
				add(FillSceneryModifier.create(15, Fill.FILL_0BF_1BC, AreaHeader.Scenery.NONE))
				add(FillSceneryModifier.create(17, Fill.FILL_2BF_1BC, AreaHeader.Scenery.NONE))
				add(FillSceneryModifier.create(18, Fill.FILL_0BF_1BC, AreaHeader.Scenery.NONE))
				add(FillSceneryModifier.create(21, Fill.FILL_ALL, AreaHeader.Scenery.NONE))
			}
		}

		this.area = area
		val spriteSheet = Image(ClassLoader.getSystemResourceAsStream("img/smb_sprite.png"))
		this.render(spriteSheet, area.header.fill)
	}
}