/*
 * SPDX-License-Identifier: MIT-0
 *
 * This file is part of Sofia (https://github.com/jimbovm/sofia).
 */

package io.github.jimbovm.sofia.io

import java.nio.file.Files
import java.nio.file.Path

import jakarta.xml.bind.JAXBContext
import jakarta.xml.bind.JAXBException
import jakarta.xml.bind.MarshalException
import jakarta.xml.bind.Marshaller
import jakarta.xml.bind.UnmarshalException

import io.github.jimbovm.isobel.common.Game

/**
 * Static utility class encapsulating load, save and export functionality.
 */
class GameIO {

	companion object {

		private fun context(): JAXBContext = JAXBContext.newInstance(Game::class.java)

		/**
		 * Load a game from Isobel XML.
		 */
		@Throws(JAXBException::class, UnmarshalException::class, IllegalArgumentException::class)
		fun load(file: String): Game {
			var path = Path.of(file)
			var reader = Files.newBufferedReader(path)
			var unmarshaller = GameIO.context().createUnmarshaller()

			return unmarshaller.unmarshal(reader) as Game
		}

		/**
		 * Save a game as Isobel XML.
		 */
		@Throws(JAXBException::class, MarshalException::class, IllegalArgumentException::class)
		fun save(game: Game, file: String) {
			var path = Path.of(file)
			var writer = Files.newBufferedWriter(path)
			var marshaller = GameIO.context().createMarshaller()
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "utf-8")

			marshaller.marshal(game, path.toFile())
		}
	}
}
