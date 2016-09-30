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

import java.nio.file.Paths

import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}

import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text

import io.github.katrix.chateditor.EditorPlugin
import io.github.katrix.chateditor.editor.Editor
import io.github.katrix.chateditor.editor.command.EditorCommand
import io.github.katrix.chateditor.editor.component.end.CompEndSave
import io.github.katrix.chateditor.editor.component.text.{CompTextCursor, CompTextLine}
import io.github.katrix.chateditor.lib.LibPerm
import io.github.katrix.katlib.helper.Implicits._

class ECmdSetText(implicit plugin: EditorPlugin) extends EditorCommand {

	override def execute(raw: String, editor: Editor, player: Player): Editor = {
		val args = raw.split(' ')
		if(args.length >= 2) {
			val behavior = args(1)
			behavior match {
				case "cursor" =>
					player.sendMessage(plugin.config.text.textSet.value(Map(plugin.config.text.Behavior -> "cursor").asJava).build())
					editor.copy(text = CompTextCursor(0, 0, editor.text.builtString))
				case "line" =>
					player.sendMessage(plugin.config.text.textSet.value(Map(plugin.config.text.Behavior -> "line").asJava).build())
					val currentStrings = editor.text.builtString.split('\n').flatMap(_.split("""\\n"""))
					val strings = if(currentStrings.forall(_.isEmpty)) Seq() else currentStrings: Seq[String]
					editor.copy(text = CompTextLine(0, 0, strings))
				case "file" if player.hasPermission(LibPerm.UnsafeFile) =>
					if(args.length > 2) {
						Try(Paths.get(args(2))) match {
							case Success(path) =>
								val newText = editor.text.dataPut("path", path)
								player.sendMessage(t"${plugin.config.text.textSet.value(Map(plugin.config.text.Behavior -> "cursor").asJava)}, ${
									plugin.config.text.endSet.value(Map(plugin.config.text.Behavior -> "cursor").asJava)}")
								editor.copy(text = newText, end = new CompEndSave)
							case Failure(e) =>
								player.sendMessage(plugin.config.text.pathInvalid.value(Map(plugin.config.text.Behavior -> e.getMessage).asJava).build())
								editor
						}
					}
					else {
						player.sendMessage(plugin.config.text.pathInvalid.value)
						editor
					}
				case "file" =>
					player.sendMessage(plugin.config.text.behaviorMissingPerm.value)
					editor
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

	override def aliases: Seq[String] = Seq("setText", "changeText")
	override def help: Text = t"Set a new text behavior. Valid behaviors are: cursor, line, file <path>"
	override def permission: String = LibPerm.Editor
}
