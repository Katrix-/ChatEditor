package io.github.katrix.chateditor.editor.components

import org.spongepowered.api.entity.living.player.Player

import io.github.katrix.chateditor.editor.Editor
import io.github.katrix.chateditor.listener.EditorListener

class CompEndWrapper(editor: Editor, orig: Editor, player: Player) extends ComponentEnd(editor) {

	override def end(): Boolean = {
		EditorListener.EDITOR_PLAYERS.put(player, orig)
		orig.text replaceSelected editor.text.builtString
		false
	}
}
