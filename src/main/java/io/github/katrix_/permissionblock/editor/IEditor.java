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
package io.github.katrix_.permissionblock.editor;

import org.spongepowered.api.entity.living.player.Player;

/**
 * Something that can hold a string while a player works on it and changes it.
 */
public interface IEditor {

	/**
	 * Add more text to this editor.
	 *
	 * @param string The text to be added.
	 */
	void addString(String string);

	/**
	 * @return The complete string in the editor, just as the player typed it in.
	 */
	String getBuiltString();

	/**
	 * Send a the formatted content of the editor to a player.
	 *
	 * @param player The player to send to.
	 */
	void sendFormatted(Player player);

	/**
	 * A action to do once the editor is "done".
	 *
	 * @param player The {@link Player} using this editor,
	 * @return If the ending was successful or not.
	 */
	boolean end(Player player);
}
