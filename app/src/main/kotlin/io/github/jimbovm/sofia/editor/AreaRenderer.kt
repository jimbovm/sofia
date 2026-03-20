/*
 * SPDX-License-Identifier: MIT-0
 *
 * This file is part of Sofia (https://github.com/jimbovm/sofia).
 */

package io.github.jimbovm.sofia.editor

import io.github.jimbovm.isobel.actor.geography.BackgroundModifier
import io.github.jimbovm.isobel.actor.geography.ExtensiblePlatform
import io.github.jimbovm.isobel.actor.geography.FixedStatic
import io.github.jimbovm.isobel.actor.geography.GeographyActor
import io.github.jimbovm.isobel.actor.geography.SingletonObject
import io.github.jimbovm.isobel.actor.geography.Staircase
import io.github.jimbovm.isobel.actor.population.Character
import io.github.jimbovm.isobel.common.Area
import javafx.scene.canvas.Canvas
import javafx.application.Platform

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

	constructor(area: Area, skin: Skin, canvas: Canvas) {
		this.area = area
		this.skin = skin
		this.canvas = canvas
		this.backgroundFillSceneryRenderer = BackgroundFillSceneryRenderer(canvas, area)
		this.startPositionRenderer = StartPositionRenderer(canvas, area)
		this.characterRenderer = CharacterRenderer(canvas, area)
		this.staircaseRenderer = StaircaseRenderer(canvas, area)
		this.extensiblePlatformRenderer = ExtensiblePlatformRenderer(canvas, area)
		this.singletonObjectRenderer = SingletonObjectRenderer(canvas, area)
		this.fixedStaticRenderer = FixedStaticRenderer(canvas, area)
		this.textRenderer = TextRenderer(canvas, area)
	}

	fun update() {
		this.backgroundFillSceneryRenderer.reskin()
		this.startPositionRenderer.reskin()
		this.characterRenderer.reskin()
		this.staircaseRenderer.reskin()
		this.extensiblePlatformRenderer.reskin()
		this.singletonObjectRenderer.reskin()
		this.fixedStaticRenderer.reskin()
		this.textRenderer.reskin()

		this.canvas.graphicsContext2D.fillRect(0.0, 0.0, this.canvas.height, this.canvas.width)
		this.backgroundFillSceneryRenderer.render()
		this.startPositionRenderer.render()
		this.updateGeography()
		this.updatePopulation()
		textRenderer.apply {
			xOrigin = 216.0
			yOrigin = 16.0
			text = "TIME\n ${area.header.ticks}"
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
}
