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

/**
 * An editor that has a concept of a cursor.
 */
public interface IEditorCursor extends IEditor {

	/**
	 * Get the location of the cursor.
	 *
	 * @return The cursors position.
	 */
	int getCursor();

	/**
	 * Sets the location of the cursor within the editor.
	 *
	 * @param location The new position of the cursor.
	 * @return The new location after modification. This might be different from the value in the
	 * input. Same as calling {@link IEditorCursor#getCursor()}.
	 */
	int setCursor(int location);

	/**
	 * Moves the cursor forward.
	 *
	 * @param add The amount to move forward.
	 * @return The new location after modification. This might be different from the value in the
	 * input. Same as calling {@link IEditorCursor#getCursor()}.
	 */
	default int addCursor(int add) {
		setCursor(getCursor() + add);
		return getCursor();
	}

	/**
	 * Moves the cursor backwards.
	 *
	 * @param subtract The amount to move backwards.
	 * @return The new location after modification. This might be different from the value in the
	 * input. Same as calling {@link IEditorCursor#getCursor()}.
	 */
	default int subtractCursor(int subtract) {
		setCursor(getCursor() - subtract);
		return getCursor();
	}

	/**
	 * Delete a specific amount of characters from the location cursor.
	 *
	 * @param amount The amount of characters to delete.
	 */
	void deleteCharacters(int amount);
}
