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

import java.io.StringWriter

import scala.util.{Failure, Success, Try}

import org.spongepowered.api.text.Text

import io.github.katrix.katlib.helper.Implicits._

object ECmdPrettifyHocon extends ECmdPrettify with HoconParser {

  override def prettify(string: String): Seq[String] = {
    val writer       = new StringWriter()
    val configLoader = loader(string, Some(writer))
    Try(configLoader.load()).map(n => configLoader.save(n)) match {
      case Success(root) => writer.toString.split('\n')
      case Failure(e)    => string.split('\n')
    }
  }

  override def aliases: Seq[String] = Seq("prettifyHocon")
  override def help:    Text        = t"Prettify your HOCON"
}
