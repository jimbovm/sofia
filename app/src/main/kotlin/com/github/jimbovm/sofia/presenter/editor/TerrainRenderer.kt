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

package com.github.jimbovm.sofia.presenter.editor;

import kotlin.math.max

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
public class TerrainRenderer {

	companion object {
	
		public final val SPRITES = mapOf(
			"overgroundTerrain" to (Pair(0.0, 64.0)),
			"overgroundBrick" to (Pair(32.0, 64.0)),
			"undergroundTerrain" to (Pair(0.0, 128.0)),
			"undergroundBrick" to (Pair(32.0, 128.0)),
			"castleBrick" to (Pair(0.0, 240.0)),
			"underwaterTerrain" to (Pair(0.0, 32.0)),
			"cloud" to (Pair(144.0, 64.0))
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

	/** The Isobel area class(render. */
	private var area: Area = Area()

	/** The canvas onto which level data is rendered. */
	private final var canvas: Canvas? = null

	/** The number of pages (16-block wide screens) to be rendered. */
	private var pages: Int = 0

	/** Create a new renderer object. */
	constructor(canvas: Canvas?, area: Area = Area()) {
		this.canvas = canvas
		this.pages = this.calculatePages(area)
		this.area = area
	}

	private fun calculatePages(area: Area): Int {

		val rightmostGeographyActorX: Int = area.geography.fold(0, {acc: Int, a: Actor -> max(a.x, acc)})
		val rightmostPopulationActorX: Int = area.population.fold(0, {acc: Int, a: Actor -> max(a.x, acc)})
		val rightmostActor = intArrayOf(rightmostGeographyActorX, rightmostPopulationActorX).max()

		return (rightmostActor / 16) + 1
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

	private fun drawColumn(spriteSheet: Image, fill: TerrainRenderer.Fill, column: Int): Unit {

		// get set of blocks to draw per column for this fill type, then draw them
		fill.blocks.forEach {

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

	public fun render(spriteSheet: Image): Unit {

		// draw first column with initial fill
		var currentFill: TerrainRenderer.Fill = TerrainRenderer.Fill.from(this.area.header.fill)
		drawColumn(spriteSheet, currentFill ?: TerrainRenderer.Fill.FILL_ALL, 0)

		// pop first actor
		val actors: Deque<GeographyActor> = ArrayDeque<GeographyActor>(area.geography)
		var currentActor: GeographyActor? = actors.poll()

		val finalColumn = (this.pages * 16)

		for (column in (1..finalColumn)) {

			if ((currentActor is FillSceneryModifier) && (currentActor?.x == column)) {
				currentFill = Fill.from(currentActor.fill) ?: Fill.FILL_ALL
				currentActor = actors.poll()
			}

			drawColumn(spriteSheet, currentFill, column)
		}
	}

	public fun demo() {
		
		area = Area()

		area.header = AreaHeader().apply {
			background = AreaHeader.Background.NONE
			fill = AreaHeader.Fill.FILL_ALL
			platform = AreaHeader.Platform.TREE
			scenery = AreaHeader.Scenery.NONE
			startPosition = AreaHeader.StartPosition.BOTTOM
			ticks = 400
		}
		area.apply {
			environment = Area.Environment.UNDERGROUND
			geography = ArrayList<GeographyActor>().apply {
				add(FillSceneryModifier.create(1, AreaHeader.Fill.FILL_2BF_0BC, AreaHeader.Scenery.NONE))
				add(FillSceneryModifier.create(5, AreaHeader.Fill.FILL_2BF_1BC, AreaHeader.Scenery.NONE))
				add(FillSceneryModifier.create(11, AreaHeader.Fill.FILL_0BF_1BC, AreaHeader.Scenery.NONE))
				add(FillSceneryModifier.create(13, AreaHeader.Fill.FILL_2BF_1BC, AreaHeader.Scenery.NONE))
				add(FillSceneryModifier.create(15, AreaHeader.Fill.FILL_0BF_1BC, AreaHeader.Scenery.NONE))
				add(FillSceneryModifier.create(17, AreaHeader.Fill.FILL_2BF_1BC, AreaHeader.Scenery.NONE))
				add(FillSceneryModifier.create(18, AreaHeader.Fill.FILL_0BF_1BC, AreaHeader.Scenery.NONE))
				add(FillSceneryModifier.create(21, AreaHeader.Fill.FILL_ALL, AreaHeader.Scenery.NONE))
			}
		}

		this.area = area
		this.pages = calculatePages(area)

		val skin = Skin.of(area)

		canvas?.apply {
			width = 256.0 * pages
			height = 240.0
		}

		canvas?.graphicsContext2D?.apply {
			fill = skin.backgroundColor
			fillRect(0.0, 0.0, canvas.width, canvas.height)
		}

		val spriteSheet = Image(ClassLoader.getSystemResourceAsStream("img/smb_sprite.png"))
		this.render(spriteSheet)
	}
}