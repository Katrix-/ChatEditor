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
package io.github.katrix_.permissionblock.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import io.github.katrix_.permissionblock.editor.EditorChat;
import io.github.katrix_.permissionblock.lib.LibPerm;
import io.github.katrix_.permissionblock.listener.EditorListener;

public class CmdEditor implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(!(src instanceof Player)) {
			src.sendMessage(Text.of(TextColors.RED, "Only players can open a editor"));
			return CommandResult.empty();
		}

		EditorListener.EDITOR_PLAYERS.put((Player)src, new EditorChat(""));
		src.sendMessage(Text.of("You are now in a editor. Just start typing. Use !end, to end the editor, and !help to get more help"));

		return CommandResult.success();
	}

	public CommandSpec getCommand() {
		return CommandSpec.builder().permission(LibPerm.EDITOR_COMMAND).description(Text.of("Open a text editor"))
				.extendedDescription(
						Text.of("While inside of the editor, normal chat and commands will no longer behave as normal. Use !help in the editor for help"))
				.executor(this).build();
	}

	public String[] getAliases() {
		return new String[] {"editor"};
	}
}
