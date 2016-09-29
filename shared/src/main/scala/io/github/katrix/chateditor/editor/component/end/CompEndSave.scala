package io.github.katrix.chateditor.editor.component.end

import java.nio.file.Path

import scala.util.{Failure, Success}

import io.github.katrix.chateditor.EditorPlugin
import io.github.katrix.chateditor.editor.Editor
import io.github.katrix.chateditor.editor.component.EndComponent
import io.github.katrix.chateditor.editor.component.text.FileEditorHelper
import io.github.katrix.chateditor.lib.LibPerm

class CompEndSave(implicit plugin: EditorPlugin) extends EndComponent {

	override def end(editor: Editor): Option[Editor] = {
		if(editor.player.get.exists(_.hasPermission(LibPerm.UnsafeFile))) {
			editor.text.data("path") match {
				case Some(path: Path) => FileEditorHelper.save(path, editor) match {
					case Success(_) =>
						editor.player.get.foreach(_.sendMessage(plugin.config.text.endFileSaveSuccess.value))
						None
					case Failure(_) =>
						editor.player.get.foreach(_.sendMessage(plugin.config.text.endFileSaveFailed.value))
						Some(editor)
				}
				case _ => editor.player.get.foreach(_.sendMessage(plugin.config.text.endIncompatible.value))
					None
			}
		}
		else {
			editor.player.get.foreach(_.sendMessage(plugin.config.text.fileMissingPerm.value))
			Some(editor)
		}
	}
}
