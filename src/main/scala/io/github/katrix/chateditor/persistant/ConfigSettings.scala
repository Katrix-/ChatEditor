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
package io.github.katrix.chateditor.persistant

import java.io.{File, IOException}
import java.nio.file.Paths
import java.util.function.Predicate

import com.google.common.reflect.TypeToken

import io.github.katrix.chateditor.ChatEditor$
import io.github.katrix.chateditor.helper.LogHelper
import io.github.katrix.chateditor.lib.LibPlugin
import ninja.leaping.configurate.{ConfigurationNode, ConfigurationOptions}
import ninja.leaping.configurate.commented.CommentedConfigurationNode
import ninja.leaping.configurate.hocon.HoconConfigurationLoader
import ninja.leaping.configurate.objectmapping.ObjectMappingException

object ConfigSettings extends ConfigurateBase(ChatEditor.configDir, "config", false) {
	loadData()
	saveFile()

	private var _debug = false

	def updateOldData(): Unit = {
		@throws[ObjectMappingException]
		def updateOldObject[T](node: ConfigurationNode, typeToken: TypeToken[T], predicate: T => Boolean, newValue: T) {
			val value = node.getValue(typeToken)
			if(predicate.apply(value)) {
				node.setValue(typeToken, newValue)
			}
		}

	}

	override def loadData(): Unit = {
		@throws[ObjectMappingException]
		def getOrDefaultObject[A](node: ConfigurationNode, typeToken: TypeToken[A], default: A): A = {
			if(!node.isVirtual) node.getValue(typeToken) else default
		}

		updateOldData()
		var node: CommentedConfigurationNode = null

		node = cfgRoot.getNode("misc", "debug")
		_debug = if(!node.isVirtual) node.getBoolean else _debug

		super.loadData()
	}

	override def saveData(): Unit = {
		@throws[ObjectMappingException]
		def saveNodeObject[T](node: CommentedConfigurationNode, typeToken: TypeToken[T], value: T, comment: String): Unit = {
			node.setComment(comment)
			node.setValue(typeToken, value)
		}

		cfgRoot.getNode("misc", "debug").setComment("Type = Boolean\nOutput debug stuff in console").setValue(_debug)
	}

	def debug: Boolean = _debug
}