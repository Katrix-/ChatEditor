package io.github.katrix.chateditor.editor.command.core

import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text

import io.github.katrix.chateditor.editor.Editor
import io.github.katrix.chateditor.editor.command.EditorCommand
import io.github.katrix.chateditor.lib.LibPerm

object ECmdView extends EditorCommand {

	override def execute(raw: String, editor: Editor, player: Player): Editor = {
		editor.text.sendPreview(editor, player)
		editor
	}

	override def aliases: Seq[String] = Seq("view", "show")
	override def help: Text = ???
	override def permission: String = LibPerm.Editor
}
