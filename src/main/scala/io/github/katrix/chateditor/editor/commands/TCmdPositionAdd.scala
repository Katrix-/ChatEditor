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
package io.github.katrix.chateditor.editor.commands

import scala.reflect.runtime.universe

import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text

import io.github.katrix.chateditor.editor.Editor
import io.github.katrix.chateditor.editor.components.{CompTextCursor, Component}
import io.github.katrix.chateditor.helper.Implicits._

object TCmdPositionAdd extends TextCommand {

	override def execute(raw: String, editor: Editor, player: Player): Unit = {
		val text = editor.text

		val intString = if(raw.startsWith("p+")) {
			raw.substring(2)
		}
		else {
			raw.split(" ", 1).apply(1)
		}

		try {
			val amount = intString.toInt
			text.pos += amount
			val position = text.pos
			player.sendMessage(s"The position is now at $position".richText.success())
			editor.text.sendFormatted(player)
		}
		catch {
			case e: NumberFormatException =>
				player.sendMessage("Not a number".richText.error())
		}
	}

	override def getAliases: Seq[String] = Seq("p+", "posAdd", "addPos")

	override def getHelp: Text = ???

	override def getPermission: String = ???

	override def getCompatibility: universe.TypeTag[_ <: Component] = universe.typeTag[CompTextCursor]
}