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

import io.github.katrix_.permissionblock.helper.LogHelper;

public class NBTList extends NBTTag {

	private List<NBTTag> values = new ArrayList<>();
	private NBTType type;

	public NBTList(NBTType type) {
		this.type = type;
	}

	public NBTTag get(int i) {
		return values.get(i);
	}

	public void add(NBTTag value) {
		if(type == NBTType.UNKNOWN) {
			type = value.getType();
		}
		else if(type != value.getType()) {
			LogHelper.error("Tried to add wrong type to NBT list");
			return;
		}

		values.add(value);
	}

	public void set(NBTTag value, int i) {
		if(type == NBTType.UNKNOWN) {
			type = value.getType();
		}
		else if(type != value.getType()) {
			LogHelper.error("Tried to add wrong type to NBT list");
			return;
		}

		values.set(i, value);
	}

	public byte getByte(int i) {
		if(i < 0 || i > values.size() || type != NBTType.TAG_BYTE) return 0;

		return ((NBTByte)values.get(i)).getByte();
	}

	public short getShort(int i) {
		if(i < 0 || i > values.size() || type != NBTType.TAG_SHORT) return 0;

		return ((NBTShort)values.get(i)).getShort();
	}

	public int getInt(int i) {
		if(i < 0 || i > values.size() || type != NBTType.TAG_INT) return 0;

		return ((NBTInt)values.get(i)).getInt();
	}

	public long getLong(int i) {
		if(i < 0 || i > values.size() || type != NBTType.TAG_LONG) return 0;

		return ((NBTLong)values.get(i)).getLong();
	}

	public float getFloat(int i) {
		if(i < 0 || i > values.size() || type != NBTType.TAG_FLOAT) return 0;

		return ((NBTFloat)values.get(i)).getFloat();
	}

	public double getDouble(int i) {
		if(i < 0 || i > values.size() || type != NBTType.TAG_DOUBLE) return 0;

		return ((NBTDouble)values.get(i)).getDouble();
	}

	public byte[] getByteArray(int i) {
		if(i < 0 || i > values.size() || type != NBTType.TAG_BYTE_ARRAY) return new byte[0];

		return ((NBTByteArray)values.get(i)).get();
	}

	public int[] getIntArray(int i) {
		if(i < 0 || i > values.size() || type != NBTType.TAG_INT_ARRAY) return new int[0];

		return ((NBTIntArray)values.get(i)).get();
	}

	public String getString(int i) {
		if(i < 0 || i > values.size() || type != NBTType.TAG_STRING) return "";

		return ((NBTString)values.get(i)).get();
	}

	public NBTList getList(int i) {
		if(i < 0 || i > values.size() || type != NBTType.TAG_LIST) return new NBTList(NBTType.UNKNOWN);

		return ((NBTList)values.get(i));
	}

	public NBTCompound getCompound(int i) {
		if(i < 0 || i > values.size() || type != NBTType.TAG_COMPOUND) return new NBTCompound();

		return ((NBTCompound)values.get(i));
	}

	protected void setType(NBTType type) {
		this.type = type;
	}

	public void remove(int i) {
		values.remove(i);
	}

	public int size() {
		return values.size();
	}

	public NBTType getListType() {
		return type;
	}

	@Override
	public NBTType getType() {
		return NBTType.TAG_LIST;
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

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		NBTList nbtList = (NBTList)o;

		return values.equals(nbtList.values) && type == nbtList.type;
	}

	@Override
	public int hashCode() {
		int result = values.hashCode();
		result = 31 * result + type.hashCode();
		return result;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder("[");

		for (int i = 0; i < values.size(); ++i) {
			if (i != 0) {
				b.append(',');
			}
			b.append(i).append(':').append(values.get(i));
		}

		return b.append(']').toString();
	}
}
