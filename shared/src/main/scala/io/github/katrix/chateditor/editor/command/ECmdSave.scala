package io.github.katrix.chateditor.editor.command

import java.nio.file.Path

import scala.util.{Failure, Success}

import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.format.TextColors._

import io.github.katrix.chateditor.editor.Editor
import io.github.katrix.chateditor.editor.component.text.{CompTextLine, FileEditorHelper}
import io.github.katrix.chateditor.lib.LibPerm
import io.github.katrix.katlib.helper.Implicits._

object ECmdSave extends EditorCommand {

	override def execute(raw: String, editor: Editor, player: Player): Editor = {

		if(player.hasPermission(LibPerm.UnsafeFile)) {
			editor.text.data("path") match {
				case Some(path: Path) => FileEditorHelper.save(path, editor) match {
					case Success(_) =>
						player.sendMessage(t"${GREEN}Saved file successfully")
						editor
					case Failure(e) =>
						player.sendMessage(t"$RED${e.getMessage}")
						editor
				}
				case _ =>
					player.sendMessage(t"${RED}You don't have a file open")
					editor
			}
		}
		else {
			player.sendMessage(t"${RED}You don't have the permission to use editors with files")
			editor.copy(text = CompTextLine(0, 0, editor.text.builtString.split('\n')))
		}
	}

	override def aliases: Seq[String] = Seq("saveFile")
	override def help: Text = t"If you have a file loaded, saves the file to disk"
	override def permission: String = LibPerm.UnsafeFile
}
