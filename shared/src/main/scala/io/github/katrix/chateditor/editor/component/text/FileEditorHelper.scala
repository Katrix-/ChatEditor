package io.github.katrix.chateditor.editor.component.text

import java.nio.file.{Files, Path}

import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Failure

import io.github.katrix.chateditor.EditorPlugin
import io.github.katrix.chateditor.editor.Editor
import io.github.katrix.katlib.helper.LogHelper

object FileEditorHelper {

  def save(path: Path, editor: Editor)(implicit plugin: EditorPlugin): Future[Path] = {
    val future = Future(Files.write(path, editor.text.builtString.split('\n').toSeq.asJava))
    future.onComplete {
      case Failure(e) => LogHelper.error("Failed to save file", e)
      case _          =>
    }

    future
  }

  def load(path: Path): Future[CompTextLine] = Future {
    val content = Files.readAllLines(path).asScala
    CompTextLine(0, 0, content).dataPut("path", path)
  }

  def loadOrCreate(path: Path): Future[CompTextLine] = Future {
    val create = path.toFile.createNewFile()
    val comp = if (create) {
      val content = Files.readAllLines(path).asScala
      CompTextLine(0, 0, content)
    } else CompTextLine(0, 0, Seq(""))

    comp.dataPut("path", path)
  }
}
