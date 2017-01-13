package io.github.katrix.chateditor.editor.command.core

import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.format.TextColors._

import io.github.katrix.chateditor.EditorPlugin
import io.github.katrix.chateditor.editor.Editor
import io.github.katrix.chateditor.editor.command.{EditorCommand, EditorCommandRegistry}
import io.github.katrix.chateditor.lib.LibPerm
import io.github.katrix.katlib.helper.Implicits._

class ECmdHelp(cmdRegistry: EditorCommandRegistry)(implicit plugin: EditorPlugin) extends EditorCommand {

  override def execute(raw: String, editor: Editor, player: Player): Editor = {
    val args = raw.split(' ')
    if (args.length >= 2) {
      cmdRegistry.getCommand(args(1)) match {
        case Some(cmd) =>
          player.sendMessage(t"$YELLOW!${cmd.aliases.head}\n${cmd.help}")
          editor
        case None =>
          player.sendMessage(t"No command with that name found")
          editor
      }
    } else {
      player.sendMessage(t"${RED}You need to specify a command")
      editor
    }
  }
  override def aliases:    Seq[String] = Seq("help")
  override def help:       Text        = t"This right here"
  override def permission: String      = LibPerm.Editor
}
