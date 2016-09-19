package io.github.katrix.chateditor.editor.command

import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text

import io.github.katrix.chateditor.editor.Editor
import io.github.katrix.chateditor.editor.component.text.CompTextLinter

import io.github.katrix.katlib.helper.Implicits._

class TCmdLint extends TextCommand {

	override def execute(raw: String, editor: Editor, player: Player): Editor = editor.text match {
		case linter: CompTextLinter =>
			player.sendMessage(linter.lint)
			editor
		case _ =>
			incompatibleCommand(player)
			editor
	}

	override def aliases: Seq[String] = Seq("lint")
	override def help: Text = ???
	override def permission: String = ???
}
