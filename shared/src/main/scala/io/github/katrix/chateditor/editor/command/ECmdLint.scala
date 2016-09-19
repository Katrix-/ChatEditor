package io.github.katrix.chateditor.editor.command

import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text

import io.github.katrix.chateditor.editor.Editor
import io.github.katrix.chateditor.lib.LibPerm

abstract class ECmdLint extends EditorCommand {

	override def execute(raw: String, editor: Editor, player: Player): Editor = {
		player.sendMessage(lint(editor.text.builtString))
		editor
	}

	def lint(string: String): Text

	override def permission: String = LibPerm.ECmdLint
}
