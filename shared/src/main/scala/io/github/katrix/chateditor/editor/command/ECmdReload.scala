package io.github.katrix.chateditor.editor.command

import java.nio.file.Path

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.format.TextColors._

import io.github.katrix.chateditor.EditorPlugin
import io.github.katrix.chateditor.editor.Editor
import io.github.katrix.chateditor.editor.component.text.FileEditorHelper
import io.github.katrix.chateditor.lib.LibPerm
import io.github.katrix.katlib.helper.Implicits._

class ECmdReload(implicit plugin: EditorPlugin) extends EditorCommand {

  override def execute(raw: String, editor: Editor, player: Player): Editor =
    //We make sure to be paranoid about this
    if (player.hasPermission(LibPerm.UnsafeFile)) {
      editor.text.data("path") match {
        case Some(path: Path) =>
          FileEditorHelper.load(path).onComplete {
            case Success(newText) =>
              player.sendMessage(t"${GREEN}File reloaded")
              editor.listener.addEditorPlayer(player, editor.copy(text = newText))
            case Failure(e) => player.sendMessage(t"$RED${e.getMessage}")
          }

          editor
        case _ =>
          player.sendMessage(t"${GREEN}File not open")
          editor
      }
    } else {
      player.sendMessage(t"${RED}You don't have the permissions to do this")
      editor.copy(text = editor.text.dataRemove("path"))
    }

  override def aliases:    Seq[String] = Seq("saveFile")
  override def help:       Text        = t"If you have a file loaded, reloads the content of the file from disk"
  override def permission: String      = LibPerm.UnsafeFile
}
