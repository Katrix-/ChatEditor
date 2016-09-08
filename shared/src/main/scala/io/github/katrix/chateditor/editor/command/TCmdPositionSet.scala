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
package io.github.katrix.chateditor.editor.command

import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.format.TextColors._

import io.github.katrix.chateditor.editor.Editor
import io.github.katrix.katlib.helper.Implicits._

object TCmdPositionSet extends TextCommand {

	override def execute(raw: String, editor: Editor, player: Player): Editor = {
		val text = editor.text

		val intString = if(raw.startsWith("p=")) {
			raw.substring(2)
		}
		else {
			raw.split(" ", 1)(1)
		}

		try {
			val amount = intString.toInt
			val newText = text.pos = amount
			val newEditor = editor.copy(text = newText)
			val position = text.pos
			player.sendMessage(t"${GREEN}The position is now at $position")
			newEditor.text.sendPreview(newEditor, player)
			newEditor
		}
		catch {
			case e: NumberFormatException =>
				player.sendMessage(t"${RED}Not a number")
				editor
		}
	}

	override def aliases: Seq[String] = Seq("p=", "posSet", "setPos")
	override def help: Text = ???
	override def permission: String = ???
}