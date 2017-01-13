package io.github.katrix.chateditor.editor.command

import org.spongepowered.api.entity.living.player.Player

import io.github.katrix.chateditor.editor.Editor
import io.github.katrix.chateditor.editor.component.text.{CompTextCursor, CompTextLine}
import io.github.katrix.chateditor.lib.LibPerm

abstract class ECmdPrettify extends EditorCommand {

  override def execute(raw: String, editor: Editor, player: Player): Editor = {
    val newText = editor.text match {
      case cursor: CompTextCursor => cursor.copy(content = prettify(cursor.builtString).mkString("\n"))
      case line:   CompTextLine   => line.copy(content = prettify(line.builtString))
    }
    editor.copy(text = newText)
  }

  def prettify(string: String): Seq[String]

  override def permission: String = LibPerm.ECmdPrettify
}
