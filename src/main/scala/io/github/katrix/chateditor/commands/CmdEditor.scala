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
package io.github.katrix.chateditor.commands

import org.spongepowered.api.command.args.CommandContext
import org.spongepowered.api.command.spec.{CommandExecutor, CommandSpec}
import org.spongepowered.api.command.{CommandException, CommandResult, CommandSource}
import org.spongepowered.api.entity.living.player.Player

import io.github.katrix.chateditor.editor.Editor
import io.github.katrix.chateditor.editor.components.{CompEndChat, CompTextCursor}
import io.github.katrix.chateditor.helper.Implicits._
import io.github.katrix.chateditor.lib.LibPerm
import io.github.katrix.chateditor.listener.EditorListener

object CmdEditor extends CommandExecutor {

	@throws[CommandException]
	def execute(src: CommandSource, args: CommandContext): CommandResult = src match {
		case player: Player =>
			EditorListener.EDITOR_PLAYERS.put(player, new Editor(new CompTextCursor(_), new CompEndChat(_, player)))
			player.sendMessage("You are now in a editor. Just start typing. Use !end, to end the editor, and !help to get more help".richText.info())
			CommandResult.success
		case _ =>
			src.sendMessage("Only players can open a editor".richText.error())
			CommandResult.empty
	}

	def getCommand: CommandSpec = CommandSpec.builder permission LibPerm.EDITOR_COMMAND description "Open a text editor".text extendedDescription
		"While inside of the editor, normal chat and commands will no longer behave as normal. Use !help in the editor for help".text executor
		this build

	def getAliases: Seq[String] = Seq("editor")
}