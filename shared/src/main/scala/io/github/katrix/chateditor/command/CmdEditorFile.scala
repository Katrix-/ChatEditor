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

import scala.concurrent.ExecutionContext.Implicits.global
import scala.ref.WeakReference
import scala.util.{Failure, Success, Try}

import org.spongepowered.api.command.args.{CommandContext, GenericArguments}
import org.spongepowered.api.command.spec.CommandSpec
import org.spongepowered.api.command.{CommandException, CommandPermissionException, CommandResult, CommandSource}
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.format.TextColors._

import io.github.katrix.chateditor.EditorPlugin
import io.github.katrix.chateditor.editor.Editor
import io.github.katrix.chateditor.editor.component.end.CompEndSave
import io.github.katrix.chateditor.editor.component.text.FileEditorHelper
import io.github.katrix.chateditor.lib.LibPerm
import io.github.katrix.chateditor.listener.EditorHandler
import io.github.katrix.katlib.command.CommandBase
import io.github.katrix.katlib.helper.Implicits._
import io.github.katrix.katlib.helper.LogHelper
import shapeless.Typeable

class CmdEditorFile(handler: EditorHandler, parent: CmdEditor)(implicit plugin: EditorPlugin) extends CommandBase(Some(parent)) {

  final val Create   = t"Create"
  final val FilePath = t"File path"

  private val playerTypeable = Typeable[Player]

  override def execute(src: CommandSource, args: CommandContext): CommandResult = {
    val data = (for {
      player <- playerTypeable.cast(src).toRight(nonPlayerError).right
      create <- args.getOne[Boolean](Create).toOption.toRight(new CommandException(t"Failed to parse boolean")).right
      path <- args
        .getOne[String](FilePath)
        .toOption
        .flatMap(s => Try(Paths.get(s)).toOption)
        .toRight(new CommandException(t"Failed to parse path"))
        .right
    } yield {
      val exists = path.toFile.exists()

      if ((!exists && create) || exists) Right((player, path))
      else Left(new CommandException(t"No file with that name found, and file creation is off"))
    }).right.flatMap(identity) //No flatten D:

    data match {
      case Right((player, path)) if player.hasPermission(LibPerm.UnsafeFile) =>
        FileEditorHelper.loadOrCreate(path).onComplete {
          case Success(comp) =>
            val editor = Editor(comp, new CompEndSave, WeakReference(player), handler)
            player.sendMessage(
              t"${GREEN}You are now in a file editor. Just start typing. Type !end to save and end the session, and !help for more help"
            )
            editor.text.dataPut("path", path)
            handler.addEditorPlayer(player, editor)
          case Failure(e) =>
            player.sendMessage(t"${RED}Failed to load file")
            LogHelper.error("Failed to load file", e)
        }

        CommandResult.success()
      case Right((_, _)) =>
        throw new CommandPermissionException(t"You MUST have permission to use a file editor")
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
