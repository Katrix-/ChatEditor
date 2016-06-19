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

/**
	* The place where a string is mapped to a [[TextCommand]]
	*/
object TextCommandRegistry {

	private val commandMap: mutable.Map[String, TextCommand] = new mutable.HashMap[String, TextCommand]

	/**
		* Register a [[TextCommand]] to the command registry so that it can be used.
		*/
	def register(command: TextCommand): Unit = command.getAliases.foreach(commandMap.put(_, command))

	/**
		* Get a [[TextCommand]] from a string, if any registered command has an alias like that.
		*
		* <p>Shorter commands are returned before longer ones.
		* This meanns that if you have two commands that have the aliases
		* `foo` and `foobar`, then the command that has the alias `foo` will always be returned.</p>
		*/
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