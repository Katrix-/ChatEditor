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
import org.spongepowered.api.text.format.TextColors

import io.github.katrix.chateditor.editor.Editor
import io.github.katrix.katlib.helper.Implicits._

/**
	* Represents a command that the player can use while in an editor.
	*/
trait EditorCommand {

  /**
		* Executes this command, and optionally modifies the editor
		*
		* @param raw The raw string input without the ! character
		* @param editor The current editor
		* @param player The player that executes the command
		* @return The new editor to use
		*/
  def execute(raw: String, editor: Editor, player: Player): Editor

  /**
		* The aliases of this command
		*/
  def aliases: Seq[String]

  /**
		* The help for this command
		*/
  def help: Text

  /**
		* The permission required to use this command
		*/
  def permission: String

  final val IncompatibleCommand = t"${TextColors.RED}Incompatible command for this editor"
}
