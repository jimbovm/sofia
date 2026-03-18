/*
 * SPDX-License-Identifier: MIT-0
 *
 * This file is part of Sofia (https://github.com/jimbovm/sofia).
 */

package io.github.jimbovm.sofia

import io.github.jimbovm.sofia.view.Main
import java.io.IOException
import java.util.*

/**
 * Entry point for the Sofia GUI.
 *
 * @param[args] Arguments supplied from the command line.
 */
fun main(args: Array<String>) {
	// Locale.setDefault(Locale.ENGLISH)

	var buildInfo: Properties? = Properties()

	try {
		buildInfo?.load(ClassLoader.getSystemResourceAsStream("build_info.properties"))
	} catch (e: IOException) {
		buildInfo = null
	}

	val version = buildInfo?.getProperty("sofiaVersion") ?: "(version unknown)"

	println("Hello from Sofia $version!")
	Main.run(args)
}
