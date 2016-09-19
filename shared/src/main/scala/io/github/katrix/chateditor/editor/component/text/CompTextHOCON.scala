package io.github.katrix.chateditor.editor.component.text

import java.io.{BufferedReader, BufferedWriter, StringReader, StringWriter}

import scala.util.{Failure, Success, Try}

import org.spongepowered.api.text.Text
import org.spongepowered.api.text.format.TextColors._

import io.github.katrix.katlib.helper.Implicits._
import ninja.leaping.configurate.hocon.HoconConfigurationLoader

class CompTextHOCON(pos: Int, select: Int, content: Seq[String]) extends CompTextLinter(pos, select, content) {

	private def loader(string: String, writer: Option[StringWriter]): HoconConfigurationLoader = {
		val builder = HoconConfigurationLoader.builder()
			.setSource(() => new BufferedReader(new StringReader(string)))
		writer.foreach(w => builder.setSink(() => new BufferedWriter(w)))
		builder.build()
	}

	override def lint: Text = {
		Try(loader(builtString, None).load()) match {
			case Failure(e) => t"$RED${e.getMessage}"
			case Success(_) => t"${GREEN}All is well"
		}
	}

	override def prettify: Seq[String] = {
		val writer = new StringWriter()
		val configLoader = loader(builtString, Some(writer))
		Try(configLoader.load()).map(n => configLoader.save(n)) match {
			case Success(root) => writer.toString.split('\n')
			case Failure(e) => content
		}
	}
}
