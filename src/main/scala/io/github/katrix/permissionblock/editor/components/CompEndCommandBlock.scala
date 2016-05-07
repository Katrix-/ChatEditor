/*
 * This file is part of PermissionBlock, licensed under the MIT License (MIT).
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
package io.github.katrix.permissionblock.editor.components

import java.util.Optional

import org.spongepowered.api.Sponge
import org.spongepowered.api.block.tileentity.{TileEntity, TileEntityTypes}
import org.spongepowered.api.command.CommandMapping
import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.format.TextColors
import org.spongepowered.api.world.{Location, World}

import io.github.katrix.permissionblock.editor.Editor

class CompEndCommandBlock(editor: Editor, var location: Location[World], player: Player) extends ComponentEnd(editor) {

	def end(): Boolean = {
		val optTileEntity: Option[TileEntity] = location.getTileEntity
		val builtString = editor.text.builtString

		optTileEntity match {
			case Some(tileEntity) if tileEntity == TileEntityTypes.COMMAND_BLOCK =>
				val optMapping: Option[_ <: CommandMapping] = Sponge.getCommandManager.get(builtString, player)
				optMapping match {
					case Some(mapping) =>
						if(!mapping.getCallable.testPermission(player)) {
							player.sendMessage(Text.of(TextColors.RED, "You don't have the permissions for that command"))
							return false
						}

						val result = tileEntity.offer(Keys.COMMAND, builtString)
						if(result.isSuccessful) {
							player.sendMessage(Text.of(TextColors.GREEN, "Command set successfully"))
							return true
						}
						else {
							player.sendMessage(Text.of(TextColors.RED, "Something went wrong when setting the command in the commandblock"))
						}
					case None =>
						player.sendMessage(Text.of("No command by that name found"))
				}
			case None =>
				player.sendMessage(Text.of(TextColors.RED,
					"Did not find a commandblock at the given coordinates. If the commandblock moved, right click the new commandblock"))
		}

		false
	}

	private implicit def optionalToOption[A](optional: Optional[A]): Option[A] = {
		if(optional.isPresent) {
			Some(optional.get())
		}
		else {
			None
		}
	}
}