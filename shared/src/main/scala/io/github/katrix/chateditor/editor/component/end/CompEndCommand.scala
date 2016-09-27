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
package io.github.katrix.chateditor.editor.component.end

import org.spongepowered.api.Sponge
import org.spongepowered.api.text.format.TextColors._

import io.github.katrix.chateditor.editor.Editor
import io.github.katrix.chateditor.editor.component.EndComponent
import io.github.katrix.katlib.helper.Implicits._

object CompEndCommand extends EndComponent {

	override def end(editor: Editor): Option[Editor] = {
		editor.player.get match {
			case Some(player) => editor.text.builtString.split(' ') match {
				case Array(command, arguments@_*) =>

					Sponge.getCommandManager.get(command, player).toOption match {
						case Some(mapping) if mapping.getCallable.testPermission(player) =>
							mapping.getCallable.process(player, arguments.mkString(" "))
							None
						case Some(_) =>
							player.sendMessage(t"${RED}You don't have the permissions for that command")
							Some(editor)
						case None =>
							player.sendMessage(t"${RED}No command by that name found")
							Some(editor)
					}
				case _ =>
					player.sendMessage(t"No command specified, please set a command")
					Some(editor)
			}
			case None => None //If no player is found, just remove the editor and call it done
		}
	}
}