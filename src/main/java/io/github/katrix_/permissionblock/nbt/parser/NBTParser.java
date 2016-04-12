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
package io.github.katrix_.permissionblock.nbt.parser;

import java.util.Iterator;

import org.apache.commons.lang3.math.NumberUtils;

import io.github.katrix_.permissionblock.nbt.NBTByte;
import io.github.katrix_.permissionblock.nbt.NBTCompound;
import io.github.katrix_.permissionblock.nbt.NBTDouble;
import io.github.katrix_.permissionblock.nbt.NBTFloat;
import io.github.katrix_.permissionblock.nbt.NBTInt;
import io.github.katrix_.permissionblock.nbt.NBTList;
import io.github.katrix_.permissionblock.nbt.NBTLong;
import io.github.katrix_.permissionblock.nbt.NBTParseException;
import io.github.katrix_.permissionblock.nbt.NBTShort;
import io.github.katrix_.permissionblock.nbt.NBTString;
import io.github.katrix_.permissionblock.nbt.NBTTag;

public class NBTParser {

	public NBTCompound parse(String string) throws NBTParseException {
		string = string.trim();
		NBTCompound tag = new NBTCompound();
		Iterator<NBTTokenizer.Token> tokens = NBTTokenizer.tokenize(string).iterator();

		if(!tokens.next().getType().equals(NBTTokenType.COMPOUND_START)) {
			throw new NBTParseException("NBT did not start with {");
		}

		return getCompound(tokens);
	}

	private NBTCompound getCompound(Iterator<NBTTokenizer.Token> tokens) throws NBTParseException {
		NBTCompound compound = new NBTCompound();
		NBTTokenizer.Token token = tokens.next();

		while(tokens.hasNext() && !token.getType().equals(NBTTokenType.COMPOUND_END)) {
			token = tokens.next();

			if(!token.getType().equals(NBTTokenType.TAG_NAME)) {
				throw new NBTParseException("Expected name, got " + token.getType());
			}

			String name = token.getValue();
			token = tokens.next();

			if(!token.getType().equals(NBTTokenType.COLON)) {
				throw new NBTParseException("Expected colon after name");
			}

			token = tokens.next();
			String value = token.getValue();
			String primitive = value.substring(0, value.length() - 1);

			switch(token.getType()) {
				case NBT_BYTE:
					compound.setByte(name, Byte.parseByte(primitive));
					break;
				case NBT_SHORT:
					compound.setShort(name, Short.parseShort(primitive));
					break;
				case NBT_LONG:
					compound.setLong(name, Long.parseLong(primitive));
					break;
				case NBT_FLOAT:
					compound.setFloat(name, Float.parseFloat(primitive));
					break;
				case NBT_DOUBLE:
					compound.setDouble(name, Double.parseDouble(primitive));
					break;
				case NBT_INT:
					compound.setInt(name, Integer.parseInt(value));
					break;
				case NBT_STRING:
					compound.setString(name, value);
					break;
				case LIST_START:
					compound.setTag(name, getList(tokens));
					break;
				case COMPOUND_START:
					compound.setTag(name, getCompound(tokens));
					break;
				default:
					throw new NBTParseException("Unexpected token " + token.getType());
			}
		}

		return compound;
	}

	private NBTList getList(Iterator<NBTTokenizer.Token> tokens) throws NBTParseException {
		NBTList<NBTTag> list = new NBTList<>(NBTTag.class); //FIXME
		NBTTokenizer.Token token = tokens.next();

		int i = 0;
		while(tokens.hasNext() && !token.getType().equals(NBTTokenType.LIST_END)) {
			token = tokens.next();

			String stringIndex = token.getValue();

			if(!token.getType().equals(NBTTokenType.TAG_NAME) || NumberUtils.isNumber(stringIndex)) {
				throw new NBTParseException("Expected index, got " + token.getType() + " " + stringIndex);
			}

			token = tokens.next();

			if(!token.getType().equals(NBTTokenType.COLON)) {
				throw new NBTParseException("Expected colon after index");
			}

			int index = Integer.parseInt(stringIndex); //Safe because of test earlier

			if(index != i) {
				throw new NBTParseException("Index did not follow sequential order");
			}

			token = tokens.next();
			String value = token.getValue();
			String primitive = value.substring(0, value.length() - 1);

			switch(token.getType()) {
				case NBT_BYTE:
					list.add(new NBTByte(Byte.parseByte(primitive)));
					break;
				case NBT_SHORT:
					list.add(new NBTShort(Short.parseShort(primitive)));
					break;
				case NBT_LONG:
					list.add(new NBTLong(Long.parseLong(primitive)));
					break;
				case NBT_FLOAT:
					list.add(new NBTFloat(Float.parseFloat(primitive)));
					break;
				case NBT_DOUBLE:
					list.add(new NBTDouble(Double.parseDouble(primitive)));
					break;
				case NBT_INT:
					list.add(new NBTInt(Integer.parseInt(value)));
					break;
				case NBT_STRING:
					list.add(new NBTString(value));
					break;
				case LIST_START:
					list.add(getList(tokens));
					break;
				case COMPOUND_START:
					list.add(getCompound(tokens));
					break;
				default:
					throw new NBTParseException("Unexpected token " + token.getType());
			}
		}

		return list;
	}
}
