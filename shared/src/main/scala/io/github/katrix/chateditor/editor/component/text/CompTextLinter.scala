package io.github.katrix.chateditor.editor.component.text

import org.spongepowered.api.text.Text

abstract class CompTextLinter(pos: Int, select: Int, content: Seq[String]) extends CompTextLine(pos, select, content) {

	def lint: Text
	def prettify: Seq[String]
}
