package io.github.katrix.chateditor.editor.command

import scala.collection.mutable

import io.github.katrix.chateditor.editor.command.core.{ECmdEnd, ECmdHelp, ECmdPosSelect, ECmdSetEnd, ECmdSetText, ECmdText, ECmdView}
import io.github.katrix.katlib.KatPlugin

class EditorCommandRegistry {

	private val commandMap: mutable.Map[String, EditorCommand] = new mutable.HashMap[String, EditorCommand]

	def register(command: EditorCommand): Unit = command.aliases.foreach(commandMap.put(_, command))

	def getCommand(raw: String): Option[EditorCommand] = {
		if(raw.startsWith("!")) {
			val commandString = raw.substring(1)

			//We need to iterate over all the possible values as a TextCommand can have args together with the command
			//For example p=5
			for(i <- commandString.indices) {
				val subString = commandString.take(i + 1)

				if(commandMap.contains(subString)) {
					return commandMap.get(subString)
				}
			}

			None
		}
		else {
			Some(ECmdText)
		}
	}

	def registerCore(plugin: KatPlugin): Unit = {
		//Note ECmdText should NOT be registered
		val cmds = Seq(
			new ECmdEnd(plugin),
			ECmdHelp,
			ECmdPosSelect,
			ECmdSetEnd,
			ECmdSetText,
			ECmdView
		)

		cmds.foreach(register)
	}

	def registerFeatures(): Unit = {
		val cmds = Seq(
			ECmdCopy,
			ECmdCut,
			ECmdPaste,

			ECmdLintHocon,
			ECmdLintJson,
			ECmdPrettifyHocon,
			ECmdPrettifyJson
		)

		cmds.foreach(register)
	}
}