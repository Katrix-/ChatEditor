package io.github.katrix.chateditor.editor.command

import scala.collection.mutable

import io.github.katrix.chateditor.editor.command.core.TCmdText

class TextCommandRegistry {

	private val commandMap: mutable.Map[String, TextCommand] = new mutable.HashMap[String, TextCommand]

	def register(command: TextCommand): Unit = command.aliases.foreach(commandMap.put(_, command))

	def getCommand(raw: String): Option[TextCommand] = {
		if(raw.startsWith("!")) {
			val commandString = raw.substring(1)

			//We need to iterate over all the possible values as a TextCommand can have args together with the command
			//For example p=5
			for(i <- commandString.indices) {
				val subString = commandString.take(i)

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