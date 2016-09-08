package io.github.katrix.chateditor.editor.command

import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text

import io.github.katrix.chateditor.editor.Editor

object TCmdView extends TextCommand {

	override def execute(raw: String, editor: Editor, player: Player): Editor = {
		editor.text.sendPreview(editor, player)
		editor
	}
	override def aliases: Seq[String] = Seq("view", "show")
	override def help: Text = ???
	override def permission: String = ???
}
