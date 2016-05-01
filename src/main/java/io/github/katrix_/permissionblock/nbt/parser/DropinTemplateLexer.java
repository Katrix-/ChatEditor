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

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("ALL")
public class DropinTemplateLexer {

	private static final String LINE_SEP = System.getProperty("line.separator");
	private static final char NL = '\n';

	/**
	 * lexer rules
	 *
	 * <pre>
	 * ESCAPED ::= '\\\\#|\\\\\\$'
	 * BACK_SLASH ::= '\\\\\\\\'
	 * IF ::= '#if'
	 * LEFT_PAREN ::= '\\('
	 * RIGHT_PAREN ::= '\\)'
	 * ELSE_IF ::= '#elseif'
	 * ELSE ::= '#else'
	 * END ::= '#end'
	 * FOREACH ::= '#foreach'
	 * REF ::= '\\$\\{[a-zA-Z_]\\w*(\\.[a-zA-Z_]\\w*)*\\}|\\$[a-zA-Z_]\\w*(\\.[a-zA-Z_]\\w*)*'
	 * IN ::= 'in'
	 * RANGE ::= '\\[[ \t]*\\-?\\d+[ \t]*\\.\\.[ \t]*\\-?\\d+[ \t]*\\]'
	 * OR ::= '\\|\\|'
	 * AND ::= '\\&\\&'
	 * NOT ::= '\\!'
	 * EQ ::= '\\=\\='
	 * NUM ::= '\\-?\\d+(\\.\\d+)?'
	 * STR ::= ''[^']*''
	 * WHITE_SPACE ::= '\\s+'
	 * PLAIN_TXT ::= '[^\\\\#\\$]+'
	 * </pre>
	 */
	private static final Pattern pattern = Pattern.compile(
			// ESCAPED 1
			"(\\G\\\\#|\\\\\\$)" +
					// BACK_SLASH 2
					"|(\\G\\\\\\\\)" +
					// IF 3
					"|(\\G#if)" +
					// LEFT_PAREN 4
					"|(\\G\\()" +
					// RIGHT_PAREN 5
					"|(\\G\\))" +
					// ELSE_IF 6
					"|(\\G#elseif)" +
					// ELSE 7
					"|(\\G#else)" +
					// END 8
					"|(\\G#end)" +
					// FOREACH 9
					"|(\\G#foreach)" +
					// REF 10
					"|(\\G\\$\\{[a-zA-Z_]\\w*(\\.[a-zA-Z_]\\w*)*\\}|\\$[a-zA-Z_]\\w*(\\.[a-zA-Z_]\\w*)*)" +
					// IN 13
					"|(\\Gin)" +
					// RANGE 14
					"|(\\G\\[[ \t]*\\-?\\d+[ \t]*\\.\\.[ \t]*\\-?\\d+[ \t]*\\])" +
					// OR 15
					"|(\\G\\|\\|)" +
					// AND 16
					"|(\\G\\&\\&)" +
					// NOT 17
					"|(\\G\\!)" +
					// EQ 18
					"|(\\G\\=\\=)" +
					// NUM 19
					"|(\\G\\-?\\d+(\\.\\d+)?)" +
					// STR 21
					"|(\\G'[^']*')" +
					// WHITE_SPACE 22
					"|(\\G\\s+)" +
					// PLAIN_TXT 23
					"|(\\G[^\\\\#\\$]+)");
	private String code = null;
	private Matcher matcher = null;
	private int currentPos = 0;
	private int currentRow = 1;
	private int currentCol = 1;
	private boolean eofReturned = false;

	private LinkedList<DtToken> lookAheadBuf = new LinkedList<DtToken>();

	public boolean hasMoreElements() {
		if (!this.lookAheadBuf.isEmpty() || currentPos < code.length())
			return true;
		if (!this.eofReturned)
			return true;
		return false;
	}

	public DtToken nextElement() {
		if (!this.lookAheadBuf.isEmpty()) {
			return this.lookAheadBuf.removeFirst();
		} else {
			return realNext();
		}
	}

	/**
	 * look ahead type
	 *
	 * @param i
	 *            lookahead count
	 * @return
	 */
	public DtTokenType LA(int i) {
		DtToken t = this.LT(i);
		return t != null ? t.getType() : null;
	}

	/**
	 * look ahead
	 *
	 * @param i
	 *            lookahead count
	 * @return
	 */
	public DtToken LT(int i) {
		if (i < this.lookAheadBuf.size())
			return this.lookAheadBuf.get(i - 1);
		int lng = i - this.lookAheadBuf.size();
		for (int j = 0; j < lng; j++) {
			this.lookAheadBuf.add(this.realNext());
		}
		return this.lookAheadBuf.get(i - 1);
	}

	public DtToken realNext() {
		if (currentPos < code.length()) {
			if (this.matcher.find(currentPos)) {
				for (DtTokenType t : DtTokenType.values()) {
					int gnum = t.getGroupNum();
					if (gnum != -1) {
						String txt = this.matcher.group(gnum);
						if (txt != null) {
							int nIndex = txt.lastIndexOf(NL);
							int lexemeLength = this.matcher.end() - this.matcher.start();
							DtToken ret = new DtToken(t, txt, this.currentRow, this.currentCol);
							this.currentPos += lexemeLength;
							if (nIndex != -1) {
								this.currentRow += countN(txt, nIndex);
								this.currentCol = lexemeLength - (nIndex + getNewLineSepLengthAt(nIndex, txt)) + 1;
							} else {
								this.currentCol += lexemeLength;
							}
							return ret;
						}
					}
				}
				throw new RuntimeException("No token matched at row: " + this.currentRow + ", col: " + this.currentCol + ", subsequent char: '"
						+ this.code.charAt(currentPos) + "'");
			} else {
				throw new RuntimeException("Unexpected char: '" + this.code.charAt(currentPos) + "' at row: " + this.currentRow + ", col: "
						+ this.currentCol);
			}
		} else if (!this.eofReturned) {
			this.eofReturned = true;
			return DtToken.EOF;
		} else {
			return null;
		}
	}

	private int getNewLineSepLengthAt(int nIndex, String txt) {
		if (LINE_SEP.length() == 1)
			return 1;
		int sepLen = LINE_SEP.length();
		int nlIndexInSep = LINE_SEP.indexOf(NL);
		int leftLen = sepLen - (nlIndexInSep + 1);
		if (leftLen != 0 && nIndex + leftLen - 1 < txt.length() && LINE_SEP.substring(nlIndexInSep).equals(txt.substring(nIndex, nIndex + leftLen))) {
			return leftLen + 1;
		} else {
			return 1;
		}
	}

	// count number of line separators
	private static int countN(String txt, int nIndex) {
		int ret = 0;
		for (int i = 0; i <= nIndex; i++) {
			if (NL == txt.charAt(i))
				ret++;
		}
		return ret;
	}

	public DropinTemplateLexer(String code) {
		this.code = code;
		this.matcher = pattern.matcher(code);
	}

	public static class DtToken {

		public static final DtToken EOF = new DtToken(DtTokenType.EOF, null, -1, -1);

		private DtTokenType type;
		private String lexeme;
		private int row;
		private int col;

		public DtToken(DtTokenType type, String lexeme, int row, int col) {
			this.type = type;
			this.lexeme = lexeme;
			this.row = row;
			this.col = col;
		}

		public DtTokenType getType() {
			return type;
		}
	}

	public static enum DtTokenType {
		ESCAPED(1), BACK_SLASH(2), IF(3), LEFT_PAREN(4), RIGHT_PAREN(5), ELSE_IF(6), ELSE(7), END(8), FOREACH(9), REF(10), IN(13), RANGE(14), OR(15), AND(
				16), NOT(17), EQ(18), NUM(19), STR(21), WHITE_SPACE(22), PLAIN_TXT(23), EOF(-1);

		private int groupNum;

		private DtTokenType(int groupNum) {
			this.groupNum = groupNum;
		}

		public int getGroupNum() {
			return groupNum;
		}

	}
}