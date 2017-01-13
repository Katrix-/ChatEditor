package io.github.katrix.chateditor

import io.github.katrix.chateditor.persistant.EditorConfig
import io.github.katrix.katlib.KatPlugin

trait EditorPlugin extends KatPlugin {

  override def config: EditorConfig

}
