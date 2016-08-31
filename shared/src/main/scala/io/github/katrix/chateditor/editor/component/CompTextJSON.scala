package io.github.katrix.chateditor.editor.component

import scala.util.{Failure, Success, Try}

import org.spongepowered.api.text.Text
import org.spongepowered.api.text.format.TextColors._

import com.google.gson.{GsonBuilder, JsonParser}

import io.github.katrix.katlib.helper.Implicits._

class CompTextJSON(pos: Int, select: Int, content: Seq[String]) extends CompTextLinter(pos, select, content) {
	private val parser = new JsonParser
	private val gson = (new GsonBuilder).setPrettyPrinting().create()

	override def lint: Text = {
		Try(parser.parse(content.mkString)) match {
			case Failure(e) => e.getMessage.text
			case Success(_) => t"${GREEN}All is well"
		}
	}

	override def prettify: Seq[String] = {
		Try(parser.parse(content.mkString)) match {
			case Success(tree) => gson.toJson(tree).split('\n')
			case Failure(e) => content
		}
	}
}
