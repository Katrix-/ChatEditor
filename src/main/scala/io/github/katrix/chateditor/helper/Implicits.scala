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
package io.github.katrix.chateditor.helper

import java.util.Optional

import org.spongepowered.api.text.Text
import org.spongepowered.api.text.format.{TextColor, TextColors, TextStyle}

object Implicits {

	implicit class RichString(val string: String) extends AnyVal {

		def text: Text = Text.of(string)

		def richText: RichText = Text.of(string)

	}

	implicit def optionalToOption[A](optional: Optional[A]): Option[A] = {
		if(optional.isPresent) {
			Some(optional.get())
		}
		else {
			None
		}
	}

	implicit class RichText(val textOf: Text) extends AnyVal {

		def error(): Text = color(TextColors.RED).textOf

		def success(): Text = color(TextColors.GREEN).textOf

		def info(): Text = color(TextColors.YELLOW).textOf

		def color(textColor: TextColor): RichText = textOf.toBuilder.color(textColor).build()

		def style(textStyle: TextStyle): RichText = textOf.toBuilder.style(textStyle).build()

	}
}