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
package io.github.katrix.permissionblock

import java.nio.file.Path

import org.slf4j.Logger
import org.spongepowered.api.Sponge
import org.spongepowered.api.config.ConfigDir
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.game.state.{GameConstructionEvent, GameInitializationEvent}
import org.spongepowered.api.plugin.Plugin

import com.google.inject.Inject

import io.github.katrix.permissionblock.editor.commands.{TCmdCancel, TCmdChat, TCmdCommand, TCmdPositionAdd, TCmdPositionGet, TCmdPositionSet, TCmdPositionSubtract, TCmdDelete, TCmdEnd, TCmdHelp, TCmdText, TCmdView, TextCommandRegistry => TCmdReg}
import io.github.katrix.permissionblock.lib.LibPlugin
import io.github.katrix.permissionblock.listener.EditorListener
import io.github.katrix.permissionblock.persistant.ConfigSettings

object PermissionBlock {

	private var _plugin: PermissionBlock = null

	def plugin: PermissionBlock = _plugin

	def logger: Logger = plugin._log

	def configDir: Path = plugin._configDir

	def init(event: GameInitializationEvent) {
		ConfigSettings.initFile()
		Sponge.getEventManager.registerListeners(this, EditorListener)
		TCmdReg.register(TCmdText)

		TCmdReg.register(TCmdCancel)
		TCmdReg.register(TCmdPositionAdd)
		TCmdReg.register(TCmdPositionGet)
		TCmdReg.register(TCmdPositionSet)
		TCmdReg.register(TCmdPositionSubtract)
		TCmdReg.register(TCmdDelete)
		TCmdReg.register(TCmdEnd)
		TCmdReg.register(TCmdHelp)
		TCmdReg.register(TCmdView)
		TCmdReg.register(TCmdChat)
		TCmdReg.register(TCmdCommand)
	}
}

@Plugin(id = LibPlugin.ID, name = LibPlugin.NAME, version = LibPlugin.VERSION, description = LibPlugin.DESCRIPTION)
class PermissionBlock {
	@Inject
	private val _log      : Logger = null
	@Inject
	@ConfigDir(sharedRoot = false)
	private val _configDir: Path   = null

	@Listener
	def gameConstruct(event: GameConstructionEvent) {
		PermissionBlock._plugin = this
	}

	@Listener
	def init(event: GameInitializationEvent): Unit = {
		PermissionBlock.init(event)
	}
}