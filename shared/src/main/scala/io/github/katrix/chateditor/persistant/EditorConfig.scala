package io.github.katrix.chateditor.persistant

import io.github.katrix.katlib.persistant.{CommentedConfigValue, Config}

trait EditorConfig extends Config {

  val version: CommentedConfigValue[String]

  override def seq: Seq[CommentedConfigValue[_]] = Seq(version)
}
