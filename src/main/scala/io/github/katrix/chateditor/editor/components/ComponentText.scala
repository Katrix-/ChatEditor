/*
 * This file is part of ChatEditor, licensed under the MIT License (MIT).
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
package io.github.katrix.chateditor.editor.components

import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text

import io.github.katrix.chateditor.editor.Editor

/**
	* A component that holds what's written into the editor.
	*/
abstract class ComponentText(editor: Editor) extends Component(editor) {

	/**
		* Gives the built string without any extra formatting. Often used from the [[ComponentEnd]].
		*/
	def builtString: String

	/**
		* Gives a formatted [[Text]] back that will provide stuff as
		* highlighting and visualization for what's selected.
		* If the formatted message consists of multiple lines, each line is stored separate on the [[Seq]]
		*/
	def formatted: Seq[Text]

	/**
		* Sends the [[formatted]] [[Text]] to the specific player.
		* @param player The player to send to.
		*/
	def sendFormatted(player: Player): Unit

	/**
		* Adds string to this [[ComponentText]].
		* If a selection is made, it instead replaces the selected string.
		* @param string The string to add or replace
		*/
	def addString(string: String): Unit

	/**
		* Gets the position of the cursor.
		*/
	def pos: Int

	/**
		* Sets the position of the cursor
		*/
	def pos_=(newPos: Int): Unit

	/**
		* Moved the position forward.
		*/
	def pos_+=(amount: Int): Unit

	/**
		* Moves the position backwards.
		*/
	def pos_-=(amount: Int): Unit

	/**
		* Gets the selection end. If a selection is made this will be after the
		* [[pos]] else it will be at the [[pos]]
		*/
	def select: Int

	/**
		* Sets the selection position
		*/
	def select_=(selectPos: Int): Unit

	/**
		* Moved the selection forward.
		*/
	def select_+=(amount: Int): Unit

	/**
		* Moves the selection backwards.
		*/
	def select_-=(amount: Int): Unit

	/**
		* Gets the selected string
		*/
	def selectedString: String
}