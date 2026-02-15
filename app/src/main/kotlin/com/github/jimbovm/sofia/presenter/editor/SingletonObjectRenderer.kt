package com.github.jimbovm.sofia.presenter.editor

import javafx.scene.canvas.Canvas
import javafx.scene.image.Image

import com.github.jimbovm.isobel.common.Area
import com.github.jimbovm.isobel.actor.geography.SingletonObject

import com.github.jimbovm.sofia.presenter.editor.Sprite

class SingletonObjectRenderer : Renderer {

	constructor(canvas: Canvas, area: Area) : super(canvas, area)

	fun render(singletonObject: SingletonObject): Unit {
		val objectName = singletonObject.type.toString()
		val objectNamePrefix = objectName.substring(0, objectName.lastIndexOf("_"))
		val objectNameSuffix = objectName.substring(objectName.lastIndexOf("_") + 1)

		val objectMetatile = when (objectName) {
			"QUESTION_BLOCK_USED" -> Sprite.Metatile.QUESTION_BLOCK_USED.sprite
			"JUMPING_BOARD" -> Sprite.Metatile.JUMPING_BOARD.sprite
			"SIDEWAYS_PIPE" -> Sprite.Metatile.SIDEWAYS_PIPE.sprite
			else -> when (objectNamePrefix) {
				"BRICK", "BRICK_MULTI" -> Sprite.Metatile.BRICK_BLOCK.sprite
				"HIDDEN_BLOCK" -> Sprite.Metatile.HIDDEN_BLOCK.sprite
				"QUESTION_BLOCK" -> Sprite.Metatile.QUESTION_BLOCK.sprite
				else -> Sprite.Metatile.BLANK.sprite
			}
		}

		val contentsMetatile = when (objectNameSuffix) {
			"1UP" -> Sprite.Metatile.MUSHROOM_1UP.sprite
			"COIN" -> Sprite.Metatile.COIN.sprite
			"POWERUP" -> Sprite.Metatile.MUSHROOM.sprite
			"STARMAN" -> Sprite.Metatile.STARMAN.sprite
			"VINE" -> Sprite.Metatile.VINE.sprite
			else -> Sprite.Metatile.BLANK.sprite
		}

		this.drawSprite(objectMetatile, singletonObject.x, singletonObject.y)

		if (objectName != "QUESTION_BLOCK_COIN") {
			this.drawSprite(contentsMetatile, singletonObject.x, singletonObject.y)
		}
	}
}