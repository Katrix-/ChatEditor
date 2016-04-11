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
package io.github.katrix_.permissionblock.nbt;

import java.util.ArrayList;
import java.util.List;

public class NBTList<T extends NBTTag> extends NBTTag {

	private List<T> values = new ArrayList<>();
	private final Class<T> type;

	public NBTList(Class<T> type) {
		this.type = type;
	}

	public T get(int i) {

		return values.get(i);
	}

	public void add(T value) {
		values.add(value);
	}

	public void set(T value, int i) {
		values.set(i, value);
	}

	public void remove(int i) {
		values.remove(i);
	}

	public int size() {
		return values.size();
	}

	public Class<T> getType() {
		return type;
	}

	@Override
	public NBTTag copy() {
		NBTList list = new NBTList(type);

		for(NBTTag tag : values) {
			NBTTag newTag = tag.copy();
			list.add(newTag);
		}
		return list;
	}
}
