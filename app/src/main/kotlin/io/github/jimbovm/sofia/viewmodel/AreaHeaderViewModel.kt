package io.github.jimbovm.sofia.viewmodel;

import io.github.jimbovm.isobel.common.Area
import io.github.jimbovm.isobel.common.AreaHeader
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty

class AreaHeaderViewModel(private val header: AreaHeader) {

	val autowalk = SimpleObjectProperty(header.isAutowalk)
	val background = SimpleObjectProperty(header.background)
	val fill = SimpleObjectProperty(header.fill)
	val platform = SimpleObjectProperty(header.platform)
	val scenery = SimpleObjectProperty(header.scenery)
	val startPosition = SimpleObjectProperty(header.startPosition)
	val ticks = SimpleObjectProperty(header.ticks)

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