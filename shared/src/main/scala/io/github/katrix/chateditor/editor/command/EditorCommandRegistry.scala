package io.github.katrix.chateditor.editor.command

import scala.collection.mutable

import io.github.katrix.chateditor.editor.command.core.{ECmdAddLine, ECmdEnd, ECmdHelp, ECmdPosSelect, ECmdSetEnd, ECmdSetText, ECmdText, ECmdView}
import io.github.katrix.katlib.KatPlugin

class EditorCommandRegistry {

	private val commandMap: mutable.Map[String, EditorCommand] = new mutable.HashMap[String, EditorCommand]

	def register(command: EditorCommand): Unit = command.aliases.foreach(commandMap.put(_, command))

	def getCommand(raw: String): Option[EditorCommand] = {
		if(raw.startsWith("!")) {
			val commandString = raw.substring(1)

			//We need to iterate over all the possible values as a TextCommand can have args together with the command
			val potentialCommands = commandString.indices.flatMap {i =>
				val subString = commandString.take(i + 1)

				if(commandMap.contains(subString)) {
					Some((subString, commandMap(subString)))
				}
				else None
			}

			//We try to select the longest valid command. A long command is normally more specific than a short one
			val longestCmd = potentialCommands.foldLeft(None: Option[(String, EditorCommand)]) {
				case (prevSome @ Some((prevString, _)), tuple @ (string, _)) =>
					if(prevString.length > string.length) prevSome else Some(tuple)
				case (None, tuple) => Some(tuple)
			}

			longestCmd.map(_._2)
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
			ECmdView,
			ECmdAddLine
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