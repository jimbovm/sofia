/*
 * SPDX-License-Identifier: MIT-0
 *
 * This file is part of Sofia (https://github.com/jimbovm/sofia).
 */

package io.github.jimbovm.sofia.editor

import javafx.scene.canvas.Canvas

import io.github.jimbovm.isobel.common.Area

import io.github.jimbovm.sofia.editor.BaseRenderer

class UprightPipeRenderer : BaseRenderer {

	constructor(canvas: Canvas, area: Area, skin: Skin) : super(canvas, area, skin)
}
