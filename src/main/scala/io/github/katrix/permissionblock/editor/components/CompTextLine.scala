/*
 * This file is part of PermissionBlock, licensed under the MIT License (MIT).
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
package io.github.katrix.permissionblock.editor.components

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

import io.github.katrix.permissionblock.editor.Editor

class CompTextLine private(editor: Editor) extends ComponentText(editor) {
	private var stringList: mutable.Buffer[String] = new ArrayBuffer[String]()
	private var line                              = 0

	def this(editor: Editor, stringList: List[String]) {
		this(editor)
		this.stringList = stringList.toBuffer
		line = stringList.size - 1
	}

	def this(editor: Editor, string: String) {
		this(editor)
		stringList += string
		line = 0
	}

	def addString(string: String): Unit = {
		val stringLines = string.split('\n')
		for(singleLine <- stringLines) {
			addSingleLine(singleLine)
			line += 1
		}
	}

	private def addSingleLine(string: String): Unit = {
		stringList.update(line, string)
	}

	def addLine(): Boolean = {
		stringList.insert(line, "")
		true
	}

	def removeLine(): Boolean = {
		if(stringList.size <= 1) return false
		stringList.remove(line)
		true
	}

	override def pos: Int = line

	override def pos_=(location: Int): Unit = {
		line = location
		line = validateLinePos
	}

	override def pos_+=(amount: Int): Unit = {
		line += amount
		line = validateLinePos
	}

	override def pos_-=(amount: Int): Unit = {
		line += amount
		line = validateLinePos
	}

	def currentLineContent: String = stringList(line)

	def builtString: String = stringList.mkString

	def formatted: Seq[Text] = {
		val list = stringList.map(s => Text.of(s))
		var selected = list(line)
		selected = selected.toBuilder.color(TextColors.BLUE).build
		list(line) = selected

		def callBack(source: CommandSource, textLine: Int): Consumer[CommandSource] = {
			(src: CommandSource) => {
				line = textLine
				src match {
					case player: Player => sendFormatted(player)
					case _ =>
				}
			}
		}

		for(i <- list.indices) {
			val text = list(i)
			val pos = i

			text.toBuilder.onClick(TextActions.executeCallback((t: CommandSource) => callBack(t, pos)))
		}

		list
	}

	def sendFormatted(player: Player) {
		val text = formatted
		val builder = Sponge.getServiceManager.provideUnchecked(classOf[PaginationService]).builder
		builder.title(Text.of(TextColors.GRAY, "Line Editor"))
		builder.contents(text.asJava)
		builder.sendTo(player)
	}

	private def validateLinePos: Int = {
		val size = stringList.size

		if(line > size) {
			line = size
		}
		else if(line < 0) {
			line = 0
		}

		line
	}
}