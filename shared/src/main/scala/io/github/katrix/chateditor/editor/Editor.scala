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

import scala.ref.WeakReference

import org.spongepowered.api.entity.living.player.Player

import io.github.katrix.chateditor.editor.component.{EndComponent, TextComponent}
import io.github.katrix.chateditor.listener.EditorHandler

case class Editor(text: TextComponent, end: EndComponent, player: WeakReference[Player], listener: EditorHandler) {

  /**
		* Replace the text component of this editor with a new one
		* and updates the listener. Only use this outside of
		* command executions.
		*/
  def useNewTextComponent(comp: TextComponent): Editor = {
    val newEditor = copy(text = comp)
    player.get match {
      case Some(online) =>
        listener.addEditorPlayer(online, newEditor)
        newEditor
      case None => newEditor
    }
  }

  /**
		* Replace the end component of this editor with a new one
		* and updates the listener. Only use this outside of
		* command executions.
		*/
  def useNewEndComponent(comp: EndComponent): Editor = {
    val newEditor = copy(end = comp)
    player.get match {
      case Some(online) =>
        listener.addEditorPlayer(online, newEditor)
        newEditor
      case None => newEditor
    }
  }
}
