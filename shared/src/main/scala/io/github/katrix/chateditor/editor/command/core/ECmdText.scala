package io.github.katrix.chateditor.editor.command.core

import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text

import io.github.katrix.chateditor.editor.Editor
import io.github.katrix.chateditor.editor.command.EditorCommand
import io.github.katrix.chateditor.lib.LibPerm
import io.github.katrix.katlib.helper.Implicits._

object ECmdText extends EditorCommand {

	override def execute(raw: String, editor: Editor, player: Player): Editor = {
		val newText = editor.text.addString(raw)
		val newEditor = editor.copy(text = newText)
		newText.sendPreview(newEditor, player)
		newEditor
	}

	override def aliases: Seq[String] = Seq(throw new IllegalStateException("ECmdText should NOT be registered"))
	override def help: Text = t"Add new text to the editor"
	override def permission: String = LibPerm.Editor
}
