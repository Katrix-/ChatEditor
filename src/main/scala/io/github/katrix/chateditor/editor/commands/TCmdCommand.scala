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
package io.github.katrix.chateditor.editor.commands

import org.spongepowered.api.Sponge
import org.spongepowered.api.command.{CommandException, CommandMapping}
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text

import io.github.katrix.chateditor.editor.Editor
import io.github.katrix.chateditor.helper.Implicits._

object TCmdCommand extends TextCommand {

	override def execute(raw: String, editor: Editor, player: Player): Unit = {
		val splitString: Array[String] = raw.split(" ")
		if(splitString(0).startsWith("/")) {
			splitString(0) = splitString(0).substring(1)
		}

		val optMapping: Option[_ <: CommandMapping] = Sponge.getCommandManager.get(splitString(0), player).toOption
		optMapping match {
			case Some(mapping) =>
				val callable = mapping.getCallable
				if(callable.testPermission(player)) {
					try {
						callable.process(player, splitString.mkString(" "))
					}
					catch {
						case e: CommandException =>
							val text = e.getText
							player.sendMessage(if(text != null) text else "Something went wrong when trying to use that command".text)
					}
				}
				else {
					player.sendMessage("You don't have the permissions to use that command".richText.error())
				}
			case None => player.sendMessage("No such command found".richText.error())
		}
	}

	override def getAliases: Seq[String] = Seq("command")

	override def getHelp: Text = ???

	override def getPermission: String = ???
}