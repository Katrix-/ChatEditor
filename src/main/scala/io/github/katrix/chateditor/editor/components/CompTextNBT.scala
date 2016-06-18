package io.github.katrix.permissionblock.editor.components

import org.spongepowered.api.entity.living.player.Player

import io.github.katrix.permissionblock.editor.Editor
import io.github.katrix.spongynbt.nbt.NBTCompound
import io.github.katrix.permissionblock.helper.Implicits._
import io.github.katrix.spongynbt.nbt.parser.{NBTParseException, NBTParser}

class CompTextNBT(editor: Editor, string: String, player: Player) extends CompTextLine(editor, string) {

	override def addString(string: String): Unit = {
		super.addString(string)
		parseCurrent()
	}

	def parseCurrent(): NBTCompound = {
		parseNBT(player, builtString)
	}

	def parseNBT(player: Player, nbtString: String): NBTCompound = {
		try {
			return NBTParser.parse(nbtString)
		}
		catch {
			case e: NBTParseException =>
				player.sendMessage(e.message.text) //TODO: Proper error message
		}

		new NBTCompound
	}
}
