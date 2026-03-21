/*
 * SPDX-License-Identifier: MIT-0
 *
 * This file is part of Sofia (https://github.com/jimbovm/sofia).
 */

package io.github.jimbovm.sofia.editor

import io.github.jimbovm.isobel.actor.geography.BackgroundModifier
import io.github.jimbovm.isobel.actor.geography.FillSceneryModifier
import io.github.jimbovm.isobel.actor.geography.GeographyActor
import io.github.jimbovm.isobel.common.Area
import io.github.jimbovm.isobel.common.AreaHeader
import io.github.jimbovm.isobel.common.AreaHeader.Background
import javafx.scene.canvas.Canvas
import java.util.*

/**
 * Encapsulates functionality for rendering terrain fill actors in Sofia's editor.
 */
class BackgroundFillSceneryRenderer : BaseRenderer {

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
			FILL_ALL((2..14).toSet()),
			;

			companion object {

				fun from(fill: AreaHeader.Fill): Fill = Fill.valueOf(fill.name)
			}
		}
	}

	/** Create a new renderer object. */
	constructor(canvas: Canvas, area: Area, skin: Skin) : super(canvas, area, skin)

	private fun drawScenery(scenery: AreaHeader.Scenery, column: Int, xOffset: Double) {
		if (scenery == AreaHeader.Scenery.NONE) {
			return
		}

		this.canvas.graphicsContext2D.drawImage(
			this.skin.scenerySheet,
			xOffset,
			0.0, // from Y origin (top)
			Skin.BLOCK_SIZE.toDouble(), // 16px slice
			Skin.SCREEN_HEIGHT, // render entire slice in one pass
			column * Skin.BLOCK_SIZE.toDouble(),
			0.0, // ditto for destination
			Skin.BLOCK_SIZE.toDouble(),
			Skin.SCREEN_HEIGHT,
		)
	}

	private fun drawFill(fill: AreaHeader.Fill, column: Int) {
		Fill.from(fill).blocks.forEach {
			val sprite: Sprite = when (it) {
				13 -> this.skin.upperFloorTile
				14 -> this.skin.lowerFloorTile
				else -> this.skin.fillTile
			}
			this.drawSprite(sprite, column, it)
		}
	}

	private fun drawSky() {
		with(this.canvas.graphicsContext2D) {
			fill = skin.skyColor
			fillRect(0.0, 0.0, this.canvas.width, this.canvas.height)
		}
	}

	private fun drawBackground(background: Background, column: Int) {
		if (background in listOf(
				Background.OVER_WATER,
				Background.CASTLE_WALL,
				Background.UNDERWATER,
			)
		) {
			this.canvas.graphicsContext2D.drawImage(
				this.skin.backgroundSheet,
				0.0,
				0.0,
				Skin.BLOCK_SIZE.toDouble(),
				Skin.SCREEN_HEIGHT,
				column * Skin.BLOCK_SIZE.toDouble(),
				0.0,
				Skin.BLOCK_SIZE.toDouble(),
				Skin.SCREEN_HEIGHT,
			)
		}
	}

	fun render() {
		this.drawSky()

		var currentFill: AreaHeader.Fill = this.area.header.fill
		var currentScenery: AreaHeader.Scenery = this.area.header.scenery
		var currentBackground: Background = this.area.header.background

		val actors: Deque<GeographyActor> =
			ArrayDeque<GeographyActor>(area.geography.filter { it is FillSceneryModifier || it is BackgroundModifier })
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
