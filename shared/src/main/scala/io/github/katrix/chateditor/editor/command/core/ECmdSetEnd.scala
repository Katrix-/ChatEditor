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
package io.github.katrix.chateditor.editor.command.core

import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}

import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text
import org.spongepowered.api.world.{Location, World}

import io.github.katrix.chateditor.EditorPlugin
import io.github.katrix.chateditor.editor.Editor
import io.github.katrix.chateditor.editor.command.EditorCommand
import io.github.katrix.chateditor.editor.component.end.{CompEndChat, CompEndCommandBlock, CompEndNOOP}
import io.github.katrix.chateditor.lib.LibPerm
import io.github.katrix.katlib.helper.Implicits._

class ECmdSetEnd(implicit plugin: EditorPlugin) extends EditorCommand {

	override def execute(raw: String, editor: Editor, player: Player): Editor = {
		val args = raw.split(' ')
		if(args.length >= 2) {
			args(1) match {
				case "chat" =>
					player.sendMessage(plugin.config.text.endSet.value(Map(plugin.config.text.Behavior -> "chat").asJava).build())
					editor.copy(end = CompEndChat)
				case "commandblock" if player.hasPermission(LibPerm.CommandBlock) =>
					if(args.length > 5) {
						val tryLoc = for {
							x <- Try(args(2).toDouble)
							y <- Try(args(3).toDouble)
							z <- Try(args(4).toDouble)
						} yield new Location[World](player.getWorld, x, y, z)

						tryLoc match {
							case Success(loc) =>
								player.sendMessage(plugin.config.text.endSet.value(Map(plugin.config.text.Behavior -> "commandblock").asJava).build())
								editor.copy(end = new CompEndCommandBlock(loc))
							case Failure(e) =>
								player.sendMessage(plugin.config.text.endSetCommandBlockInvalidPos.value)
								editor
						}
					}
					else {
						player.sendMessage(plugin.config.text.endSetCommandBlockSpecifyPos.value)
						editor
					}
				case "command" if player.hasPermission(LibPerm.Command) =>
					player.sendMessage(plugin.config.text.endSet.value(Map(plugin.config.text.Behavior -> "command").asJava).build())
					editor.copy(end = CompEndChat)
				case "commandblock" | "command" =>
					player.sendMessage(plugin.config.text.behaviorMissingPerm.value)
					editor
				case "cancel" =>
					player.sendMessage(t"${plugin.config.text.endSet.value(Map(plugin.config.text.Behavior -> "none").asJava)}. ${
						plugin.config.text.endSetNOOPHelp.value}")
					editor.copy(end = new CompEndNOOP)
				case _ =>
					player.sendMessage(plugin.config.text.behaviorUnknown.value)
					editor
			}
		}
		else {
			player.sendMessage(plugin.config.text.behaviorMissing.value)
			editor
		}
	}

	override def aliases: Seq[String] = Seq("setEnd", "changeEnd")
	override def help: Text = t"Set a new end behavior. Valid behaviors are: chat, command, commandblock <x> <y> <z>, cancel"
	override def permission: String = LibPerm.Editor
}
