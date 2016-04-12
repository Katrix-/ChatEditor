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

import io.github.katrix_.permissionblock.nbt.NBTCompound;
import io.github.katrix_.permissionblock.nbt.NBTParseException;
import io.github.katrix_.permissionblock.nbt.NBTTag;

public class NBTParser {

	public NBTCompound parse(String string) throws NBTParseException {
		string = string.trim();
		NBTCompound tag = new NBTCompound();
		Iterator<NBTTokenizer.Token> tokens = NBTTokenizer.tokenize(string).iterator();

		NBTTokenizer.Token token = tokens.next();
		if(!token.getType().equals(NBTTokenType.COMPOUND_START)) {
			throw new NBTParseException("NBT did not start with {");
		}

		while(tokens.hasNext()) {
			token = tokens.next();

			if(!token.getType().equals(NBTTokenType.TAG_NAME)) {
				throw new NBTParseException("Expected name, got " + token.getType());
			}

			if(!token.getType().equals(NBTTokenType.COLON)) {
				throw new NBTParseException("Expected colon after name");
			}
		}


		return null;
	}

	private NBTTag getObject(NBTTokenizer.Token current, Iterable<NBTTokenizer.Token> next) {

	}
}
