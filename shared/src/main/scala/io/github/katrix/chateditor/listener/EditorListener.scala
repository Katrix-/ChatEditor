package io.github.katrix.chateditor.listener

import scala.collection.mutable
import scala.ref.WeakReference

import org.spongepowered.api.block.BlockTypes
import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.block.InteractBlockEvent
import org.spongepowered.api.event.command.{SendCommandEvent, TabCompleteEvent}
import org.spongepowered.api.event.filter.cause.First
import org.spongepowered.api.event.message.MessageChannelEvent
import org.spongepowered.api.event.{Listener, Order}
import org.spongepowered.api.text.format.TextColors._

import io.github.katrix.chateditor.editor.Editor
import io.github.katrix.chateditor.editor.command.TextCommandRegistry
import io.github.katrix.chateditor.editor.component.text.{CompTextCursor, CompTextLine}
import io.github.katrix.chateditor.editor.component.CompTextLine
import io.github.katrix.chateditor.editor.component.end.CompEndCommandBlock
import io.github.katrix.chateditor.lib.LibPerm
import io.github.katrix.katlib.helper.Implicits._

class EditorListener(textCommandRegistry: TextCommandRegistry) {

	val editorPlayers = new mutable.WeakHashMap[Player, Editor]

	@Listener
	def interactCommandBlock(event: InteractBlockEvent.Secondary, @First player: Player): Unit = {
		if(player.get(Keys.IS_SNEAKING).orElse(false)) {
			val blockSnapshot = event.getTargetBlock
			if(blockSnapshot.getState.getType == BlockTypes.COMMAND_BLOCK) {
				val optName = blockSnapshot.get(Keys.DISPLAY_NAME).toOption
				val permCmdBlock = optName match {
					case Some(name) => s"${LibPerm.CommandBlock}.${name.toPlain}"
					case None => LibPerm.CommandBlock
				}

				if(player.hasPermission(permCmdBlock)) {
					blockSnapshot.getLocation.toOption match {
						case Some(location) => editorPlayers.get(player) match {
							case Some(editor) => editor.end match {
								case componentEnd: CompEndCommandBlock =>
									event.setCancelled(true)

									val newEditor = editor.copy(end = new CompEndCommandBlock(location))
									editorPlayers.put(player, newEditor)
									player.sendMessage(t"${YELLOW}Edit location set to ${location.getBlockPosition}")
								case _ =>
							}
							case None =>
								location.getTileEntity.toOption match {
									case Some(tileEntity) =>
										event.setCancelled(true)

										val commandString = tileEntity.get(Keys.COMMAND).orElse("")
										player.sendMessage(
											//We use the old methods here as we need to strip the margin
											s"""Now editing command block at ${location.getBlockPosition}.
													|Just start typing to fill in what should go into the commandblock.
													|Once you are done, write !end to submit the command. Write !help to get more help""".stripMargin.richText.info())
										val text = CompTextCursor(0, 0, commandString)
										val end = new CompEndCommandBlock(location)
										editorPlayers.put(player, Editor(text, end, WeakReference(player))(this))
									case None => player.sendMessage(t"${RED}Error while getting tile entity for command block")
								}
						}
						case None => player.sendMessage(t"${RED}Could not get location of the command block")
					}
				}
			}
		}
	}

	@Listener(order = Order.FIRST)
	def onChat(event: MessageChannelEvent.Chat, @First player: Player): Unit = {
		editorPlayers.get(player) match {
			case Some(editor) =>
				val rawText = event.getRawMessage.toPlain
				textCommandRegistry.getCommand(rawText) match {
					case Some(command) /*if player.hasPermission(command.permission)*/ =>
						val commandText = if(rawText.startsWith("!")) rawText.substring(1) else rawText
						editorPlayers.put(player, command.execute(commandText, editor, player))
						event.setCancelled(true)
					case Some(command) => player.sendMessage(t"${RED}You don't have permissions to use that command")
					case None => player.sendMessage(t"${RED}Command not found")
				}
			case None =>
		}
	}

	@Listener(order = Order.FIRST)
	def onCommand(event: SendCommandEvent, @First player: Player): Unit = {
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
	def onTabComplete(event: TabCompleteEvent, @First player: Player): Unit = {
		editorPlayers.get(player) match {
			case Some(editor) =>
				editor.text match {
					case lineEditor: CompTextLine =>
						val suggestions = event.getTabCompletions
						if(suggestions.isEmpty) {
							suggestions.add(lineEditor.currentLine)
						}
					case _ =>
				}
			case None =>
		}
	}
}
