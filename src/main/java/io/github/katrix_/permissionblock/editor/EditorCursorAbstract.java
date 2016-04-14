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
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public abstract class EditorCursorAbstract implements IEditorCursor {

	protected StringBuilder commandBuilder = new StringBuilder();
	protected int cursor;

	public EditorCursorAbstract(String string) {
		commandBuilder.append(string);
		cursor = commandBuilder.length();
	}

	@Override
	public void addString(String string) {
		commandBuilder.insert(cursor, string);
		cursor += string.length();
	}

	@Override
	public void deleteCharacters(int amount) {
		commandBuilder.delete(cursor, cursor + amount);
		cursor = validateCursorPos();
	}

	@Override
	public int getCursor() {
		return cursor;
	}

	@Override
	public int setCursor(int cursor) {
		this.cursor = cursor;
		this.cursor = validateCursorPos();
		return this.cursor;
	}

	@Override
	public int addCursor(int add) {
		cursor += add;
		cursor = validateCursorPos();
		return cursor;
	}

	@Override
	public int subtractCursor(int subtract) {
		cursor -= subtract;
		cursor = validateCursorPos();
		return cursor;
	}

	@Override
	public String getBuiltString() {
		return commandBuilder.toString();
	}

	@Override
	public void sendFormatted(Player player) {
		String firstPart = commandBuilder.substring(0, cursor);
		String secondPart = "";
		String selected = "";

		if(cursor < commandBuilder.length()) {
			selected = String.valueOf(commandBuilder.charAt(cursor));
			secondPart = commandBuilder.substring(cursor + 1, commandBuilder.length());
		}

		player.sendMessage(Text.of(firstPart, TextColors.BLUE, "[", selected, "]", TextColors.RESET, secondPart));
	}

	private int validateCursorPos() {
		int length = commandBuilder.length();
		if(cursor > length) {
			cursor = length;
		}
		else if(cursor < 0) {
			cursor = 0;
		}
		return cursor;
	}
}
