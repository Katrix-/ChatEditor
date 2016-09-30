package io.github.katrix.chateditor.editor.component.text

import java.nio.file.{Files, Path}

import scala.collection.JavaConverters._
import scala.io.Source
import scala.util.{Failure, Try}

import io.github.katrix.chateditor.EditorPlugin
import io.github.katrix.chateditor.editor.Editor
import io.github.katrix.katlib.helper.LogHelper

object FileEditorHelper {

	def save(path: Path, editor: Editor)(implicit plugin: EditorPlugin): Try[Path] = {
		val res = Try(Files.write(path, editor.text.builtString.split('\n').toSeq.asJava))
		res match {
			case Failure(e) =>
				LogHelper.error("Failed to save file", e)
				res
			case _ => res
		}
	}

	def load(path: Path): Try[CompTextLine] = {
		Try(Source.fromFile(path.toFile).getLines().toSeq).map(content => CompTextLine(0, 0, content).dataPut("path", path))
	}

	def loadOrCreate(path: Path): CompTextLine = {
		val create = Try(path.toFile.createNewFile())
		create.flatMap(b => Try(Files.readAllLines(path).asScala)
			.map(s => CompTextLine(0, 0, s)))
			.getOrElse(CompTextLine(0, 0, Seq(""))).dataPut("path", path)
	}
}