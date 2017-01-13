package io.github.katrix.chateditor.editor.component.end

import java.nio.file.Path

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

import org.spongepowered.api.text.format.TextColors._

import io.github.katrix.chateditor.EditorPlugin
import io.github.katrix.chateditor.editor.Editor
import io.github.katrix.chateditor.editor.component.EndComponent
import io.github.katrix.chateditor.editor.component.text.FileEditorHelper
import io.github.katrix.chateditor.lib.LibPerm
import io.github.katrix.katlib.helper.Implicits._

class CompEndSave(implicit plugin: EditorPlugin) extends EndComponent {

  override def end(editor: Editor): Option[Editor] =
    if (editor.player.get.exists(_.hasPermission(LibPerm.UnsafeFile))) {
      editor.text.data("path") match {
        case Some(path: Path) =>
          FileEditorHelper.save(path, editor).onComplete {
            case Success(_) =>
              editor.player.get.foreach(p => {
                p.sendMessage(t"${GREEN}File saved. Exited editor")
                editor.listener.removeEditorPlayer(p)
              })
              None
            case Failure(_) => editor.player.get.foreach(_.sendMessage(t"${RED}Error when saving file."))
          }
          editor.player.get.foreach(_.sendMessage(t"${YELLOW}Saving. Please  wait..."))
          Some(editor)
        case _ =>
          editor.player.get.foreach(_.sendMessage(t"${RED}Incompatible end behavior"))
          None
      }
    } else {
      editor.player.get.foreach(_.sendMessage(t"${RED}You don't have the permissions to do this"))
      Some(editor)
    }
}
