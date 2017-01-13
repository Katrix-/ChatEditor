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

import scala.util.{Failure, Success, Try}

import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text

import io.github.katrix.chateditor.EditorPlugin
import io.github.katrix.chateditor.editor.Editor
import io.github.katrix.chateditor.editor.command.EditorCommand
import io.github.katrix.chateditor.lib.LibPerm
import io.github.katrix.katlib.helper.Implicits._

class ECmdPosSelect(implicit plugin: EditorPlugin) extends EditorCommand {

  override def execute(raw: String, editor: Editor, player: Player): Editor = {
    val args = raw.split(' ')
    if (args.length >= 3) {
      val tryParse = for {
        pos    <- Try(args(1).toInt)
        select <- Try(args(2).toInt)
      } yield (pos, select)

      tryParse match {
        case Success((pos, select)) =>
          val posText    = editor.text.pos = pos
          val selectText = posText.select = select
          val newEditor  = editor.copy(text = selectText)
          selectText.sendPreview(newEditor, player)
          newEditor
        case Failure(e) =>
          player.sendMessage(plugin.config.text.eCmdPosSelectInvalid.value)
          editor
      }
    } else {
      player.sendMessage(plugin.config.text.eCmdPosSelectSpecify.value)
      editor
    }
  }
  override def aliases:    Seq[String] = Seq("posSelect")
  override def help:       Text        = t"Set a new position and selection"
  override def permission: String      = LibPerm.Editor
}
