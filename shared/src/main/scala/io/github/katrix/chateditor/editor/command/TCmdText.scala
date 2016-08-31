package io.github.katrix.chateditor.editor.command

import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text

import io.github.katrix.chateditor.editor.Editor

object TCmdText extends TextCommand {

	override def execute(raw: String, editor: Editor, player: Player): Editor = {
		val componentText = editor.text
		val newText = componentText.addString(raw)
		val newEditor = editor.copy(text = newText)
		newText.sendPreview(newEditor, player)
		newEditor
	}
	override def aliases: Seq[String] = Seq("")
	override def help: Text = ???
	override def permission: String = ???
}
