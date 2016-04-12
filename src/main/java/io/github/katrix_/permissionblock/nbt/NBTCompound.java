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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings({"unused", "WeakerAccess"})
public class NBTCompound extends NBTTag {

	private Map<String, NBTTag> values = new LinkedHashMap<>(); //So that we retain the order

	public int size() {
		return values.size();
	}

	public void setTag(String key, NBTTag tag) {
		values.put(key, tag);
	}

	public void setByte(String key, byte value) {
		setTag(key, new NBTByte(value));
	}

	public void setShort(String key, short value) {
		setTag(key, new NBTShort(value));
	}

	public void setInt(String key, int value) {
		setTag(key, new NBTInt(value));
	}

	public void setLong(String key, long value) {
		setTag(key, new NBTLong(value));
	}

	public void setFloat(String key, float value) {
		setTag(key, new NBTFloat(value));
	}

	public void setDouble(String key, double value) {
		setTag(key, new NBTDouble(value));
	}

	public void setString(String key, String value) {
		setTag(key, new NBTString(value));
	}

	public void setByteArray(String key, byte[] value) {
		setTag(key, new NBTByteArray(value));
	}

	public void setIntArray(String key, int[] value) {
		setTag(key, new NBTIntArray(value));
	}

	public void setUUID(String key, UUID value) {
		setLong(key + "Most", value.getLeastSignificantBits());
		setLong(key + "Least", value.getLeastSignificantBits());
	}

	public void setBoolean(String key, boolean value) {
		setByte(key, (byte)(value ? 1 : 0));
	}

	public NBTTag getTag(String key) {
		return values.get(key);
	}

	public byte getByte(String key) {
		NBTTag tag = getTag(key);

		if(tag.getType().isPrimitive()) return ((NBTTag.NBTPrimitive)tag).getByte();

		return 0;
	}

	public short getShort(String key) {
		NBTTag tag = getTag(key);

		if(tag.getType().isPrimitive()) return ((NBTTag.NBTPrimitive)tag).getShort();

		return 0;
	}

	public int getInt(String key) {
		NBTTag tag = getTag(key);

		if(tag.getType().isPrimitive()) return ((NBTTag.NBTPrimitive)tag).getInt();

		return 0;
	}

	public long getLong(String key) {
		NBTTag tag = getTag(key);

		if(tag.getType().isPrimitive()) return ((NBTTag.NBTPrimitive)tag).getLong();

		return 0;
	}

	public float getFloat(String key) {
		NBTTag tag = getTag(key);

		if(tag.getType().isPrimitive()) return ((NBTTag.NBTPrimitive)tag).getFloat();

		return 0F;
	}

	public double getDouble(String key) {
		NBTTag tag = getTag(key);

		if(tag.getType().isPrimitive()) return ((NBTTag.NBTPrimitive)tag).getDouble();

		return 0D;
	}

	public String getString(String key) {
		NBTTag tag = getTag(key);

		if(tag instanceof NBTString) return ((NBTString)tag).get();

		return "";
	}

	public byte[] getByteArray(String key) {
		NBTTag tag = getTag(key);

		if(tag instanceof NBTByteArray) return ((NBTByteArray)tag).get();

		return new byte[0];
	}

	public int[] getIntArray(String key) {
		NBTTag tag = getTag(key);

		if(tag instanceof NBTIntArray) return ((NBTIntArray)tag).get();

		return new int[0];
	}

	public NBTCompound getCompound(String key) {
		NBTTag tag = getTag(key);

		if(tag instanceof NBTCompound) return (NBTCompound)tag;

		return new NBTCompound();
	}

	public NBTList getList(String key, NBTType type) {
		NBTTag tag = getTag(key);

		if(tag instanceof NBTList) {
			NBTList list = (NBTList)tag;
			if(type == list.getListType()) {
				return list;
			}
			else {
				return new NBTList(type);
			}
		}
		return new NBTList(type);
	}

	public boolean getBoolean(String key) {
		return getByte(key) != 0;
	}

	public UUID getUUID(String key) {
		return new UUID(getLong(key + "Most"), getLong(key + "Least"));
	}

	public void remove(String key) {
		values.remove(key);
	}

	public boolean hasKey(String key) {
		return values.containsKey(key);
	}

	public void merge(NBTCompound other) {
		for(Map.Entry<String, NBTTag> entry : other.values.entrySet()) {
			NBTTag tag = entry.getValue();
			String key = entry.getKey();
			if(tag instanceof NBTCompound) {
				if(hasKey(key) && getTag(key) instanceof NBTCompound) {
					getCompound(key).merge((NBTCompound)tag);
				}
				else {
					setTag(key, tag.copy());
				}
			}
			else {
				setTag(key, tag.copy());
			}
		}
	}

	@Override
	public NBTTag copy() {
		NBTCompound tag = new NBTCompound();

		for(Map.Entry<String, NBTTag> entry : values.entrySet()) {
			tag.setTag(entry.getKey(), entry.getValue().copy());
		}

		return tag;
	}

	@Override
	public NBTType getType() {
		return NBTType.TAG_COMPOUND;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		NBTCompound that = (NBTCompound)o;

		return values.equals(that.values);

	}

	@Override
	public int hashCode() {
		return values.hashCode();
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder("{");

		for (Map.Entry<String, NBTTag> entry : values.entrySet()) {
			if (b.length() != 1)
			{
				b.append(',');
			}
			b.append(entry.getKey()).append(':').append(entry.getValue());
		}

		return b.append('}').toString();
	}
}
