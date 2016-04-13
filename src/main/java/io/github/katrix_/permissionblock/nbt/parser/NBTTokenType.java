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

import java.util.regex.Pattern;

public enum NBTTokenType {
	COLON("[:]"),
	COMMA("[,]"),
	COMPOUNDSTART("[\\{]"),
	COMPOUNDEND("[\\}]"),
	LISTSTART("[\\[]"),
	LISTEND("[\\]]"),

	NBTBYTE("-?\\d+b"),
	NBTSHORT("-?\\d+s"),
	NBTLONG("-?\\d+L"),
	NBTFLOAT("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?f"),
	NBTDOUBLE("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?d"),
	NBTINT("-?\\d+"),
	NBTSTRING(Pattern.compile("\\\".+?\\\"")), //Doesn't support nested quotes. Problem?
	TAGNAME("^([^:]+)");

	private final Pattern pattern;

	NBTTokenType(String regex) {
		pattern = Pattern.compile("^(" + regex + ")");
	}

	NBTTokenType(Pattern pattern) {
		this.pattern = pattern;
	}

	public Pattern getPattern() {
		return pattern;
	}
}
