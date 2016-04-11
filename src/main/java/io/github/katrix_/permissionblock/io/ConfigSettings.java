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
package io.github.katrix_.permissionblock.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import io.github.katrix_.permissionblock.PermissionBlock;
import io.github.katrix_.permissionblock.helper.LogHelper;
import io.github.katrix_.permissionblock.lib.LibPlugin;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

public class ConfigSettings {

	private static final ConfigSettings CFG = new ConfigSettings();

	private Path cfgFile = Paths.get(PermissionBlock.getPlugin().getConfigDir() + "/" + LibPlugin.ID + ".conf");
	private ConfigurationLoader<CommentedConfigurationNode> cfgLoader = HoconConfigurationLoader.builder().setPath(cfgFile).build();
	private CommentedConfigurationNode cfgRoot;

	private boolean debug = false;

	public void loadSettings() {
		CommentedConfigurationNode node;

		node = cfgRoot.getNode("misc", "debug");
		debug = !node.isVirtual() ? node.getBoolean() : debug;

		saveSettings();
	}

	public void saveSettings() {
		cfgRoot.getNode("misc", "debug").setComment("Type = Boolean\nOutput debug stuff in console").setValue(debug);
	}

	public void loadFile() {
		LogHelper.info("Loading config");
		try {
			cfgRoot = cfgLoader.load();
		}
		catch(IOException e) {
			cfgRoot = cfgLoader.createEmptyNode(ConfigurationOptions.defaults());
		}

		loadSettings();
	}

	public void saveFile() {
		try {
			cfgLoader.save(cfgRoot);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void initFile() {
		if(cfgRoot == null) {
			loadFile();
		}

		File parent = cfgFile.getParent().toFile();
		if(!parent.exists()) {
			if(!parent.mkdirs()) {
				LogHelper.error("Something went wring when creating the parent directory for the config");
				return;
			}
		}

		saveFile();
	}

	public static boolean getDebug() {
		return CFG.debug;
	}

	public static ConfigSettings getConfig() {
		return CFG;
	}
}
