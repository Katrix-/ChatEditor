package io.github.katrix.chateditor.editor.command

import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.format.TextColors._

import io.github.katrix.chateditor.editor.Editor
import io.github.katrix.chateditor.lib.LibPerm
import io.github.katrix.katlib.helper.Implicits._

object ECmdPaste extends EditorCommand {

	override def execute(raw: String, editor: Editor, player: Player): Editor = editor.text.data("clipboard") match {
		case Some(string: String) =>
			player.sendMessage(t"${GREEN}Pasted: $string")
			editor.copy(text = editor.text.addString(string))
		case _ =>
			player.sendMessage(t"${RED}Nothing on the clipboard")
			editor
	}

	override def aliases: Seq[String] = Seq("copy")
	override def help: Text = t"Pastes the text currently in the clipboard"
	override def permission: String = LibPerm.ECmdCopyPaste
}
