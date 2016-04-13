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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import io.github.katrix_.permissionblock.nbt.NBTType;

public class NBTParser {

	public static void main(String[] args) {

		NBTParser parser = new NBTParser();
		String nbt = "{DropChances:[0:0.085f,1:0.085f,2:0.085f,3:0.085f,4:0.085f],Age:0,UUIDLeast:-6587974176882625461L,"
				+ "Attributes:[0:{Name:\"generic.maxHealth\",Base:4.0d},1:{Name:\"generic.knockbackResistance\",Base:0.0d},2:{Name:\"generic"
				+ ".movementSpeed\",Base:0.25d},3:{Name:\"generic.followRange\",Base:16.0d,Modifiers:[0:{Name:\"Random spawn bonus\","
				+ "UUIDLeast:-7151323691541476538L,Operation:1,Amount:-0.019585756731058196d,UUIDMost:941504467614123631L}]}],IsChickenJockey:0b,"
				+ "Motion:[0:-3.719184344028112E-4d,1:-0.0784000015258789d,2:-0.0733363376799967d],test:2,Health:4s,HealF:4.0f,Bukkit.updateLevel:2,"
				+ "Fire:-1s,Invulnerable:0b,DeathTime:0s,ForcedAge:0,Equipment:[0:{},1:{},2:{},3:{},4:{}],AbsorptionAmount:0.0f,InLove:0,"
				+ "OnGround:1b,HurtTime:0s,AgeLocked:0b,UUIDMost:-8914269988870142553L,HurtByTimestamp:0,Dimension:0,"
				+ "WorldUUIDLeast:-8214179146566002376L,Air:300s,Pos:[0:328.5118353835865d,1:43.0d,2:-7152.507890682375d],CanPickUpLoot:0b,"
				+ "EggLayTime:5219,PortalCooldown:0,PersistenceRequired:1b,Leashed:0b,WorldUUIDMost:-6365660778832771081L,FallDistance:0.0f,"
				+ "Rotation:[0:179.36337f,1:0.0f],Spigot.ticksLived:1087}";

		try {
			NBTCompound tag = parser.parse(nbt);
			System.out.println(tag);
		}
		catch(NBTParseException e) {
			e.printStackTrace();
		}
	}

	public NBTCompound parse(String string) throws NBTParseException {
		string = string.trim();
		Iterator<Token> tokens = tokenize(string).iterator();

		testHasNest(tokens);
		Token token = tokens.next();
		if(token.getType() != NBTTokenType.COMPOUND_START) throw new NBTParseException("NBT did not start with {", token);

		return getCompound(tokens);
	}

	private NBTCompound getCompound(Iterator<Token> tokens) throws NBTParseException {
		NBTCompound compound = new NBTCompound();
		testHasNest(tokens);
		Token token = tokens.next();

		if(token.getType() == NBTTokenType.COMPOUND_END) return compound;

		while(tokens.hasNext()) {

			if(token.getType() != NBTTokenType.TAG_NAME) throw new NBTParseException("Expected name, got " + token.getType(), token);

			String name = token.getValue();

			testHasNest(tokens);
			token = tokens.next();
			if(token.getType() != NBTTokenType.COLON) throw new NBTParseException("Expected colon after name", token);

			testHasNest(tokens);
			token = tokens.next();
			String value = token.getValue();
			String primitive = value.substring(0, value.length() - 1);
			System.out.println(token);

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
					compound.setString(name, value.substring(1, value.length() - 1));
					break;
				case LIST_START:
					compound.setTag(name, getList(tokens));
					break;
				case COMPOUND_START:
					compound.setTag(name, getCompound(tokens));
					break;
				default:
					throw new NBTParseException("Unexpected token " + token.getType(), token);
			}

			testHasNest(tokens);
			token = tokens.next();
			System.out.println(token);

			if(token.getType() != NBTTokenType.COMMA) {
				if(token.getType() == NBTTokenType.COMPOUND_END) {
					break;
				}
				else {
					throw new NBTParseException("Didn't encounter a '}' at the end of the compound", token);
				}
			}

			testHasNest(tokens);
			token = tokens.next();
		}

		return compound;
	}

	private NBTList getList(Iterator<Token> tokens) throws NBTParseException {
		NBTList list = new NBTList(NBTType.UNKNOWN);
		testHasNest(tokens);
		Token token = tokens.next();

		int i = 0;
		while(tokens.hasNext()) {

			String stringIndex = token.getValue();
			if(token.getType() != NBTTokenType.NBT_INT) throw new NBTParseException("Expected index, got " + token.getType() + " " + stringIndex, token);

			testHasNest(tokens);
			token = tokens.next();
			if(token.getType() != NBTTokenType.COLON) throw new NBTParseException("Expected colon after index", token);

			int index = Integer.parseInt(stringIndex); //Safe because of test earlier
			if(index != i) throw new NBTParseException("Index did not follow sequential order", token);

			testHasNest(tokens);
			token = tokens.next();
			String value = token.getValue();
			String primitive = value.substring(0, value.length() - 1);

			if(list.getListType() == NBTType.UNKNOWN) {
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
						throw new NBTParseException("Unexpected token " + token.getType(), token);
				}
			}
			else {
				switch(list.getListType()) {
					case TAG_BYTE:
						list.add(new NBTByte(Byte.parseByte(primitive)));
						break;
					case TAG_SHORT:
						list.add(new NBTShort(Short.parseShort(primitive)));
						break;
					case TAG_LONG:
						list.add(new NBTLong(Long.parseLong(primitive)));
						break;
					case TAG_FLOAT:
						list.add(new NBTFloat(Float.parseFloat(primitive)));
						break;
					case TAG_DOUBLE:
						list.add(new NBTDouble(Double.parseDouble(primitive)));
						break;
					case TAG_INT:
						list.add(new NBTInt(Integer.parseInt(value)));
						break;
					case TAG_STRING:
						list.add(new NBTString(value));
						break;
					case TAG_LIST:
						list.add(getList(tokens));
						break;
					case TAG_COMPOUND:
						list.add(getCompound(tokens));
						break;
					default:
						throw new NBTParseException("Unexpected type " + token.getType(), token);
				}
			}

			testHasNest(tokens);
			token = tokens.next();
			System.out.println(token);

			if(token.getType() != NBTTokenType.COMMA) {
				if(token.getType() == NBTTokenType.LIST_END) {
					break;
				}
				else {
					throw new NBTParseException("Didn't encounter a ']' at the end of the list", token);
				}
			}

			testHasNest(tokens);
			token = tokens.next();
			i++;
		}

		return list;
	}

	private void testHasNest(Iterator<Token> tokens) throws NBTParseException {
		if(!tokens.hasNext()) throw new NBTParseException("Unexpected end of tokens");
	}

	private static class Lexer {

		private int pos = 0;
		private final Matcher matcher;
		private final String input;

		private Lexer(String input) {
			this.input = input;

			StringBuilder patternBuilder = new StringBuilder();
			for(NBTTokenType token : NBTTokenType.values()) {
				patternBuilder.append(String.format("|(?<%s>%s)", token.name(), token.getPattern()));
			}
			matcher = Pattern.compile(patternBuilder.toString().substring(1)).matcher(input);
		}

		private List<Token2> lex() throws NBTParseException {
			List<Token2> list = new ArrayList<>();

			while(hasNext()) {
				list.add(next());
			}

			return list;
		}

		private boolean hasNext() {
			return pos < input.length();
		}

		private Token2 next() throws NBTParseException {
			int line = getLine();
			int col = getCol(line);
			if(matcher.find(pos)) {
				for(NBTTokenType tokenType : NBTTokenType.values()) {
					String result = matcher.group(tokenType.name());
					if(result != null) {
						int length = matcher.end() - matcher.start();
						Token2 token = new Token2(tokenType, result, col, line);
						pos += length;
						return token;
					}
				}
				throw new NBTParseException("No patters matched", col, line);
			}
			else throw new NBTParseException("Unrecognized character", col, line);
		}

		private int getLine() {
			String current = input.substring(0, pos);
			int lines = 1;
			Matcher m = Pattern.compile("\r\n|\r|\n").matcher(current);

			while (m.find()) {
				lines ++;
			}

			return lines;
		}

		private int getCol() {
			String current = input.substring(0, pos);
			int col = pos;
			Matcher matcher = Pattern.compile("\r\n|\r|\n").matcher(current);
			List<String> tmpList = new ArrayList<>();

			while(matcher.find()) {
				tmpList.add(matcher.group());
			}

			//Need to watch out for newline characters?
			Iterator<String> stringIt = tmpList.iterator();
			while(stringIt.hasNext()) {
				String line = stringIt.next();

				if(stringIt.hasNext()) {
					col -= line.length();
				}
			}

			return col;
		}
	}

	public static class Token2 {

		private final NBTTokenType type;
		private final String value;
		private final int col;
		private final int line;

		private Token2(NBTTokenType type, String value, int col, int line) {
			this.type = type;
			this.value = value;
			this.col = col;
			this.line = line;
		}

		public NBTTokenType getType() {
			return type;
		}

		public String getValue() {
			return value;
		}

		public int getCol() {
			return col;
		}

		public int getLine() {
			return line;
		}

		@Override
		public String toString() {
			return "Token2{" +
					"type=" + type +
					", value='" + value + '\'' +
					", col=" + col +
					", line=" + line +
					'}';
		}
	}

	private static List<Token> tokenize(String input) {
		List<Token> list = new ArrayList<>();

		while(!input.equals("")) {

			boolean match = false;

			while(!match) {
				for(NBTTokenType tokenType : NBTTokenType.values()) {
					Matcher matcher = tokenType.getPattern().matcher(input);
					if(matcher.find()) {
						match = true;

						input = matcher.replaceFirst("");

						System.out.println(input);
						Token token = new Token(tokenType, matcher.group());
						System.out.println(token);

						list.add(token);
						break; //TODO: Do I need this break here?
					}
				}
			}
		}

		return list;
	}

	public static class Token {

		private final NBTTokenType type;
		private final String value;

		private Token(NBTTokenType type, String value) {
			this.type = type;
			this.value = value;
		}

		public NBTTokenType getType() {
			return type;
		}

		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return "Token{" + "type=" + type + ", value='" + value + '\'' + '}';
		}
	}
}
