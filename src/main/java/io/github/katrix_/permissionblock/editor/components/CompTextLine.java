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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class CompTextLine implements IComponentText {

	private List<String> stringList;
	private int line;

	public CompTextLine(List<String> stringList) {
		this.stringList = stringList;
		line = stringList.size() - 1;
	}

	public CompTextLine(String string) {
		stringList = new ArrayList<>();
		stringList.add(string);
		line = 0;
	}

	public void addString(String string) {
		stringList.set(line, string);
	}

	public boolean addLine() {
		stringList.add(line, "");
		return true;
	}

	public boolean removeLine() {
		if(stringList.size() <= 1) return false;

		stringList.remove(line);
		return true;
	}

	public int getLine() {
		return line;
	}

	public int setLinePos(int location) {
		line = location;
		line = validateLinePos();
		return line;
	}

	public String getCurrentLineContent() {
		return stringList.get(line);
	}

	public String getBuiltString() {
		return String.join("", stringList);
	}

	@Override
	public List<Text> getFormatted() {
		List<Text> list = stringList.stream().map(Text::of).collect(Collectors.toList());

		Text selected = list.get(line);
		selected = selected.toBuilder().color(TextColors.BLUE).build();
		list.set(line, selected);

		return list;
	}

	@Override
	public void sendFormatted(Player player) {
		List<Text> text = getFormatted();

		PaginationList.Builder builder = Sponge.getServiceManager().provideUnchecked(PaginationService.class).builder();
		builder.title(Text.of(TextColors.GRAY, "Line Editor"));
		builder.contents(text);
		builder.sendTo(player);
	}

	private int validateLinePos() {
		int size = stringList.size();
		if(line > size) {
			line = size;
		}
		else if(line < 0) {
			line = 0;
		}
		return line;
	}
}
