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

import javax.annotation.Nullable;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.google.common.collect.ImmutableList;

import io.github.katrix_.permissionblock.editor.IEditor;
import io.github.katrix_.permissionblock.editor.IEditorCursor;

public class TCmdDelete extends TextCommand {

	@Override
	public void execute(String raw, IEditor editor, Player player) {
		String[] parameters = getParameters(raw);
		if(parameters == null) return;

		try {
			IEditorCursor cursor = (IEditorCursor)editor;
			int amount = Integer.parseInt(parameters[0]);
			cursor.deleteCharacters(amount);
			player.sendMessage(Text.of(TextColors.GREEN, "Deleted " + amount + " characters"));
			editor.sendFormatted(player);
		}
		catch(NumberFormatException e) {
			player.sendMessage(Text.of(TextColors.RED, "Not a number"));
		}
	}

	@Nullable
	private String[] getParameters(String string) {
		String[] splitString = string.split(" ");

		if(splitString.length - 1 < 1) return null;

		int dstLength = splitString.length - 1;

		String[] parameters = new String[dstLength];
		System.arraycopy(splitString, 1, parameters, 0, dstLength);
		return parameters;
	}

	@Override
	public List<String> getAliases() {
		return ImmutableList.of("-", "delete", "subtract");
	}

	@Override
	public Text getHelp() {
		return null;
	}

	@Override
	public String getPermission() {
		return null;
	}

	@Override
	public Class<? extends IEditor> getCompatibility() {
		return IEditorCursor.class;
	}
}
