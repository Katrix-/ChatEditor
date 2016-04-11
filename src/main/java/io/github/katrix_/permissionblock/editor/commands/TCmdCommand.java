/**
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
package io.github.katrix_.permissionblock.editor.commands;

import java.util.List;
import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandManager;
import org.spongepowered.api.command.CommandMapping;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.google.common.collect.ImmutableList;

import io.github.katrix_.permissionblock.editor.IEditor;

public class TCmdCommand extends TextCommand {

	@Override
	public void execute(String raw, IEditor editor, Player player) {
		String[] splitString = raw.split(" ");

		if(splitString[0].startsWith("/")) {
			splitString[0] = splitString[0].substring(1);
		}

		CommandManager manager = Sponge.getCommandManager();
		Optional<? extends CommandMapping> mapping = manager.get(splitString[0], player);

		if(!mapping.isPresent()) {
			player.sendMessage(Text.of(TextColors.RED, "No such command found"));
			return;
		}

		CommandCallable callable = mapping.get().getCallable();

		if(!callable.testPermission(player)) {
			player.sendMessage(Text.of(TextColors.RED, "You don't have the permissions to use that command"));
			return;
		}

		try {
			callable.process(player, String.join(" ", splitString));
		}
		catch(CommandException e) {
			Text text = e.getText();
			player.sendMessage(text == null ? Text.of("Something went wrong when trying to use that command") : text);
		}
	}

	@Override
	public List<String> getAliases() {
		return ImmutableList.of("command");
	}

	@Override
	public Text getHelp() {
		return null;
	}

	@Override
	public String getPermission() {
		return null;
	}
}
