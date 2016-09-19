package io.github.katrix.chateditor.lib

object LibPerm {

	final val ChatEditor = "chateditor"

	final val Editor = s"$ChatEditor.editor"
	final val ECmd = s"$Editor.command"
	final val ECmdLint = s"$ECmd.lint"
	final val ECmdPrettify = s"$ECmd.prettify"

	final val TextComp = s"$Editor.text"

	final val EndComp = s"$Editor.end"
	final val CommandBlock = s"$EndComp.commandblock"

	final val Unsafe = s"${ChatEditor}unsafe"
	final val UnsafeFile = s"$Unsafe.file"
}
