/*
 * This file is part of PermissionBlock, licensed under the MIT License (MIT).
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
package io.github.katrix.permissionblock.editor.components

import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.format.TextColors
import com.google.common.collect.ImmutableList
import io.github.katrix.permissionblock.editor.Editor

class CompTextCursor(editor: Editor, string: String) extends ComponentText(editor) {

	private val commandBuilder = new StringBuilder
	commandBuilder.append(string)
	private var _cursor = commandBuilder.length

	def this(editor: Editor) {
		this(editor, "")
	}

	def addString(string: String): Unit = {
		commandBuilder.insert(_cursor, string)
		_cursor += string.length
	}

	def deleteCharacters(amount: Int): Unit = {
		commandBuilder.delete(_cursor, _cursor + amount)
		_cursor = validateCursorPos
	}

	override def pos: Int = _cursor

	override def pos_=(cursor: Int): Unit = {
		this._cursor = cursor
		this._cursor = validateCursorPos
	}

	override def pos_+=(amount: Int): Unit = {
		this._cursor += amount
		this._cursor = validateCursorPos
	}

	override def pos_-=(amount: Int): Unit = {
		this._cursor -= amount
		this._cursor = validateCursorPos
	}

	private def validateCursorPos: Int = {
		val length = commandBuilder.length

		if(_cursor > length) {
			_cursor = length
		}
		else if(_cursor < 0) {
			_cursor = 0
		}

		_cursor
	}

	def builtString: String = commandBuilder.toString

	def sendFormatted(player: Player): Unit = {
		player.sendMessage(formatted.head)
	}

	def formatted: Seq[Text] = {
		val firstPart = commandBuilder.substring(0, _cursor)
		if(_cursor < commandBuilder.length) {
			val selected = String.valueOf(commandBuilder.charAt(_cursor))
			val secondPart = commandBuilder.substring(_cursor + 1, commandBuilder.length)

			Seq(Text.of(firstPart, TextColors.BLUE, "[", selected, "]", TextColors.RESET, secondPart))
		}
		else {
			Seq(Text.of(firstPart))
		}
	}
}