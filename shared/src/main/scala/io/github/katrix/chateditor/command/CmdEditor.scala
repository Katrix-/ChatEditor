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
package io.github.katrix.chateditor.command

import scala.ref.WeakReference

import org.spongepowered.api.command.args.CommandContext
import org.spongepowered.api.command.spec.CommandSpec
import org.spongepowered.api.command.{CommandResult, CommandSource}
import org.spongepowered.api.entity.living.player.Player

import io.github.katrix.chateditor.EditorPlugin
import io.github.katrix.chateditor.editor.Editor
import io.github.katrix.chateditor.editor.component.end.CompEndChat
import io.github.katrix.chateditor.editor.component.text.CompTextCursor
import io.github.katrix.chateditor.lib.LibPerm
import io.github.katrix.chateditor.listener.EditorHandler
import io.github.katrix.katlib.command.CommandBase
import io.github.katrix.katlib.helper.Implicits._

class CmdEditor(handler: EditorHandler)(implicit plugin: EditorPlugin) extends CommandBase(None) {

	override def execute(src: CommandSource, args: CommandContext): CommandResult = src match {
		case player: Player =>
			player.sendMessage(plugin.config.text.commandEditorSuccess.value)
			handler.addEditorPlayer(player, Editor(CompTextCursor(0, 0, ""), CompEndChat, WeakReference(player), handler))
			CommandResult.success()
		case _ => throw nonPlayerError
	}

	override def commandSpec: CommandSpec = CommandSpec.builder()
		.description(t"Opens a minimal editor with an end behavior of chat")
		.permission(LibPerm.Editor)
		.children(this)
		.executor(this)
		.build()

	override def aliases: Seq[String] = Seq("editor")

	override def children: Seq[CommandBase] = Seq(new CmdEditorFile(handler, this))
}
