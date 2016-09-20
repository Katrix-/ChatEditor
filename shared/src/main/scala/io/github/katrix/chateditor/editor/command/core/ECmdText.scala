package io.github.katrix.chateditor.editor.command.core

import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text

import io.github.katrix.chateditor.editor.Editor
import io.github.katrix.chateditor.editor.command.EditorCommand
import io.github.katrix.chateditor.lib.LibPerm

object ECmdText extends EditorCommand {

	override def execute(raw: String, editor: Editor, player: Player): Editor = {
		val componentText = editor.text
		val newText = componentText.addString(raw)
		val newEditor = editor.copy(text = newText)
		newText.sendPreview(newEditor, player)
		newEditor
	}

	override def aliases: Seq[String] = Seq("")
	override def help: Text = ???
	override def permission: String = LibPerm.Editor
}
