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

import java.util.List;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

@SuppressWarnings("unused")
public interface IEditor {

	/**
	 * Add more text to this editor.
	 *
	 * @param string The text to be added.
	 */
	void addString(String string);

	/**
	 * Get the location of the cursor.
	 *
	 * @return The cursors position.
	 */
	int getLocation();

	/**
	 * Sets the location within the editor. What the location refers to depends on the
	 * implementation.
	 *
	 * @param location The new position of the cursor.
	 * @return The new location after modification. This might be different from the value in the
	 * input. Same as calling {@link IEditor#getLocation()}.
	 */
	int setLocation(int location);

	/**
	 * Moves the location forward. What the location refers to depends on the implementation.
	 *
	 * @param add The amount to move forward.
	 * @return The new location after modification. This might be different from the value in the
	 * input. Same as calling {@link IEditor#getLocation()}.
	 */
	int addLocation(int add);

	/**
	 * Moves the location backwards. What the location refers to depends on the implementation.
	 *
	 * @param subtract The amount to move backwards.
	 * @return The new location after modification. This might be different from the value in the
	 * input. Same as calling {@link IEditor#getLocation()}.
	 */
	int subtractLocation(int subtract);

	/**
	 * @return The complete string in the editor, just as the player typed it in.
	 */
	String getBuiltString();

	/**
	 * @return A modified list of {@link Text} used for displaying it to the player. Each new
	 * element in the list represents a new line.
	 */
	List<Text> getFormattedText();

	/**
	 * A action to do once the editor is "done".
	 *
	 * @param player The {@link Player} using this editor,
	 * @return If the ending was successful or not.
	 */
	boolean end(Player player);
}
