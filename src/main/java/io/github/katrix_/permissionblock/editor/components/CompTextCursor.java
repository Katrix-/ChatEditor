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
package io.github.katrix_.permissionblock.editor.components;

import java.util.List;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.google.common.collect.ImmutableList;

import io.github.katrix_.permissionblock.editor.Editor;

public class CompTextCursor extends ComponentText {

	private StringBuilder commandBuilder = new StringBuilder();
	private int cursor = 0;

	public CompTextCursor(Editor editor, String string) {
		super(editor);
		commandBuilder.append(string);
		cursor = commandBuilder.length();
	}

	public CompTextCursor(Editor editor) {
		this(editor, "");
	}

	@Override
	public void addString(String string) {
		commandBuilder.insert(cursor, string);
		cursor += string.length();
	}

	public void deleteCharacters(int amount) {
		commandBuilder.delete(cursor, cursor + amount);
		cursor = validateCursorPos();
	}

	public int getCursor() {
		return cursor;
	}

	public int setCursor(int cursor) {
		this.cursor = cursor;
		this.cursor = validateCursorPos();
		return this.cursor;
	}

	@Override
	public String getBuiltString() {
		return commandBuilder.toString();
	}

	@Override
	public List<Text> getFormatted() {
		String firstPart = commandBuilder.substring(0, cursor);
		String secondPart = "";
		String selected = "";

		if(cursor < commandBuilder.length()) {
			selected = String.valueOf(commandBuilder.charAt(cursor));
			secondPart = commandBuilder.substring(cursor + 1, commandBuilder.length());
		}

		return ImmutableList.of((Text.of(firstPart, TextColors.BLUE, "[", selected, "]", TextColors.RESET, secondPart)));
	}

	@Override
	public void sendFormatted(Player player) {
		player.sendMessage(getFormatted().get(0));
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
