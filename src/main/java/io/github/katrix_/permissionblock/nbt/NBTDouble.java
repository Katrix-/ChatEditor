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

public class NBTDouble extends NBTTag.NBTPrimitive {

	private double value;

	public NBTDouble(double value) {
		this.value = value;
	}

	public NBTDouble() {}

	public void set(double value) {
		this.value = value;
	}

	@Override
	public NBTTag copy() {
		return new NBTDouble(value);
	}

	@Override
	public long getLong() {
		return (long)Math.floor(value);
	}

	@Override
	public int getInt() {
		return (int)Math.floor(value);
	}

	//Following what Minecraft does, so we get the same result.
	@Override
	public short getShort() {
		return (short)((int)Math.floor(value) & 65535);
	}

	//Following what Minecraft does, so we get the same result.
	@Override
	public byte getByte() {
		return (byte)((int)Math.floor(value) & 255);
	}

	@Override
	public double getDouble() {
		return value;
	}

	@Override
	public float getFloat() {
		return (float)value;
	}

	@Override
	public NBTType getType() {
		return NBTType.TAG_DOUBLE;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		NBTDouble nbtDouble = (NBTDouble)o;

		return Double.compare(nbtDouble.value, value) == 0;
	}

	@Override
	public int hashCode() {
		long temp = Double.doubleToLongBits(value);
		return (int)(temp ^ (temp >>> 32));
	}

	@Override
	public String toString() {
		return value + "d";
	}
}
