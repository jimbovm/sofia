/*
 * SPDX-License-Identifier: MIT-0
 *
 * This file is part of Sofia (https://github.com/jimbovm/sofia).
 */

package io.github.jimbovm.sofia.viewmodel

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import org.apache.logging.log4j.LogManager

import io.github.jimbovm.isobel.common.Area

class AreaViewModel(val area: Area) {

	private val log = LogManager.getRootLogger()

	val id = SimpleStringProperty(area.id)
	val familiarName = SimpleStringProperty(area.familiarName)

	val header = SimpleObjectProperty(area.header)
	val environment = SimpleObjectProperty(area.environment)
	val geography = SimpleObjectProperty(area.geography)
	val population = SimpleObjectProperty(area.population)

	init {
		environment.addListener { _, _, new ->
			this.area.environment = new
			log.trace(this.area.toString())
		}
	}

	fun commit() {
		this.area.familiarName = familiarName.get()
		this.area.header = header.get()
		this.area.environment = environment.get()
		this.area.geography = geography.get()
		this.area.population = population.get()
	}

	fun get(): Area = this.area
}
