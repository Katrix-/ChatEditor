package io.github.katrix.chateditor.editor.component.end

import java.nio.file.Path

import scala.util.{Failure, Success}

import org.spongepowered.api.text.format.TextColors._

import io.github.katrix.chateditor.editor.Editor
import io.github.katrix.chateditor.editor.component.EndComponent
import io.github.katrix.chateditor.editor.component.text.FileEditorHelper
import io.github.katrix.katlib.helper.Implicits._

object CompEndSave extends EndComponent {

	override def end(editor: Editor): Option[Editor] = editor.text.data("path") match {
		case Some(path: Path) => FileEditorHelper.save(path, editor) match {
			case Success(_) =>
				editor.player.get.foreach(_.sendMessage(t"${GREEN}Saved and closed file"))
				None
			case Failure(_) =>
				editor.player.get.foreach(_.sendMessage(t"${RED}Failed to save file. Not closing"))
				Some(editor)
		}
		case _ => editor.player.get.foreach(_.sendMessage(t"${RED}Invalid end component for: $editor"))
			None
	}
}
