package io.github.katrix.chateditor.editor.command

import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text

import io.github.katrix.chateditor.editor.Editor
import io.github.katrix.chateditor.listener.EditorListener

class TCmdEnd(editorListener: EditorListener) extends TextCommand {

	override def execute(raw: String, editor: Editor, player: Player): Editor = {
		editor.end.end(editor) match {
			case Some(newEditor) => newEditor
			case None =>
				editorListener.editorPlayers.remove(player)
				??? //Tell listener to not add this one back
		}
	}
	override def aliases: Seq[String] = Seq("end")
	override def help: Text = ???
	override def permission: String = ???
}
