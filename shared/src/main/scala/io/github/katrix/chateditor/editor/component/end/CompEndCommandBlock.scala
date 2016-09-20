package io.github.katrix.chateditor.editor.component.end

import org.spongepowered.api.Sponge
import org.spongepowered.api.block.tileentity.TileEntityTypes
import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.text.format.TextColors._
import org.spongepowered.api.world.{Location, World}

import io.github.katrix.chateditor.editor.Editor
import io.github.katrix.chateditor.editor.component.EndComponent
import io.github.katrix.katlib.helper.Implicits._

class CompEndCommandBlock(location: Location[World]) extends EndComponent {

	override def end(editor: Editor): Option[Editor] = {
		editor.player.get match {
			case Some(player) =>
				val optTileEntity = location.getTileEntity.toOption
				val builtString = editor.text.builtString
				optTileEntity match {
					case Some(tileEntity) if tileEntity.getType == TileEntityTypes.COMMAND_BLOCK =>
						Sponge.getCommandManager.get(builtString.split(' ').head, player).toOption match {
							case Some(mapping) if mapping.getCallable.testPermission(player) =>
								val result = tileEntity.offer(Keys.COMMAND, builtString)
								if(result.isSuccessful) {
									player.sendMessage(t"${GREEN}Command set successfully")
									None
								}
								else {
									tileEntity.undo(result)
									player.sendMessage(t"${RED}Something went wrong when setting the command in the commandblock")
									Some(editor)
								}
							case Some(_) =>
								player.sendMessage(t"${RED}You don't have the permissions for that command")
								Some(editor)
							case None =>
								player.sendMessage(t"${RED}No command by that name found")
								Some(editor)
						}
					case None | Some(_) =>
						player.sendMessage(
							t"${RED}Did not find a commandblock at the specified location. If the commandblock moved, right click the new commandblock")
						Some(editor)
				}
			case None => None //If no player is found, just remove the editor and call it done
		}
	}
}
