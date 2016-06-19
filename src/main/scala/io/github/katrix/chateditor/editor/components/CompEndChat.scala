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

import java.util.Optional

import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.SpongeEventFactory
import org.spongepowered.api.event.cause.Cause
import org.spongepowered.api.event.message.MessageEvent.MessageFormatter
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.transform.SimpleTextTemplateApplier

import io.github.katrix.chateditor.editor.Editor
import io.github.katrix.chateditor.lib.LibPlugin

class CompEndChat(editor: Editor, player: Player) extends ComponentEnd(editor) {

	def end(): Boolean = {
		val rawText = Text.of(editor.text.builtString)
		val formatter = new MessageFormatter
		formatter.setBody(rawText)
		formatter.getHeader.add(new SimpleTextTemplateApplier(???))

		val cause = Cause.builder().owner(player).suggestNamed(s"${LibPlugin.ID}.editor", editor)
		val messageChannel = player.getMessageChannel
		SpongeEventFactory.createMessageChannelEventChat(cause.build(), messageChannel, Optional.of(messageChannel), formatter, rawText, false)
		true
	}
}