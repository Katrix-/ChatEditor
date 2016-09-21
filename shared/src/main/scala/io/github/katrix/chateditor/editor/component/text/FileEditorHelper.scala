package io.github.katrix.chateditor.editor.component.text

import java.nio.file.{Files, Path}

import scala.collection.JavaConverters._
import scala.io.Source
import scala.util.Try

import io.github.katrix.chateditor.editor.Editor

object FileEditorHelper {

	def save(path: Path, editor: Editor): Try[Unit] = Try(Files.write(path, editor.text.builtString.split('\n').toSeq.asJava))

	def load(path: Path): Try[CompTextLine] = {
		Try(Source.fromFile(path.toFile).getLines().toSeq).map(content => CompTextLine(0, 0, content).dataPut("path", path))
	}

	def loadOrCreate(path: Path): CompTextLine = {
		Try(Files.readAllLines(path).asScala).map(s => CompTextLine(0, 0, s)).getOrElse(CompTextLine(0, 0, Seq(""))).dataPut("path", path)
	}
}