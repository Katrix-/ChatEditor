package io.github.katrix.chateditor.persistant

import java.nio.file.Path

import io.github.katrix.chateditor.EditorPlugin
import io.github.katrix.katlib.helper.Implicits._
import io.github.katrix.katlib.helper.LogHelper
import io.github.katrix.katlib.persistant.{ConfigLoader, ConfigValue}

class EditorConfigLoader(dir: Path)(implicit plugin: EditorPlugin) extends ConfigLoader[EditorConfig](dir, identity) {

  override def loadData: EditorConfig = {
    val loaded = cfgRoot.getNode("version").getString("2") match {
      case "1" =>
        //TODO: Cleanup text values
        new EditorConfigV2(cfgRoot, default)
      case "2" => new EditorConfigV2(cfgRoot, default)
      case _ =>
        LogHelper.warn("Unknown config version. Using default")
        default
    }

    saveData(loaded)
    loaded
  }

  protected val default = new EditorConfig {
    override val version = ConfigValue("2", "Please don't touch this", Seq("version"))
  }
}
