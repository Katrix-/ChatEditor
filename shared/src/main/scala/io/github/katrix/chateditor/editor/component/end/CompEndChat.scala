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
package io.github.katrix.chateditor.editor.component.end

import java.util.Optional

import org.spongepowered.api.Sponge
import org.spongepowered.api.event.SpongeEventFactory
import org.spongepowered.api.event.cause.Cause
import org.spongepowered.api.event.message.MessageEvent.MessageFormatter
import org.spongepowered.api.text.transform.SimpleTextTemplateApplier

import io.github.katrix.chateditor.editor.Editor
import io.github.katrix.chateditor.editor.component.EndComponent
import io.github.katrix.chateditor.lib.LibPlugin
import io.github.katrix.chateditor.listener.BypassEditor
import io.github.katrix.katlib.helper.Implicits._

object CompEndChat extends EndComponent {

	override def end(editor: Editor): Option[Editor] = {
		editor.player.get match {
			case Some(player) =>
				val rawText = t"${editor.text.builtString}"
				val formatter = new MessageFormatter
				formatter.setBody(rawText)

				val headerTemplate = tt"<${"header"}>"
				val applier = new SimpleTextTemplateApplier(headerTemplate)
				applier.setParameter("header", t"${player.getName}")

				formatter.getHeader.add(applier)

				val cause = Cause.builder().owner(player).named(s"${LibPlugin.Id}.editor", editor).named("bypass", BypassEditor)
				val messageChannel = player.getMessageChannel
				val event = SpongeEventFactory.createMessageChannelEventChat(cause.build(), messageChannel, Optional.of(messageChannel), formatter, rawText,
					false)
				val cancelled = Sponge.getEventManager.post(event)

				println(cancelled)

				if(!cancelled) {
					event.getChannel.ifPresent(m => m.send(player, event.getMessage))
				}

				None
			case None => None
		}
	}
}
