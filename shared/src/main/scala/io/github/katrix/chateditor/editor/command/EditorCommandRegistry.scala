package io.github.katrix.chateditor.editor.command

import scala.collection.mutable

import io.github.katrix.chateditor.EditorPlugin
import io.github.katrix.chateditor.editor.command.core.{ECmdAddLine, ECmdEnd, ECmdHelp, ECmdPosSelect, ECmdSetEnd, ECmdSetText, ECmdText, ECmdView}

class EditorCommandRegistry {

  private val commandMap: mutable.Map[String, EditorCommand] = new mutable.HashMap[String, EditorCommand]

  def register(command: EditorCommand): Unit = command.aliases.foreach(commandMap.put(_, command))

  def getCommand(raw: String): Option[EditorCommand] =
    if (raw.startsWith("!")) {
      val commandString = raw.substring(1)

      //We need to iterate over all the possible values as a TextCommand can have args together with the command
      val potentialCommands = commandString.indices.flatMap { i =>
        val subString = commandString.take(i + 1)
        commandMap.get(subString).map(cmd => (subString, cmd))
      }

      //We try to select the longest valid command. A long command is normally more specific than a short one
      potentialCommands.sortBy { case (string, _) => string.length }.lastOption.map(_._2)
    } else {
      Some(ECmdText)
    }

  def registerCore(implicit plugin: EditorPlugin): Unit = {
    //Note ECmdText should NOT be registered
    val cmds = Seq(new ECmdEnd, new ECmdHelp(this), new ECmdPosSelect, new ECmdSetEnd, new ECmdSetText, ECmdView, new ECmdAddLine)

    cmds.foreach(register)
  }

  def registerFeatures(implicit plugin: EditorPlugin): Unit = {
    val cmds = Seq(
      new ECmdCopy,
      new ECmdCut,
      new ECmdPaste,
      new ECmdLintHocon,
      new ECmdLintJson,
      ECmdPrettifyHocon,
      ECmdPrettifyJson,
      new ECmdReload,
      new ECmdSave
    )

    cmds.foreach(register)
  }

  def registeredCommands: Map[String, EditorCommand] = Map(commandMap.toSeq: _*)
}
