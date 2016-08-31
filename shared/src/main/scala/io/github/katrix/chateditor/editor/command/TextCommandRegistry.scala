package io.github.katrix.chateditor.editor.command

import scala.collection.mutable

class TextCommandRegistry {

	private val commandMap: mutable.Map[String, TextCommand] = new mutable.HashMap[String, TextCommand]

	def register(command: TextCommand): Unit = command.aliases.foreach(commandMap.put(_, command))

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