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
package io.github.katrix.chateditor.command

import java.nio.file.Paths

import scala.ref.WeakReference
import scala.util.Try

import org.spongepowered.api.command.args.{CommandContext, GenericArguments}
import org.spongepowered.api.command.spec.CommandSpec
import org.spongepowered.api.command.{CommandException, CommandPermissionException, CommandResult, CommandSource}
import org.spongepowered.api.entity.living.player.Player

import io.github.katrix.chateditor.EditorPlugin
import io.github.katrix.chateditor.editor.Editor
import io.github.katrix.chateditor.editor.component.end.CompEndSave
import io.github.katrix.chateditor.editor.component.text.FileEditorHelper
import io.github.katrix.chateditor.lib.LibPerm
import io.github.katrix.chateditor.listener.EditorHandler
import io.github.katrix.katlib.command.CommandBase
import io.github.katrix.katlib.helper.Implicits._

class CmdEditorFile(handler: EditorHandler, parent: CmdEditor)(implicit plugin: EditorPlugin) extends CommandBase(Some(parent)) {

  final val Create   = t"Create"
  final val FilePath = t"File path"

  override def execute(src: CommandSource, args: CommandContext): CommandResult = {
    val data = (for {
      player <- src.asInstanceOfOpt[Player].toRight(nonPlayerError).right
      create <- args.getOne[Boolean](Create).toOption.toRight(new CommandException(plugin.config.text.commandErrorParseBoolean.value)).right
      path <- args
        .getOne[String](FilePath)
        .toOption
        .flatMap(s => Try(Paths.get(s)).toOption)
        .toRight(new CommandException(plugin.config.text.commandErrorInvalidPath.value))
        .right
    } yield {
      val exists = path.toFile.exists()

      if ((!exists && create) || exists) Right((player, path))
      else Left(new CommandException(plugin.config.text.commandEditorFilePathNotFoundNoCreate.value))
    }).right.flatMap(identity) //No flatten D:

    data match {
      case Right((player, path)) if player.hasPermission(LibPerm.UnsafeFile) =>
        player.sendMessage(plugin.config.text.commandEditorFileSuccess.value)
        val editor = Editor(FileEditorHelper.loadOrCreate(path), new CompEndSave, WeakReference(player), handler)
        editor.text.dataPut("path", path)
        handler.addEditorPlayer(player, editor)
        CommandResult.success()
      case Right((player, path)) =>
        throw new CommandPermissionException(plugin.config.text.commandEditorFilePermError.value)
      case Left(e) => throw e
    }
  }

  override def commandSpec: CommandSpec =
    CommandSpec
      .builder()
      .description(t"Opens a editor to a specific file path")
      .permission(LibPerm.UnsafeFile)
      .arguments(GenericArguments.bool(Create), GenericArguments.remainingJoinedStrings(FilePath))
      .executor(this)
      .build()

  override def aliases: Seq[String] = Seq("file")
}
