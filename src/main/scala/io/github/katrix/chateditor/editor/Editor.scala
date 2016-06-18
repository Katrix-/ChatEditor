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
package io.github.katrix.chateditor.editor

import scala.reflect.runtime.universe

import io.github.katrix.chateditor.editor.components.{Component, ComponentEnd, ComponentText}

class Editor(textFactory: Editor => ComponentText, endFactory: Editor => ComponentEnd) {
	val text = textFactory.apply(this)
	val end = endFactory.apply(this)

	def hasComponent(tag: universe.TypeTag[_ <: Component]): Boolean = {
		val tagType = tag.tpe
		tagType <:< getType(text) || tagType <:< getType(end)
	}

	def getComponent[A <: Component](tag: universe.TypeTag[A]): Option[A] = {
		val tagType = tag.tpe

		if(tagType <:< getType(text)) {
			Some(text.asInstanceOf[A])
		}
		else if(tagType <:< getType(end)) {
			Some(end.asInstanceOf[A])
		}

		None
	}

	def getComponentUnchecked[A <: Component](tag: universe.TypeTag[A]): A = {
		getComponent(tag).get
	}

	private def getType[T: universe.TypeTag](obj: T) = universe.typeOf[T]
}
