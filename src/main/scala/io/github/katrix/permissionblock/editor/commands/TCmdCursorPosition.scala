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
package io.github.katrix.permissionblock.editor.commands

import scala.reflect.runtime._

import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.format.TextColors

import io.github.katrix.permissionblock.helper.Implicits._
import io.github.katrix.permissionblock.editor.Editor
import io.github.katrix.permissionblock.editor.components.{CompTextCursor, Component}

object TCmdCursorPosition extends TextCommand {

	def execute(raw: String, editor: Editor, player: Player): Unit = {
		val cursor = editor.getComponentUnchecked(universe.typeTag[CompTextCursor])
		player.sendMessage(s"Cursor position is ${cursor.cursor}".richText.info())
		editor.text.sendFormatted(player)
	}

	def getAliases: Seq[String] = Seq("c", "cursorPos", "posCursor")

	def getHelp: Text = ???

	def getPermission: String = ???

	override def getCompatibility: universe.TypeTag[_ <: Component] = universe.typeTag[CompTextCursor]
}