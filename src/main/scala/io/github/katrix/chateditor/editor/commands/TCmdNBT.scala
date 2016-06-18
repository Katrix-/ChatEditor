package io.github.katrix.chateditor.editor.commands

import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text

import io.github.katrix.chateditor.helper.Implicits._
import io.github.katrix.chateditor.editor.Editor
import io.github.katrix.chateditor.editor.components.{CompEndWrapper, CompTextNBT}
import io.github.katrix.chateditor.listener.EditorListener

object TCmdNBT extends TextCommand{

	override def execute(raw: String, editor: Editor, player: Player): Unit = {
		editor.text match {
			case nbtEditor: CompTextNBT => nbtEditor.parseCurrent()
			case _ =>
				val newEditor = new Editor(
					ed => new CompTextNBT(ed, editor.text.selectedText, player),
					ed => new CompEndWrapper(ed, editor, player))
				EditorListener.EDITOR_PLAYERS.put(player, newEditor)

				player.sendMessage(
					"""You are now in an NBT editor.
						|Exit the NBT editor with !end.
						|Validate the nbt with !nbt""".stripMargin.richText.info())
		}
	}

	override def getPermission: String = ???

	override def getHelp: Text = ???

	override def getAliases: Seq[String] = Seq("nbt")
}
