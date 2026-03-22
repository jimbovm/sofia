/*
 * SPDX-License-Identifier: MIT-0
 *
 * This file is part of Sofia (https://github.com/jimbovm/sofia).
 */

package io.github.jimbovm.sofia.editor

import javafx.application.Platform
import javafx.scene.canvas.Canvas

import io.github.jimbovm.isobel.actor.geography.BackgroundModifier
import io.github.jimbovm.isobel.actor.geography.Column
import io.github.jimbovm.isobel.actor.geography.ExtensiblePlatform
import io.github.jimbovm.isobel.actor.geography.FixedExtensible
import io.github.jimbovm.isobel.actor.geography.FixedStatic
import io.github.jimbovm.isobel.actor.geography.GeographyActor
import io.github.jimbovm.isobel.actor.geography.Row
import io.github.jimbovm.isobel.actor.geography.SingletonObject
import io.github.jimbovm.isobel.actor.geography.Staircase
import io.github.jimbovm.isobel.actor.geography.UprightPipe
import io.github.jimbovm.isobel.actor.population.Character
import io.github.jimbovm.isobel.common.Area

import io.github.jimbovm.sofia.editor.ColumnRenderer

class AreaRenderer {

	private val area: Area
	private val canvas: Canvas
	private val skin: Skin

	private val backgroundFillSceneryRenderer: BackgroundFillSceneryRenderer
	private val characterRenderer: CharacterRenderer
	private val startPositionRenderer: StartPositionRenderer
	private val staircaseRenderer: StaircaseRenderer
	private val extensiblePlatformRenderer: ExtensiblePlatformRenderer
	private val fixedStaticRenderer: FixedStaticRenderer
	private val singletonObjectRenderer: SingletonObjectRenderer
	private val textRenderer: TextRenderer
	private val uprightPipeRenderer: UprightPipeRenderer
	private val rowRenderer: RowRenderer
	private val columnRenderer: ColumnRenderer

	constructor(area: Area, skin: Skin, canvas: Canvas) {
		this.area = area
		this.skin = skin
		this.canvas = canvas
		this.backgroundFillSceneryRenderer = BackgroundFillSceneryRenderer(canvas, area, skin)
		this.startPositionRenderer = StartPositionRenderer(canvas, area, skin)
		this.characterRenderer = CharacterRenderer(canvas, area, skin)
		this.staircaseRenderer = StaircaseRenderer(canvas, area, skin)
		this.extensiblePlatformRenderer = ExtensiblePlatformRenderer(canvas, area, skin)
		this.singletonObjectRenderer = SingletonObjectRenderer(canvas, area, skin)
		this.fixedStaticRenderer = FixedStaticRenderer(canvas, area, skin)
		this.textRenderer = TextRenderer(canvas, area, skin)
		this.uprightPipeRenderer = UprightPipeRenderer(canvas, area, skin)
		this.rowRenderer = RowRenderer(canvas, area, skin)
		this.columnRenderer = ColumnRenderer(canvas, area, skin)
	}

	fun update() {
		skin.update(area)

		this.canvas.graphicsContext2D.fillRect(0.0, 0.0, this.canvas.height, this.canvas.width)
		this.backgroundFillSceneryRenderer.render()
		this.startPositionRenderer.render()
		this.updateGeography()
		this.updatePopulation()
		textRenderer.apply {
			xOrigin = 200.0
			yOrigin = 16.0
			text = "TIME\n " + String.format("%03d", area.header.ticks)
			render()
		}
	}

	fun updateGeography() {
		for (geographyActor in area.geography) {
			when (geographyActor) {
				is Staircase -> this.render(geographyActor)
				is ExtensiblePlatform -> this.render(geographyActor)
				is FixedStatic -> this.render(geographyActor)
				is SingletonObject -> this.render(geographyActor)
				is UprightPipe -> this.render(geographyActor)
				is Row -> this.render(geographyActor)
				is FixedExtensible -> this.render(geographyActor)
				is Column -> this.render(geographyActor)
			}
		}
	}

	fun updatePopulation() {
		for (populationActor in area.population) {
			when (populationActor) {
				is Character -> this.render(populationActor)
			}
		}
	}

	fun render(fixedExtensible: FixedExtensible) {
		this.rowRenderer.render(fixedExtensible)
	}

	fun render(row: Row) {
		this.rowRenderer.render(row)
	}

	fun render(extensiblePlatform: ExtensiblePlatform) {
		this.extensiblePlatformRenderer.render(extensiblePlatform)
	}

	fun render(staircase: Staircase) {
		this.staircaseRenderer.render(staircase)
	}

	fun render(fixedStatic: FixedStatic) {
		this.fixedStaticRenderer.render(fixedStatic)
	}

	fun render(singletonObject: SingletonObject) {
		this.singletonObjectRenderer.render(singletonObject)
	}

	fun render(character: Character) {
		this.characterRenderer.render(character)
	}

	fun render(pipe: UprightPipe) {
		this.uprightPipeRenderer.render(pipe)
	}

	fun render(column: Column) {
		this.columnRenderer.render(column)
	}
}
