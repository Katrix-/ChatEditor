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
package io.github.katrix.chateditor.editor.commands

import scala.collection.mutable

object TextCommandRegistry {

	private val commandMap: mutable.Map[String, TextCommand] = new mutable.HashMap[String, TextCommand]

	def register(command: TextCommand): Unit = command.getAliases.foreach(commandMap.put(_, command))

	def getCommand(raw: String): Option[TextCommand] = {

		if(raw.startsWith("!")) {
			val commandString = raw.substring(1)

			for(i <- 0 until commandString.length) {
				val subString = commandString.substring(0, i)

				if(commandMap.contains(subString)) {
					return commandMap.get(subString)
				}
			}

			None
		}
		else {
			Some(TCmdText)
		}
	}
}