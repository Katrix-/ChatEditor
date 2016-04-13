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

public abstract class NBTTag {

	public abstract NBTTag copy();

	public abstract NBTType getType();

	//Ensure that all NBT set this.
	@Override
	public abstract String toString();

	public String toBeautyString() {
		return toBeautyIndent(1);
	}

	protected String toBeautyIndent(int indention) {
		return toString();
	}

	protected StringBuilder beautify(StringBuilder b, int indention) {
		b.append('\n');

		for(int i = 0; i < indention; i++) {
			b.append('\t');
		}

		return b;
	}

	@Override
	public abstract boolean equals(Object that);

	public abstract static class NBTPrimitive extends NBTTag {

		public abstract long getLong();

		public abstract int getInt();

		public abstract short getShort();

		public abstract byte getByte();

		public abstract double getDouble();

		public abstract float getFloat();
	}
}
