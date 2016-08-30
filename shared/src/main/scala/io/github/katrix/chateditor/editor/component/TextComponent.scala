/*
 * This file is part of ChatEditor, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2016 Katrix
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package io.github.katrix.chateditor.editor.component

import org.spongepowered.api.entity.living.player.Player

abstract class TextComponent {
	type Preview

	def builtString: String
	def selectedString: String

	def preview: Preview
	def selectedPreview: Preview
	def sendPreview(player: Player): Unit

	def addString(string: String): Unit

	def hasSelection: Boolean = pos != select

	def pos: Int
	def pos_=(pos: Int): Unit

	def select: Int
	def select_=(pos: Int): Unit
}
