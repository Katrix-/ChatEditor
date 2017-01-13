package io.github.katrix.chateditor.persistant

import scala.util.Try

import io.github.katrix.chateditor.EditorPlugin
import io.github.katrix.katlib.KatPlugin
import io.github.katrix.katlib.helper.LogHelper
import io.github.katrix.katlib.persistant.CommentedConfigValue
import ninja.leaping.configurate.commented.CommentedConfigurationNode
import ninja.leaping.configurate.objectmapping.ObjectMappingException

class EditorConfigV2(cfgRoot: CommentedConfigurationNode, default: EditorConfig)(implicit plugin: EditorPlugin) extends EditorConfig {

  def configValue[A](existing: CommentedConfigValue[A])(implicit plugin: KatPlugin): CommentedConfigValue[A] =
    Try(Option(cfgRoot.getNode(existing.path: _*).getValue(existing.typeToken)).get)
      .map(found => existing.value_=(found)) //Doesn't want to work with CommentedConfigValue
      .recover {
        case _: ObjectMappingException =>
          LogHelper.error(s"Failed to deserialize value of ${existing.path.mkString(", ")}, using the default instead")
          existing
        case _: NoSuchElementException =>
          LogHelper.warn(s"No value found for ${existing.path.mkString(", ")}, using default instead")
          existing
      }
      .get

  override val version: CommentedConfigValue[String] = configValue(default.version)

}
