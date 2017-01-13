package io.github.katrix.chateditor.editor.command.core

import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text

import io.github.katrix.chateditor.EditorPlugin
import io.github.katrix.chateditor.editor.Editor
import io.github.katrix.chateditor.editor.command.EditorCommand
import io.github.katrix.chateditor.editor.component.text.CompTextLine
import io.github.katrix.chateditor.lib.LibPerm
import io.github.katrix.katlib.helper.Implicits._

class ECmdAddLine(implicit plugin: EditorPlugin) extends EditorCommand {

  override def execute(raw: String, editor: Editor, player: Player): Editor = editor.text match {
    case line: CompTextLine =>
      player.sendMessage(plugin.config.text.eCmdAddLine.value)
      editor.copy(text = line.copy(content = line.content :+ ""))
    case _ =>
      player.sendMessage(IncompatibleCommand)
      editor
  }

  override def aliases:    Seq[String] = Seq("addLine")
  override def help:       Text        = t"Adds a new line to a line editor"
  override def permission: String      = LibPerm.Editor
}
