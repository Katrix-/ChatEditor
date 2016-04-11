/**
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
package io.github.katrix_.permissionblock;

import java.nio.file.Path;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameConstructionEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Plugin;

import com.google.inject.Inject;

import io.github.katrix_.permissionblock.editor.commands.TCmdCancel;
import io.github.katrix_.permissionblock.editor.commands.TCmdChat;
import io.github.katrix_.permissionblock.editor.commands.TCmdCommand;
import io.github.katrix_.permissionblock.editor.commands.TCmdDelete;
import io.github.katrix_.permissionblock.editor.commands.TCmdEnd;
import io.github.katrix_.permissionblock.editor.commands.TCmdHelp;
import io.github.katrix_.permissionblock.editor.commands.TCmdLocationAdd;
import io.github.katrix_.permissionblock.editor.commands.TCmdLocationPosition;
import io.github.katrix_.permissionblock.editor.commands.TCmdLocationSet;
import io.github.katrix_.permissionblock.editor.commands.TCmdLocationSubtract;
import io.github.katrix_.permissionblock.editor.commands.TCmdText;
import io.github.katrix_.permissionblock.editor.commands.TCmdView;
import io.github.katrix_.permissionblock.editor.commands.TextCommandRegistry;
import io.github.katrix_.permissionblock.io.ConfigSettings;
import io.github.katrix_.permissionblock.lib.LibPlugin;
import io.github.katrix_.permissionblock.listener.EditorListener;

@Plugin(id = LibPlugin.ID, name = LibPlugin.NAME, version = LibPlugin.VERSION, description = LibPlugin.DESCRIPTION)
public class PermissionBlock {

	private static PermissionBlock plugin;

	@Inject
	private Logger log;
	@Inject
	@ConfigDir(sharedRoot = false)
	private Path configDir;

	@Listener
	public void gameConstruct(GameConstructionEvent event) {
		plugin = this;
	}

	@Listener
	public void init(GameInitializationEvent event) {
		ConfigSettings.getConfig().initFile();
		Sponge.getEventManager().registerListeners(this, new EditorListener());

		TextCommandRegistry.register(new TCmdCancel());
		TextCommandRegistry.register(new TCmdLocationAdd());
		TextCommandRegistry.register(new TCmdLocationPosition());
		TextCommandRegistry.register(new TCmdLocationSet());
		TextCommandRegistry.register(new TCmdLocationSubtract());
		TextCommandRegistry.register(new TCmdDelete());
		TextCommandRegistry.register(new TCmdEnd());
		TextCommandRegistry.register(new TCmdHelp());
		TextCommandRegistry.register(TCmdText.INSTANCE);
		TextCommandRegistry.register(new TCmdView());
		TextCommandRegistry.register(new TCmdChat());
		TextCommandRegistry.register(new TCmdCommand());
	}

	public Logger getLog() {
		return log;
	}

	public static PermissionBlock getPlugin() {
		return plugin;
	}

	public Path getConfigDir() {
		return configDir;
	}
}
