package io.github.katrix.chateditor.editor.command.core

import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.format.TextColors._

import io.github.katrix.chateditor.editor.Editor
import io.github.katrix.chateditor.editor.command.{EditorCommand, EditorCommandRegistry}
import io.github.katrix.chateditor.lib.LibPerm
import io.github.katrix.katlib.helper.Implicits._

class ECmdHelp(cmdRegistry: EditorCommandRegistry) extends EditorCommand {

	override def execute(raw: String, editor: Editor, player: Player): Editor = {
		val args = raw.split(' ')
		if(args.length >= 2) {
			cmdRegistry.getCommand(args(1)) match {
				case Some(cmd) =>
					player.sendMessage(t"$GREEN!${cmd.aliases.head}\n${cmd.help}")
					editor
				case None =>
					player.sendMessage(t"Unknown command")
					editor
			}
		}
		else {
			player.sendMessage(t"Please specify a command to get help for")
			editor
		}
	}
	override def aliases: Seq[String] = Seq("help")
	override def help: Text = t"This right here"
	override def permission: String = LibPerm.Editor
}
