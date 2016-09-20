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
package io.github.katrix.chateditor.editor.component

import org.spongepowered.api.entity.living.player.Player

import io.github.katrix.chateditor.editor.Editor

abstract class TextComponent {

	/**
		* The preview type that the text will be printed as
		*/
	type Preview
	type Self <: TextComponent

	/**
		* Builds the string this [[TextComponent]] represents
		*/
	def builtString: String

	/**
		* Gets the currently selected string
		*/
	def selectedString: String

	/**
		* Gets a preview of this [[TextComponent]]. This might
		* return a result including stuff like colors to make reading easier
		* @param editor The current editor
		*/
	def preview(editor: Editor): Preview

	/**
		* Gets a preview of the selected area
		* @param editor The current editor
		*/
	def selectedPreview(editor: Editor): Preview

	/**
		* Sends a preview to a player
		* @param editor The current editor
		* @param player The player to send to
		*/
	def sendPreview(editor: Editor, player: Player): Unit

	/**
		* Adds a new string to this [[TextComponent]] or
		* replaces if a selection is made
		* @param string The string to add
		* @return The new [[TextComponent]] to use.
		*/
	def addString(string: String): Self

	/**
		* Check if there is currently something selected
		*/
	def hasSelection: Boolean = pos != select

	def pos: Int
	def pos_=(pos: Int): Self

	def select: Int
	def select_=(pos: Int): Self

	/**
		* Get data in this [[TextComponent]]
		*/
	def data(key: String): Option[Any]

	/**
		* Store data in this [[TextComponent]]
		*/
	def dataPut(key: String, value: Any): Self

	/**
		* Remove data from this [[TextComponent]]
		*/
	def dataRemove(key: String): Self

	/**
		* Remove data from this [[TextComponent]]
		*/
	def dataRemove(value: Any): Self
}
