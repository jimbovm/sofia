/*
 * SPDX-License-Identifier: MIT-0
 *
 * This file is part of Sofia (https://github.com/jimbovm/sofia).
 */

package io.github.jimbovm.sofia.viewmodel

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import java.io.File
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path

import jakarta.xml.bind.JAXBContext
import jakarta.xml.bind.JAXBException
import jakarta.xml.bind.MarshalException
import jakarta.xml.bind.UnmarshalException

import io.github.jimbovm.isobel.common.Game

import io.github.jimbovm.sofia.io.GameIO

class GameViewModel(val game: Game) {

	val id = SimpleStringProperty(game.id)
	val atlas = SimpleObjectProperty(game.atlas)
	val scenario = SimpleObjectProperty(game.scenario)

	private val modified = false

	private var openFile = ""

	/**
	 * Load a game from Isobel XML.
	 */
	@Throws(JAXBException::class, UnmarshalException::class, IllegalArgumentException::class)
	fun load(file: String): Game = GameIO.load(file)

	/**
	 * Save a game to Isobel XML.
	 */
	@Throws(JAXBException::class, MarshalException::class, IllegalArgumentException::class)
	fun save(file: String) {
		if (file == "") {
			GameIO.save(this.game, file)
		}
	}

	fun save() {
		GameIO.save(this.game, this.openFile)
	}

	fun commit() {
		this.game.id = id.get()
		this.game.atlas = atlas.get()
		this.game.scenario = scenario.get()
	}
}
