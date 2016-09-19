package io.github.katrix.chateditor.editor.component.text

import java.nio.file.{Files, Path}

import scala.collection.JavaConverters._
import scala.io.Source
import scala.util.Try

class CompTextFile(pos: Int, select: Int, content: Seq[String], path: Path) extends CompTextLine(pos, select, content) {

	def save: Try[Unit] = Try(Files.write(path, builtString.split('\n').toSeq.asJava))
	def reload: Try[CompTextFile] = CompTextFile.load(path)
}
object CompTextFile {

	def load(path: Path): Try[CompTextFile] = {
		Try(Source.fromFile(path.toFile).getLines().toSeq).map(content => new CompTextFile(0, 0, content, path))
	}

	def loadOrCreate(path: Path): CompTextLine = {
		Try(Files.readAllLines(path).asScala).map(s => new CompTextFile(0, 0, s, path)).getOrElse(new CompTextFile(0, 0, Seq(""), path))
	}
}