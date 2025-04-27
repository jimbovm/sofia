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
import com.github.jimbovm.isobel.common.AreaHeader

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

	override fun render() {
		val actors: Deque<GeographyActor> = ArrayDeque<GeographyActor>(
			area.geography.filter({ it is ExtensiblePlatform }))

		var currentActor = actors.poll()

		val finalColumn = (canvas.width / 16).toInt()

		for (column in (0..finalColumn)) {

			while (currentActor?.x == column) {

				this.drawCannon(
					currentActor.x,
					(currentActor as YPlaceable).y,
					(currentActor as Extensible).extent
				)
				currentActor = actors.poll()
			}
		}
	}
}