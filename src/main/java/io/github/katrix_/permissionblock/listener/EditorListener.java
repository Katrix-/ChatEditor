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
package io.github.katrix_.permissionblock.listener;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;

import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.tileentity.TileEntity;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.command.SendCommandEvent;
import org.spongepowered.api.event.command.TabCompleteEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import io.github.katrix_.permissionblock.editor.EditorCommandBlock;
import io.github.katrix_.permissionblock.editor.IEditor;
import io.github.katrix_.permissionblock.editor.IEditorLine;
import io.github.katrix_.permissionblock.editor.commands.TextCommand;
import io.github.katrix_.permissionblock.editor.commands.TextCommandRegistry;

public class EditorListener {

	public static final Map<Player, IEditor> EDITOR_PLAYERS = new WeakHashMap<>();

	@Listener
	public void interactCommandBlock(InteractBlockEvent.Secondary event, @First Player player) {
		BlockSnapshot blockSnapshot = event.getTargetBlock();
		BlockType blockType = blockSnapshot.getState().getType();
		if(blockType != BlockTypes.COMMAND_BLOCK) return;

		Optional<Text> name = blockSnapshot.get(Keys.DISPLAY_NAME);
		String permCmdblock = name.isPresent() ? "minecraft.commandblock.edit.block." + name.get().toPlain() : "minecraft.commandblock.edit.block";
		if(player.hasPermission(permCmdblock) /*|| !player.hasPermission(LibPerm.INTERACT)*/) return;

		Optional<Location<World>> optLocation = blockSnapshot.getLocation();
		if(!optLocation.isPresent()) {
			player.sendMessage(Text.of(TextColors.RED, "Could not get location of the command block"));
			return;
		}

		Location<World> location = optLocation.get();
		if(EDITOR_PLAYERS.containsKey(player)) {

			IEditor editor = EDITOR_PLAYERS.get(player);
			if(editor instanceof EditorCommandBlock) {
				player.sendMessage(Text.of(TextColors.YELLOW, "Edit location set to " + location.getBlockPosition()));
				((EditorCommandBlock)editor).setLocation(location);
			}
		}
		else {
			Optional<TileEntity> optTile = location.getTileEntity();
			if(!optTile.isPresent()) {
				player.sendMessage(Text.of(TextColors.RED, "Error while getting tile entity for command block"));
				return;
			}

			TileEntity tile = optTile.get();
			Optional<String> command = tile.get(Keys.COMMAND);
			player.sendMessage(Text.of(TextColors.YELLOW, "Now editing command block at " + location.getBlockPosition()
					+ " . Just start typing to fill in what should go into the commandblock. \nOnce you are done, write !end to submit the command. Write !help to get more help"));
			EDITOR_PLAYERS.put(player, new EditorCommandBlock(location, command.orElse("")));
		}

		event.setCancelled(true);
	}

	@Listener(order = Order.FIRST) //Not normal chat
	public void onChat(MessageChannelEvent.Chat event, @First Player player) {
		if(!EDITOR_PLAYERS.containsKey(player)) return;

		String rawText = event.getRawMessage().toPlain();
		Optional<TextCommand> optCommand = TextCommandRegistry.getCommand(rawText);

		if(!optCommand.isPresent()) {
			player.sendMessage(Text.of(TextColors.RED, "Command not found"));
			return;
		}

		TextCommand command = optCommand.get();
		IEditor editor = EDITOR_PLAYERS.get(player);

		if(!command.getCompatibility().isInstance(editor)) {
			player.sendMessage(Text.of(TextColors.RED, "You can't use that command with this type of editor"));
			return;
		}

		/*
		if(!player.hasPermission(command.getPermission())) {
			player.sendMessage(Text.of(TextColors.RED, "You don't have the permission to use that command"));
		}
		*/

		if(rawText.startsWith("!")) {
			rawText = rawText.substring(1);
		}

		command.execute(rawText, editor, player);

		event.setCancelled(true);
	}

	@Listener(order = Order.FIRST) //Not a normal command
	public void onCommand(SendCommandEvent event, @First Player player) {
		if(!EDITOR_PLAYERS.containsKey(player)) return;

		IEditor editor = EDITOR_PLAYERS.get(player);
		editor.addString("/" + event.getCommand() + " " + event.getArguments());
		editor.sendFormatted(player);

		event.setCancelled(true);
	}

	@Listener
	public void onTabComplete(TabCompleteEvent event, @First Player player) {
		if(!EDITOR_PLAYERS.containsKey(player)) return;

		IEditor editor = EDITOR_PLAYERS.get(player);
		if(!(editor instanceof IEditorLine)) return;

		List<String> suggestions = event.getTabCompletions();
		if(!suggestions.isEmpty()) return;

		suggestions.add(((IEditorLine)editor).getCurrentLineContent());
	}
}
