package io.github.jimbovm.sofia.presenter.editor

import javafx.scene.canvas.Canvas
import javafx.scene.image.Image

import io.github.jimbovm.isobel.common.Area
import io.github.jimbovm.isobel.actor.population.PopulationActor
import io.github.jimbovm.isobel.actor.population.Character

import io.github.jimbovm.sofia.presenter.editor.Sprite

class CharacterRenderer : Renderer {

	constructor(canvas: Canvas, area: Area) : super(canvas, area)

	fun render(character: Character): Unit {
		when (character.type) {
			Character.Type.BLOOBER,
			Character.Type.BOWSER,
			Character.Type.BUZZY_BEETLE,
			Character.Type.GOOMBA,
			Character.Type.GREEN_CHEEP,
			Character.Type.GREEN_PARATROOPA_HOP,
			Character.Type.GREEN_PARATROOPA_HOVER,
			Character.Type.GREEN_TROOPA,
			Character.Type.HAMMER_BRO,
			Character.Type.LAKITU,
			Character.Type.PODOBOO,
			Character.Type.RED_CHEEP,
			Character.Type.RED_PARATROOPA,
			Character.Type.RED_TROOPA,
			Character.Type.SPINY,
			Character.Type.TOAD_PEACH -> {
				this.renderSingleton(character)
			}

			else -> {
				if (character.type.toString().contains("SQUAD", false)) {
					this.renderSquad(character);
				} else if (character.type.toString()
						.contains("FIRE_BAR", false)
				) {
					this.renderFireBar(character)
				} else if (character.type.toString()
						.contains("LIFT", false)
				) {
					this.renderLift(character)
				}
			}
		}
	}

	private fun renderSingleton(character: Character): Unit {

		val characterName = character.type.toString()
		val characterMetatile = Sprite.Metatile.valueOf(characterName)
		this.drawSprite(characterMetatile.sprite, character.x, character.y, 0.0, 1.0)
	}

	private fun renderSquad(squad: Character): Unit {

		val type = squad.type.toString()

		val metatile =
			if (type.startsWith("GOOMBA")) Sprite.Metatile.GOOMBA.sprite else Sprite.Metatile.GREEN_TROOPA.sprite
		val members = if (type.contains("3", false)) 3 else 2
		val y = if (type.contains("Y10", false)) 10 else 6

		for (member in 0..<members) {
			drawSprite(metatile, squad.x, y, member * 23.0, 1.0)
		}
	}

	private fun renderFireBar(fireBar: Character): Unit {

	}

	private fun renderLift(lift: Character): Unit {

		val liftName = lift.toString()
		val liftType = liftName.substring(5 + liftName.lastIndexOf("LIFT_"))
		val isShort = liftName.startsWith("SHORT")

		val extent = if (isShort) 1 else 2

		for (x in (lift.x)..(lift.x + extent)) {

			this.drawSprite(Sprite.Metatile.LIFT.sprite, x, lift.y)
			this.drawSprite(Sprite.Metatile.LIFT.sprite, x, lift.y, 8.0, 0.0)
		}
	}
}