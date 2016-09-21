package io.github.katrix.chateditor.editor.command.core

import org.spongepowered.api.Sponge
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text

import io.github.katrix.chateditor.editor.Editor
import io.github.katrix.chateditor.editor.command.EditorCommand
import io.github.katrix.chateditor.lib.LibPerm
import io.github.katrix.katlib.KatPlugin

class ECmdEnd(plugin: KatPlugin) extends EditorCommand {

	override def execute(raw: String, editor: Editor, player: Player): Editor = {
		editor.end.end(editor) match {
			case Some(newEditor) => newEditor
			case None =>
				//TODO: Hacky, need better way
				Sponge.getScheduler.createTaskBuilder()
					.delayTicks(1)
					.execute(() => editor.listener.removeEditorPlayer(player))
					.submit(plugin)
				editor
		}
	}

	override def aliases: Seq[String] = Seq("end")
	override def help: Text = ???
	override def permission: String = LibPerm.Editor
}
