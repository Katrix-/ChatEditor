package io.github.katrix.chateditor.editor.command

import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.format.TextColors._

import io.github.katrix.chateditor.editor.Editor
import io.github.katrix.chateditor.lib.LibPerm
import io.github.katrix.katlib.helper.Implicits._

object ECmdCut extends EditorCommand {

	override def execute(raw: String, editor: Editor, player: Player): Editor = {
		if(editor.text.hasSelection) {
			val selected = editor.text.selectedString
			val newCompCopy = editor.text.dataPut("copy", selected)
			val newCompCut = newCompCopy.addString("")
			player.sendMessage(t"${GREEN}Copied: $selected")
			editor.copy(text = newCompCut)
		}
		else {
			player.sendMessage(t"${RED}Please select something")
			editor
		}
	}

	override def aliases: Seq[String] = Seq("copy")
	override def help: Text = ???
	override def permission: String = LibPerm.ECmdCopyPaste
}
