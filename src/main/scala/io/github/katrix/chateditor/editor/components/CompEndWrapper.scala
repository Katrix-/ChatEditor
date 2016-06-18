package io.github.katrix.permissionblock.editor.components

import org.spongepowered.api.entity.living.player.Player

import io.github.katrix.permissionblock.editor.Editor
import io.github.katrix.permissionblock.listener.EditorListener

class CompEndWrapper(editor: Editor, orig: Editor, player: Player) extends ComponentEnd(editor) {

	override def end(): Boolean = {
		EditorListener.EDITOR_PLAYERS.put(player, orig)
		orig.text replaceSelected editor.text.builtString
		false
	}
}
