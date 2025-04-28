package com.github.jimbovm.sofia.presenter.editor

import com.github.jimbovm.isobel.actor.Extensible
import com.github.jimbovm.isobel.actor.YPlaceable
import java.util.ArrayDeque
import java.util.Deque

import com.github.jimbovm.isobel.actor.geography.BackgroundModifier
import com.github.jimbovm.isobel.actor.geography.ExtensiblePlatform
import com.github.jimbovm.isobel.actor.geography.FillSceneryModifier
import com.github.jimbovm.isobel.actor.geography.GeographyActor
import javafx.scene.canvas.Canvas

import com.github.jimbovm.isobel.common.Area
import com.github.jimbovm.isobel.common.AreaHeader.Platform

import com.github.jimbovm.sofia.presenter.editor.Renderer
import com.github.jimbovm.sofia.presenter.editor.Sprite

class ExtensiblePlatformRenderer : Renderer {

	constructor(canvas: Canvas, area: Area) : super(canvas, area)

	private fun drawCannon(x: Int, y: Int, extent: Int) {

		for (section in 0..extent) {
			when (section) {
				0 -> drawSprite(Sprite.Metatile.CANNON_TOP.sprite, x, y)
				1 -> drawSprite(Sprite.Metatile.CANNON_UPPER_BODY.sprite, x, y + section)
				2 -> drawSprite(Sprite.Metatile.CANNON_LOWER_BODY.sprite, x, y + section)
				else -> drawSprite(Sprite.Metatile.CANNON_BASE.sprite, x, y + section)
			}
		}
	}

	private fun drawShroom(x: Int, y: Int, extent: Int) {

		val stipeOriginX = x + ((extent + 1) / 2)
		val stipeOriginY = y + 1
		if (extent > 1) {
			for (row in stipeOriginY..15) {
				when (row) {
					stipeOriginY -> drawSprite(
						Sprite.Metatile.STIPE_RING.sprite, stipeOriginX, row)
					else -> drawSprite(
						Sprite.Metatile.STIPE.sprite,stipeOriginX, row)
				}
			}
		}
		drawCap(x, y, extent,
			Sprite.Metatile.MUSHROOM_LEFT.sprite,
			Sprite.Metatile.MUSHROOM_MIDDLE.sprite,
			Sprite.Metatile.MUSHROOM_RIGHT.sprite)
	}

	private fun drawTree(x: Int, y: Int, extent: Int) {

		val trunkOriginX = x + 1
		val trunkOriginY = y + 1
		val trunkWidth = extent - 2
		if (extent > 1) {
			for (x in trunkOriginX..(trunkOriginX + trunkWidth)) {
				for (y in trunkOriginY..15) {
					drawSprite(Sprite.Metatile.TREE_TRUNK.sprite, x, y)
				}
			}
		}
		drawCap(x, y, extent,
			Sprite.Metatile.TREE_LEFT.sprite,
			Sprite.Metatile.TREE_MIDDLE.sprite,
			Sprite.Metatile.TREE_RIGHT.sprite
		)
	}

	private fun drawCap(x: Int, y: Int, extent: Int, left: Sprite, middle: Sprite, right: Sprite) {

		for (column in x..(x + extent)) {
			when (column) {
				x -> drawSprite(left, column, y)
				(x + extent) -> drawSprite(right, column, y)
				else -> drawSprite(middle, column, y)
			}
		}
	}

	override fun render() {
		val actors: Deque<GeographyActor> = ArrayDeque<GeographyActor>(
			area.geography.filter({ it is ExtensiblePlatform }))

		var currentActor = actors.poll()

		val finalColumn = (canvas.width / 16).toInt()

		for (column in (0..finalColumn)) {
			while (currentActor?.x == column) {
				when (this.area.header.platform) {
					Platform.MUSHROOM -> this.drawShroom(
						currentActor.x,
						(currentActor as YPlaceable).y,
						(currentActor as Extensible).extent
					)
					Platform.CANNON -> this.drawCannon(
						currentActor.x,
						(currentActor as YPlaceable).y,
						(currentActor as Extensible).extent
						)
					Platform.TREE, Platform.CLOUD -> this.drawTree(
						currentActor.x,
						(currentActor as YPlaceable).y,
						(currentActor as Extensible).extent
					)
				}
				currentActor = actors.poll()
			}
		}
	}
}