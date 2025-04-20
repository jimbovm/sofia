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

package com.github.jimbovm.sofia.presenter.editor

/**
 * Encapsulates coordinates for referring to sections of sprite sheets in a
 * semantic manner.
 */
class Sprite(
		val x: Double,
		val y: Double,
		val width: Double,
		val height: Double) {

	enum class Metatile(val sprite: Sprite) {
		AXE(Sprite(160.0, 48.0, 16.0, 16.0)),
		BLANK(Sprite(160.0, 0.0, 16.0, 16.0)),
		BLOCK(Sprite(0.0, 16.0, 16.0, 16.0)),
		BOWSER_BRIDGE(Sprite(114.0, 16.0, 16.0, 16.0)),
		BRICK(Sprite(32.0, 0.0, 16.0, 16.0)),
		BRIDGE_BASE(Sprite(112.0, 48.0, 8.0, 8.0)),
		BRIDGE_ROPE(Sprite(208.0, 40.0, 8.0, 8.0)),
		CANNON_BASE(Sprite(112.0, 32.0, 16.0, 16.0)),
		CANNON_BODY(Sprite(112.0, 16.0, 16.0, 16.0)),
		CANNON_TOP(Sprite(112.0, 0.0, 16.0, 16.0)),
		CASTLE_BRICK(Sprite(0.0, 48.0, 16.0, 16.0)),
		CHAIN(Sprite(224.0, 32.0, 16.0, 16.0)),
		CLOUD(Sprite(144.0, 0.0, 16.0, 16.0)),
		COIN(Sprite(160.0, 16.0, 8.0, 8.0)),
		CORAL(Sprite(256.0, 16.0, 16.0, 16.0)),
		EMPTY_BLOCK(Sprite(48.0, 0.0, 16.0, 16.0)),
		FLAGPOLE_FINIAL(Sprite(336.0, 32.0, 16.0, 16.0)),
		FLAGPOLE_STAFF(Sprite(336.0, 48.0, 16.0, 16.0)),
		GROUND(Sprite(0.0, 0.0, 16.0, 16.0)),
		HORIZONTAL_PIPE_BODY(Sprite(304.0, 32.0, 16.0, 32.0)),
		HORIZONTAL_PIPE_JOIN(Sprite(304.0, 32.0, 16.0, 32.0)),
		HORIZONTAL_PIPE_MOUTH(Sprite(288.0, 32.0, 16.0, 32.0)),
		INVISIBLE_BLOCK(Sprite(32.0, 16.0, 16.0, 16.0)),
		JUMPING_BOARD(Sprite(96.0, 32.0, 16.0, 32.0)),
		LIFT_CLOUD(Sprite(112.0, 48.0, 8.0, 8.0)),
		LIFT(Sprite(120.0, 48.0, 8.0, 8.0)),
		MUSHROOM_LEFT(Sprite(256.0, 0.0, 16.0, 16.0)),
		MUSHROOM_MIDDLE(Sprite(272.0, 0.0, 16.0, 16.0)),
		MUSHROOM_RIGHT(Sprite(288.0, 0.0, 16.0, 16.0)),
		QUESTION_BLOCK(Sprite(128.0, 32.0, 16.0, 16.0)),
		ROPE(Sprite(48.0, 48.0, 16.0, 16.0)),
		SCALE_PULLEY_LEFT(Sprite(16.0, 32.0, 16.0, 16.0)),
		SCALE_PULLEY_RIGHT(Sprite(48.0, 32.0, 16.0, 16.0)),
		SCALE_ROPE_MIDDLE(Sprite(32.0, 32.0, 16.0, 16.0)),
		SEAFLOOR(Sprite(0.0, 32.0, 16.0, 16.0)),
		STIPE_RING(Sprite(64.0, 32.0, 16.0, 16.0)),
		STIPE(Sprite(64.0, 48.0, 16.0, 16.0)),
		TREE_LEFT(Sprite(208.0, 0.0, 16.0, 16.0)),
		TREE_MIDDLE(Sprite(224.0, 0.0, 16.0, 16.0)),
		TREE_RIGHT(Sprite(240.0, 0.0, 16.0, 16.0)),
		TREE_TRUNK(Sprite(16.0, 16.0, 16.0, 16.0)),
		UPRIGHT_PIPE_BODY_LEFT(Sprite(320.0, 16.0, 16.0, 16.0)),
		UPRIGHT_PIPE_BODY_RIGHT(Sprite(336.0, 16.0, 16.0, 16.0)),
		UPRIGHT_PIPE_BODY(Sprite(320.0, 16.0, 32.0, 16.0)),
		UPRIGHT_PIPE_MOUTH(Sprite(320.0, 0.0, 32.0, 16.0)),
		MARIO_STAND(Sprite(480.0, 0.0, 16.0, 16.0)),
		MARIO_FALL(Sprite(496.0, 0.0, 16.0, 16.0)),
		GOOMBA(Sprite(640.0, 0.0, 16.0, 16.0)),
		BUZZY_BEETLE(Sprite(640.0, 16.0, 16.0, 16.0)),
		BLOOBER(Sprite(640.0, 32.0, 16.0, 24.0)),
		GREEN_TROOPA(Sprite(688.0, 32.0, 16.0, 24.0)),
		RED_TROOPA(Sprite(688.0, 8.0, 16.0, 24.0)),
		GREEN_PARATROOPA(Sprite(720.0, 32.0, 16.0, 24.0)),
		RED_PARATROOPA(Sprite(720.0, 8.0, 16.0, 24.0)),
		PIRANHA_PLANT(Sprite(784.0, 32.0, 16.0, 24.0)),
		LAKITU(Sprite(816.0, 32.0, 16.0, 24.0)),
		SPINY(Sprite(848.0, 16.0, 16.0, 16.0)),
		PODOBOO(Sprite(880.0, 16.0, 16.0, 16.0)),
		HAMMER_BRO(Sprite(927.0, 22.0, 16.0, 34.0)),
		RED_CHEEP(Sprite(975.0, 16.0, 16.0, 16.0)),
		GREEN_CHEEP(Sprite(975.0, 39.0, 16.0, 16.0)),
		BOWSER(Sprite(1006.0, 24.0, 32.0, 32.0));
	}
}
