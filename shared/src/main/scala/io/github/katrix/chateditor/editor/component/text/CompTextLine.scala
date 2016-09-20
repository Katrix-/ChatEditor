/*
 * This file is part of ChatEditor, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2016 Katrix
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package io.github.katrix.chateditor.editor.component.text

import scala.collection.JavaConverters._

import org.spongepowered.api.Sponge
import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.service.pagination.PaginationService
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.action.TextActions
import org.spongepowered.api.text.format.TextColors._

import io.github.katrix.chateditor.editor.Editor
import io.github.katrix.chateditor.editor.component.TextComponent
import io.github.katrix.katlib.helper.Implicits._

case class CompTextLine(pos: Int, select: Int, content: Seq[String], dataMap: Map[String, Any]) extends TextComponent {
	require(pos >= 0)
	require(select >= pos)
	require(pos <= content.size)
	require(select <= content.size)

	override type Preview = Seq[Text]
	override type Self = CompTextLine

	def currentLine: String = content(pos)

	override def builtString: String = content.mkString("\n")
	override def selectedString: String = content.slice(pos, select).mkString("\n")

	override def preview(editor: Editor): Seq[Text] = {
		val raw = content.map(_.text)

		val currentLine = raw(pos).toBuilder.color(AQUA).build()
		val selectedLines = currentLine +: (pos to select map raw).drop(1).map(_.toBuilder.color(BLUE).build())

		val clickCallback: (CommandSource, Int) => Unit = (src, textLine) => {
			val newCompText = copy(pos = textLine, select = textLine)
			editor.useNewTextComponent(newCompText)

			src match {
				case player: Player => newCompText.sendPreview(editor, player)
				case _ =>
			}
		}

		val shiftCallback: (Int) => String = (textLine) => {
			val (newPos, newSelect) = if(textLine < pos) (textLine, pos) else (pos, textLine)
			val newCompText = copy(pos = newPos, select = newSelect)
			editor.useNewTextComponent(newCompText)

			/*
			src match {
				case player: Player => newCompText.sendPreview(editor, player)
				case _ =>
			}
			*/
			""
		}

		val display = raw.take(pos) ++ selectedLines ++ raw.drop(select + pos)
		val interactive = display.indices.map(i => display(i).toBuilder
			.onClick(TextActions.executeCallback(src => clickCallback(src, i)))
			.onShiftClick(TextActions.insertText(shiftCallback(i)))
			.build())

		interactive
	}
	override def selectedPreview(editor: Editor): Seq[Text] = content.slice(pos, select).map(s => t"$s")
	override def sendPreview(editor: Editor, player: Player): Unit = {
		val text = preview(editor)
		val builder = Sponge.getServiceManager.provideUnchecked(classOf[PaginationService]).builder
		builder.title(t"${GRAY}Line Editor")
		builder.contents(text.asJava)
		builder.sendTo(player)
	}

	override def addString(string: String): Self = if(hasSelection) {
		val newStrings = string.split('\n')
		val top = content.take(pos)
		val bottom = content.drop(pos + select)
		copy(content = top ++ newStrings ++ bottom)
	}
	else copy(content = content ++ string.split('\n'))

	override def pos_=(pos: Int): Self = copy(pos = clamp(0, content.size, pos))
	override def select_=(selected: Int): Self = copy(select = clamp(pos, content.size, selected))

	private def clamp(min: Int, max: Int, orig: Int): Int = {
		if(orig > max) max
		else if(orig < min) min
		else orig
	}

	override def data(key: String): Option[Any] = dataMap.get(key)
	override def dataPut(key: String, value: Any): Self = copy(dataMap = dataMap + ((key, value)))
	override def dataRemove(key: String): Self = copy(dataMap = dataMap.filterKeys(_ != key))
	override def dataRemove(value: Any): Self = copy(dataMap = dataMap.filter { case (_, otherVal) => otherVal != value})
}
