package io.github.katrix.chateditor.editor.command

import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.format.TextColors._

import io.github.katrix.chateditor.EditorPlugin
import io.github.katrix.chateditor.editor.Editor
import io.github.katrix.chateditor.lib.LibPerm
import io.github.katrix.katlib.helper.Implicits._

class ECmdCut(implicit plugin: EditorPlugin) extends EditorCommand {

  override def execute(raw: String, editor: Editor, player: Player): Editor =
    if (editor.text.hasSelection) {
      val selected    = editor.text.selectedString
      val newCompCopy = editor.text.dataPut("clipboard", selected)
      val newCompCut  = newCompCopy.addString("")
      player.sendMessage(t"${GREEN}Cut the selected text")
      editor.copy(text = newCompCut)
    } else {
      player.sendMessage(t"${RED}Please make a selection before using this command")
      editor
    }

  override def aliases:    Seq[String] = Seq("copy")
  override def help:       Text        = t"Cut the selected text to the clipboard. Paste with !paste"
  override def permission: String      = LibPerm.ECmdCopyPaste
}
