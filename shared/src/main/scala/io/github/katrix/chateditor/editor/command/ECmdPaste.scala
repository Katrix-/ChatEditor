package io.github.katrix.chateditor.editor.command

import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text

import io.github.katrix.chateditor.EditorPlugin
import io.github.katrix.chateditor.editor.Editor
import io.github.katrix.chateditor.lib.LibPerm
import io.github.katrix.katlib.helper.Implicits._

class ECmdPaste(implicit plugin: EditorPlugin) extends EditorCommand {

  override def execute(raw: String, editor: Editor, player: Player): Editor = editor.text.data("clipboard") match {
    case Some(string: String) =>
      player.sendMessage(plugin.config.text.eCmdPaste.value)
      editor.copy(text = editor.text.addString(string))
    case _ =>
      player.sendMessage(plugin.config.text.eCmdPasteClipboardEmpty.value)
      editor
  }

  override def aliases:    Seq[String] = Seq("copy")
  override def help:       Text        = t"Pastes the text currently in the clipboard"
  override def permission: String      = LibPerm.ECmdCopyPaste
}
