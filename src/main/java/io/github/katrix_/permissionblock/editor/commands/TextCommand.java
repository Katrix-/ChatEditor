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

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import io.github.katrix_.permissionblock.editor.Editor;

public abstract class TextCommand {

	/**
	 * Executes the command.
	 *
	 * @param raw The raw string with all the parameters. Does not include "!"
	 * @param editor The editor the player is using.
	 * @param player The Player.
	 */
	public abstract void execute(String raw, Editor editor, Player player);

	/**
	 * @return A list of aliases for this command.
	 */
	public abstract List<String> getAliases();

	/**
	 * @return The help that will be provided with this command.
	 */
	public abstract Text getHelp();

	/**
	 * @return The permission a player needs to have to use this command.
	 */
	public abstract String getPermission();

	/**
	 * Remove this player from the editor list.
	 *
	 * @param player The player to remove.
	 */
	protected void removePlayerList(Player player) {
		//TODO
	}

	/**
	 * Gets the compatibility level for this command.
	 * The editor in {@link TextCommand#execute(String, IEditor, Player)} can be safely casted tp this.
	 * This is also used for hiding help entries.
	 */
	public Class<? extends IEditor> getCompatibility() {
		return IEditor.class;
	}
}
