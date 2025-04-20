/* SPDX-License-Identifier: MIT-0

Ⓒ 2025 Jimbo Brierley.

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
of the Software, and to permit persons to whom the Software is furnished to do
so.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE. */

package com.github.jimbovm.sofia

import java.io.IOException
import java.util.*

import com.github.jimbovm.sofia.view.Main

/**
 * Entry point for the Sofia GUI.
 *
 * @param[args] Arguments supplied from the command line. 
 */
fun main(args: Array<String>) {

	var buildInfo: Properties? = Properties()

	try {
		buildInfo?.load(ClassLoader.getSystemResourceAsStream("build_info.properties"))
	} catch (e: IOException) {
		buildInfo = null
	}

	val version = buildInfo?.getProperty("sofiaVersion") ?: "(version unknown)"

	println("Hello from Sofia ${version}!")
	Main.run(args)
}