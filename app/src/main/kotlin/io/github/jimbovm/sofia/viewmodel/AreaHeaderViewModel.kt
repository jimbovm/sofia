/*
 * SPDX-License-Identifier: MIT-0
 *
 * This file is part of Sofia (https://github.com/jimbovm/sofia).
 */

package io.github.jimbovm.sofia.viewmodel

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import javafx.beans.property.SimpleObjectProperty

import io.github.jimbovm.isobel.common.AreaHeader

class AreaHeaderViewModel(private val header: AreaHeader) {

	val autowalk = SimpleObjectProperty(header.isAutowalk)
	val background = SimpleObjectProperty(header.background)
	val fill = SimpleObjectProperty(header.fill)
	val platform = SimpleObjectProperty(header.platform)
	val scenery = SimpleObjectProperty(header.scenery)
	val startPosition = SimpleObjectProperty(header.startPosition)
	val ticks = SimpleObjectProperty(header.ticks)

	private val log = LogManager.getRootLogger()

	init {

		background.addListener { _, old, new ->
			this.header.background = new
			log.debug(String.format("%s->%s", old, new))
		}

		fill.addListener { _, old, new ->
			this.header.fill = new
			log.debug(String.format("%s->%s", old, new))
		}

		platform.addListener { _, old, new ->
			this.header.platform = new
			log.debug(String.format("%s->%s", old, new))
		}

		scenery.addListener { _, old, new ->
			this.header.scenery = new
			log.debug(String.format("%s->%s", old, new))
		}

		startPosition.addListener { _, old, new ->
			this.header.startPosition = new
			log.debug(String.format("%s->%s", old, new))
		}

		ticks.addListener { _, old, new ->
			this.header.ticks = new
			log.debug(String.format("%s->%s", old, new))
		}
	}

	fun commit() {
		this.header.isAutowalk = autowalk.get()
		this.header.background = background.get()
		this.header.fill = fill.get()
		this.header.platform = platform.get()
		this.header.scenery = scenery.get()
		this.header.startPosition = startPosition.get()
		this.header.ticks = ticks.get()
	}
}
