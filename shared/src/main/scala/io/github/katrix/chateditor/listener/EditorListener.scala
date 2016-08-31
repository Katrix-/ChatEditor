package io.github.katrix.chateditor.listener

import scala.collection.mutable

import org.spongepowered.api.block.BlockTypes
import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.{Listener, Order}
import org.spongepowered.api.event.block.InteractBlockEvent
import org.spongepowered.api.event.command.{SendCommandEvent, TabCompleteEvent}
import org.spongepowered.api.event.filter.cause.First
import org.spongepowered.api.event.message.MessageChannelEvent
import org.spongepowered.api.text.format.TextColors._

import io.github.katrix.chateditor.editor.Editor
import io.github.katrix.chateditor.editor.command.TextCommandRegistry
import io.github.katrix.chateditor.editor.component.{CompEndCommandBlock, CompTextCursor, CompTextLine}
import io.github.katrix.katlib.helper.Implicits._

class EditorListener(textCommandRegistry: TextCommandRegistry) {

	val editorPlayers = new mutable.WeakHashMap[Player, Editor]

	@Listener
	def interactCommandBlock(event: InteractBlockEvent.Secondary, @First player: Player) {
		val blockSnapshot = event.getTargetBlock
		if(blockSnapshot.getState.getType == BlockTypes.COMMAND_BLOCK) {
			val optName = blockSnapshot.get(Keys.DISPLAY_NAME).toOption
			val permCmdBlock = optName match {
				case Some(name) => s"minecraft.commandblock.edit.block.${name.toPlain}"
				case None => "minecraft.commandblock.edit.block"
			}

			if(!player.hasPermission(permCmdBlock)) {
				blockSnapshot.getLocation.toOption match {
					case Some(location) =>
						event.setCancelled(true)
						editorPlayers.get(player) match {
							case Some(editor) => editor.end match {
								case componentEnd: CompEndCommandBlock =>
									val newEditor = editor.copy(end = new CompEndCommandBlock(location, player))
									editorPlayers.put(player, newEditor)
									player.sendMessage(t"${YELLOW}Edit location set to ${location.getBlockPosition}")
								case _ =>
							}
							case None =>
								location.getTileEntity.toOption match {
									case Some(tileEntity) =>
										val commandString = tileEntity.get(Keys.COMMAND).toOption match {
											case Some(string) => string
											case None => ""
										}
										player.sendMessage(
											s"""Now editing command block at ${location.getBiomePosition}.
													|Just start typing to fill in what should go into the commandblock.
													|Once you are done, write !end to submit the command. Write !help to get more help""".stripMargin.richText.info())
										editorPlayers.put(player, Editor(CompTextCursor(0, 0, commandString), new CompEndCommandBlock(location, player)))
									case None => player.sendMessage(t"${RED}Error while getting tile entity for command block")
								}
						}
					case None => player.sendMessage(t"${RED}Could not get location of the command block")
				}
			}
		}
	}

	@Listener(order = Order.FIRST)
	def onChat(event: MessageChannelEvent.Chat, @First player: Player) {
		editorPlayers.get(player) match {
			case Some(editor) =>
				val rawText = event.getRawMessage.toPlain
				textCommandRegistry.getCommand(rawText) match {
					case Some(command) =>
						val commandText = if(rawText.startsWith("!")) {
							rawText.substring(1)
						}
						else {
							rawText
						}
						editorPlayers.put(player, command.execute(commandText, editor, player))
						event.setCancelled(true)
					case None => player.sendMessage(t"${RED}Command not found")
				}
			case None =>
		}
	}

	@Listener(order = Order.FIRST)
	def onCommand(event: SendCommandEvent, @First player: Player) {
		editorPlayers.get(player) match {
			case Some(editor) =>
				val newText = editor.text.addString(s"/${event.getCommand} ${event.getArguments}")
				val newEditor = editor.copy(text = newText)
				newText.sendPreview(newEditor, player)
				editorPlayers.put(player, newEditor)
				event.setCancelled(true)
			case None =>
		}
	}

	@Listener
	def onTabComplete(event: TabCompleteEvent, @First player: Player) {
		editorPlayers.get(player) match {
			case Some(editor) =>
				editor.text match {
					case lineEditor: CompTextLine =>
						val suggestions = event.getTabCompletions
						if(suggestions.isEmpty) {
							suggestions.add(lineEditor.currentLine)
						}
				}
			case None =>
		}
	}
}
