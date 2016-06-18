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
package io.github.katrix.permissionblock.listener

import scala.collection.mutable

import org.spongepowered.api.block.BlockTypes
import org.spongepowered.api.block.tileentity.TileEntity
import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.block.InteractBlockEvent
import org.spongepowered.api.event.command.{SendCommandEvent, TabCompleteEvent}
import org.spongepowered.api.event.filter.cause.First
import org.spongepowered.api.event.message.MessageChannelEvent
import org.spongepowered.api.event.{Listener, Order}
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.format.TextColors
import org.spongepowered.api.world.{Location, World}

import io.github.katrix.permissionblock.editor.Editor
import io.github.katrix.permissionblock.editor.commands.{TextCommand, TextCommandRegistry}
import io.github.katrix.permissionblock.editor.components.{CompEndCommandBlock, CompTextCursor, CompTextLine}
import io.github.katrix.permissionblock.helper.Implicits._

object EditorListener {

	val EDITOR_PLAYERS = new mutable.WeakHashMap[Player, Editor]

	@Listener
	def interactCommandBlock(event: InteractBlockEvent.Secondary, @First player: Player) {
		val blockSnapshot = event.getTargetBlock
		val blockType = blockSnapshot.getState.getType
		if(blockType == BlockTypes.COMMAND_BLOCK) {
			val optName: Option[Text] = blockSnapshot.get(Keys.DISPLAY_NAME)
			val permCmdblock = optName match {
				case Some(name) => s"minecraft.commandblock.edit.block.${name.toPlain}"
				case None => "minecraft.commandblock.edit.block"
			}

			if(!player.hasPermission(permCmdblock)) {
				val optLocation: Option[Location[World]] = blockSnapshot.getLocation
				optLocation match {
					case Some(location) =>
						event.setCancelled(true)
						EDITOR_PLAYERS.get(player) match {
							case Some(editor) => editor.end match {
								case componentEnd: CompEndCommandBlock =>
									componentEnd.location = location
									player.sendMessage(s"Edit location set to ${location.getBlockPosition}".richText.info())
								case _ =>
							}
							case None =>
								val optTileEntity: Option[TileEntity] = location.getTileEntity
								optTileEntity match {
									case Some(tileEntity) =>
										val command: Option[String] = tileEntity.get(Keys.COMMAND)
										val commandString = command match {
											case Some(string) => string
											case None => ""
										}
										player.sendMessage(
											s"""Now editing command block at ${location.getBiomePosition}.
													|Just start typing to fill in what should go into the commandblock.
													|Once you are done, write !end to submit the command. Write !help to get more help""".stripMargin.richText.info())
										EditorListener.EDITOR_PLAYERS.put(player, new Editor(new CompTextCursor(_, commandString), new CompEndCommandBlock(_, location,
											player)))
									case None =>
										player.sendMessage("Error while getting tile entity for command block".richText.error())
								}
						}
					case None => player.sendMessage("Could not get location of the command block".richText.error())
				}
			}
		}
	}

	//TODO
	@Listener(order = Order.FIRST)
	def onChat(event: MessageChannelEvent.Chat, @First player: Player) {
		EDITOR_PLAYERS.get(player) match {
			case Some(editor) =>
				val rawText = event.getRawMessage.toPlain
				val optCommand: Option[TextCommand] = TextCommandRegistry.getCommand(rawText)
				optCommand match {
					case Some(command) =>
						if(editor.hasComponent(command.getCompatibility)) {

							val commandText = if(rawText.startsWith("!")) {
								rawText.substring(1)
							}
							else {
								rawText
							}
							command.execute(commandText, editor, player)
							event.setCancelled(true)
						}
						else {
							player.sendMessage("You can't use that command with this type of editor".richText.error())
						}
					case None => player.sendMessage("Command not found".richText.error())
				}
			case None =>
		}
	}

	@Listener(order = Order.FIRST)
	def onCommand(event: SendCommandEvent, @First player: Player) {
		EDITOR_PLAYERS.get(player) match {
			case Some(editor) =>
				val componentText = editor.text
				componentText.addString("/" + event.getCommand + " " + event.getArguments)
				componentText.sendFormatted(player)
				event.setCancelled(true)
			case None =>
		}
	}

	@Listener
	def onTabComplete(event: TabCompleteEvent, @First player: Player) {
		EDITOR_PLAYERS.get(player) match {
			case Some(editor) =>
				editor.text match {
					case lineEditor: CompTextLine =>
						val suggestions = event.getTabCompletions
						if(suggestions.isEmpty) {
							suggestions.add(lineEditor.currentLineContent)
						}
				}
			case None =>
		}
	}
}