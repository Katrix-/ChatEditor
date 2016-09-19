package io.github.katrix.chateditor.editor.command.core

import org.spongepowered.api.Sponge
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text

import io.github.katrix.chateditor.editor.Editor
import io.github.katrix.chateditor.editor.command.TextCommand
import io.github.katrix.chateditor.listener.EditorListener
import io.github.katrix.katlib.KatPlugin

class TCmdEnd(editorListener: EditorListener, plugin: KatPlugin) extends TextCommand {

	override def execute(raw: String, editor: Editor, player: Player): Editor = {
		editor.end.end(editor) match {
			case Some(newEditor) => newEditor
			case None =>
				//TODO: Hacky, need better way
				Sponge.getScheduler.createTaskBuilder()
					.delayTicks(1)
					.execute(() => editorListener.editorPlayers.remove(player))
					.submit(player)
				editor
		}
	}
	override def aliases: Seq[String] = Seq("end")
	override def help: Text = ???
	override def permission: String = ???
}
