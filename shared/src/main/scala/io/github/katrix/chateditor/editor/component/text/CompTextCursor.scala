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

import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.format.TextColors._

import io.github.katrix.chateditor.editor.Editor
import io.github.katrix.chateditor.editor.component.TextComponent
import io.github.katrix.katlib.helper.Implicits._

case class CompTextCursor(pos: Int, select: Int, content: String, dataMap: Map[String, Any]) extends TextComponent {
	require(pos >= 0)
	require(select >= pos)
	require(select <= content.length)

	override type Preview = Text
	override type Self = CompTextCursor

	override def builtString: String = content
	override def selectedString: String = content.substring(pos, select)

	override def preview(editor: Editor): Text = {
		val top = content.substring(0, pos)
		val selected = content.substring(pos, if(hasSelection) select + 1 else select)
		val bottom = content.substring(if(hasSelection) select + 1 else select, content.length)
		//TODO: Callback
		t"$top$BLUE[$selected]$RESET$bottom"
	}
	override def selectedPreview(editor: Editor): Text = t"$BLUE$selectedString"
	override def sendPreview(editor: Editor, player: Player): Unit = player.sendMessage(preview(editor))

	override def addString(string: String): Self = {
		if(hasSelection) {
			val builder = new StringBuilder(content)
			builder.replace(pos, select, string)
			copy(content = builder.mkString)
		}
		else copy(content = content + string)
	}

	override def pos_=(pos: Int): Self = copy(pos = clamp(0, content.length, pos))
	override def select_=(select: Int): Self = copy(select = clamp(pos, content.length, select))

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
