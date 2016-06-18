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
package io.github.katrix.chateditor.editor.components

import java.util.function.Consumer

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

import org.spongepowered.api.Sponge
import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.service.pagination.PaginationService
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.action.TextActions
import org.spongepowered.api.text.format.TextColors

import io.github.katrix.chateditor.editor.Editor

class CompTextLine private(editor: Editor) extends ComponentText(editor) {
	private var stringList: mutable.Buffer[String] = new ArrayBuffer[String]()
	private var _line                              = 0
	private var _select = _line

	def this(editor: Editor, stringList: List[String]) {
		this(editor)
		this.stringList = stringList.toBuffer
		_line = stringList.size - 1
		_select = _line
	}

	def this(editor: Editor, string: String) {
		this(editor)
		stringList += string
		_line = 0
	}

	def addString(string: String): Unit = {
		val stringLines = string.split('\n')
		for(singleLine <- stringLines) {
			addSingleLine(singleLine)
			_line += 1
		}
	}

	private def addSingleLine(string: String): Unit = {
		stringList.update(_line, string)
	}

	def addLine(): Boolean = {
		stringList.insert(_line, "")
		true
	}

	def removeLine(): Boolean = {
		if(stringList.size <= 1) return false
		stringList.remove(_line)
		true
	}

	override def pos: Int = _line

	override def pos_=(location: Int): Unit = _line = validateLinePos(location)

	override def pos_+=(amount: Int): Unit = _line = validateLinePos(_line + amount)

	override def pos_-=(amount: Int): Unit = _line = validateLinePos(_line + amount)

	override def select: Int = _select

	override def select_=(selectPos: Int): Unit = _select = validateSelectPos(selectPos)

	override def select_+=(amount: Int): Unit = _select = validateSelectPos(_select + amount)

	override def select_-=(amount: Int): Unit = _select = validateSelectPos(_select - amount)

	def currentLineContent: String = stringList(_line)

	override def builtString: String = stringList.mkString

	override def formatted: Seq[Text] = {
		val list = stringList.map(s => Text.of(s))

		val selectedRange = _line to _select
		val selected = selectedRange map(list(_))
		selected foreach(_.toBuilder color TextColors.BLUE build)
		var i = 0
		selectedRange foreach {elem =>
			list(elem) = selected(i)
			i += 1
		}

		val onLine = list(_line)
		list(_line) = onLine.toBuilder color TextColors.BLUE build

		def callBack(source: CommandSource, textLine: Int): Consumer[CommandSource] = (src: CommandSource) => {
			pos = textLine
			select = _line
			src match {
				case player: Player => sendFormatted(player)
				case _ =>
			}
		}

		for(i <- list.indices) {
			val text = list(i)
			val pos = i

			text.toBuilder.onClick(TextActions.executeCallback((t: CommandSource) => callBack(t, pos)))
		}

		list
	}

	override def sendFormatted(player: Player) {
		val text = formatted
		val builder = Sponge.getServiceManager.provideUnchecked(classOf[PaginationService]).builder
		builder.title(Text.of(TextColors.GRAY, "Line Editor"))
		builder.contents(text.asJava)
		builder.sendTo(player)
	}

	override def selectedText: String = {
		stringList.slice(_line, _select).mkString
	}

	override def replaceSelected(string: String): Unit = {
		stringList.remove(_line, _select)
		stringList.insertAll(_line, string.split('\n'))
	}

	private def validateLinePos(orig: Int): Int = validatePos(0, stringList.length, orig)

	private def validateSelectPos(orig: Int): Int = validatePos(_line, stringList.length, orig)

	private def validatePos(min: Int, max: Int, orig: Int): Int = {
		if(orig > max) {
			max
		}
		else if(orig < min) {
			min
		}
		else {
			orig
		}
	}

}