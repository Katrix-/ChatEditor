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
package io.github.katrix.chateditor.editor.command

import scala.util.{Failure, Success, Try}

import org.spongepowered.api.text.Text

import com.google.gson.{GsonBuilder, JsonParser}

import io.github.katrix.katlib.helper.Implicits._

object ECmdPrettifyJson extends ECmdPrettify {

  private val parser = new JsonParser
  private val gson   = (new GsonBuilder).setPrettyPrinting().create()

  override def prettify(string: String): Seq[String] =
    Try(parser.parse(string)) match {
      case Success(tree) => gson.toJson(tree).split('\n')
      case Failure(e)    => string.split('\n')
    }
  override def aliases: Seq[String] = Seq("prettifyJson")
  override def help:    Text        = t"Prettify your JSON"
}
