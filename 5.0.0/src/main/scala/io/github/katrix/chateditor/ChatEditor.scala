package io.github.katrix.chateditor

import java.nio.file.Path

import org.slf4j.Logger
import org.spongepowered.api.Sponge
import org.spongepowered.api.config.ConfigDir
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.game.state.{GameConstructionEvent, GameInitializationEvent}
import org.spongepowered.api.plugin.{Dependency, Plugin, PluginContainer}

import com.google.inject.Inject

import io.github.katrix.chateditor.command.CmdEditor
import io.github.katrix.chateditor.editor.command.EditorCommandRegistry
import io.github.katrix.chateditor.lib.LibPlugin
import io.github.katrix.chateditor.listener.EditorHandler
import io.github.katrix.chateditor.persistant.{EditorConfig, EditorConfigLoader}
import io.github.katrix.katlib.lib.LibKatLibPlugin
import io.github.katrix.katlib.{ImplKatPlugin, KatLib}

object ChatEditor {

	final val Version         = s"${KatLib.CompiledAgainst}-0.1.0"
	final val ConstantVersion = "5.0.0-0.1.0"
	assert(Version == ConstantVersion)

	private var _plugin: ChatEditor = _

	implicit def plugin: ChatEditor = _plugin

	def init(event: GameInitializationEvent): Unit = {
		val registry = new EditorCommandRegistry
		registry.registerCore
		registry.registerFeatures

		val editorHandler = new EditorHandler(registry)
		Sponge.getEventManager.registerListeners(_plugin, editorHandler)

		val editorCmd = new CmdEditor(editorHandler)
		Sponge.getCommandManager.register(plugin, editorCmd.commandSpec, editorCmd.aliases: _*)
		Sponge.getCommandManager.register(plugin, plugin.pluginCmd.commandSpec, plugin.pluginCmd.aliases: _*)
	}
}

@Plugin(id = LibPlugin.Id, name = LibPlugin.Name, version = ChatEditor.ConstantVersion, dependencies = Array(new Dependency(id = LibKatLibPlugin.Id)))
class ChatEditor @Inject()(logger: Logger, @ConfigDir(sharedRoot = false) cfgDir: Path, spongeContainer: PluginContainer)
	extends ImplKatPlugin(logger, cfgDir, spongeContainer, LibPlugin.Id) with EditorPlugin {

	implicit val plugin = this

	private val configLoader = new EditorConfigLoader(cfgDir)
	override def config: EditorConfig = configLoader.loadData()

	@Listener
	def gameConstruct(event: GameConstructionEvent) {
		ChatEditor._plugin = this
	}

	@Listener
	def init(event: GameInitializationEvent): Unit = {
		ChatEditor.init(event)
	}
}