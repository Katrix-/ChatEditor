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
package io.github.katrix.chateditor.persistant

import java.io.IOException
import java.nio.file.{Path, Paths}

import com.typesafe.config.ConfigRenderOptions

import io.github.katrix.chateditor.helper.LogHelper
import ninja.leaping.configurate.ConfigurationOptions
import ninja.leaping.configurate.commented.CommentedConfigurationNode
import ninja.leaping.configurate.hocon.HoconConfigurationLoader
import ninja.leaping.configurate.loader.ConfigurationLoader

abstract class ConfigurateBase(configDir: Path, name: String, data: Boolean) {

	protected val path = getPath
	protected val cfgLoader = initLoader()
	protected val cfgRoot   = loadRoot()

	def getPath: Path = {
		val ending = if(data) ".json" else ".conf"
		Paths.get(configDir.toString, "/" + name + ending)
	}

	def initFile(): Unit = {
		val parent = path.getParent.toFile
		if(!parent.exists && !parent.mkdirs) {
				LogHelper.error(s"Something went wrong when creating the directory for the file used by ${getClass.getName}")
		}
	}

	def initLoader(): ConfigurationLoader[CommentedConfigurationNode] = {
		val builder: HoconConfigurationLoader.Builder = HoconConfigurationLoader.builder.setPath(path)
		if(data) {
			builder.setRenderOptions(ConfigRenderOptions.concise)
		}

		builder.build()
	}

	protected def loadData() {
		saveData()
	}

	protected def saveData()

	protected def loadRoot(): CommentedConfigurationNode = {
		LogHelper.info(s"Loading configurate file ${getClass.getName}")
		val root = try {
			cfgLoader.load
		}
		catch {
			case e: IOException => cfgLoader.createEmptyNode(ConfigurationOptions.defaults)
		}

		root
	}

	protected def saveFile() {
		try {
			cfgLoader.save(cfgRoot)
		}
		catch {
			case e: IOException => e.printStackTrace()
		}
	}
}
