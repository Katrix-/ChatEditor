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
package io.github.katrix_.permissionblock.editor;

import java.util.List;

import com.google.common.collect.ImmutableList;

import io.github.katrix_.permissionblock.editor.components.IComponentEnd;
import io.github.katrix_.permissionblock.editor.components.IComponentMisc;
import io.github.katrix_.permissionblock.editor.components.IComponentText;

public class Editor {

	private final IComponentText text;
	private final IComponentEnd end;
	private final List<IComponentMisc> misc;

	public Editor(IComponentText text, IComponentEnd end, IComponentMisc... misc) {
		this.text = text;
		this.end = end;
		this.misc = ImmutableList.copyOf(misc);
	}

	public IComponentText getTextComponent() {
		return text;
	}

	public IComponentEnd getEndComponent() {
		return end;
	}

	public boolean end() {
		return end.end(text.getBuiltString());
	}
}
