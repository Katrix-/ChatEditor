package io.github.katrix.chateditor.persistant

import java.nio.file.Path

import org.spongepowered.api.text.format.TextColors._

import io.github.katrix.chateditor.EditorPlugin
import io.github.katrix.katlib.helper.Implicits._
import io.github.katrix.katlib.helper.LogHelper
import io.github.katrix.katlib.persistant.{ConfigLoader, ConfigValue}

class EditorConfigLoader(dir: Path)(implicit plugin: EditorPlugin) extends ConfigLoader[EditorConfig](dir) {

  override protected def loadVersionedData(version: String): EditorConfig = version match {
    case "1" => new EditorConfigV1(cfgRoot, default)
    case _ =>
      LogHelper.error("Invalid version in config. Loading default")
      default
  }

  override protected val default = new EditorConfig {
    override val version = ConfigValue("1", "Please don't touch this", Seq("version"))
    override val text = new Texts {
      override val textSet =
        ConfigValue(tt"${GREEN}Text behavior set to $Behavior", s"The text shown when switching text behavior", Seq("eCmd", "core", "setText"))
      override val endSet =
        ConfigValue(tt"${GREEN}End behavior set to $Behavior", "The text shown when switching end behavior", Seq("eCmd", "core", "setEnd"))

      override val commandBlockLocationSet = ConfigValue(
        tt"${GREEN}Commandblock location set to $Location",
        "The text shown when setting a new command block position",
        Seq("editor", "end", "commandblock", "setPos")
      )
      override val commandBlockStart = ConfigValue(
        tt"${GREEN}Created a new editor for the commandblock at $Location",
        "The text shown when creating a new editor for a commandblock",
        Seq("editor", "end", "commandblock", "create")
      )

      override val pathInvalid = ConfigValue(
        tt"${RED}The path specified is not valid: $Message",
        "The text shown when a path inserted is not valid",
        Seq("eCmd", "core", "setText", "file", "invalidPath")
      )

      override val commandEditorSuccess = ConfigValue(
        t"${GREEN}You are now in an editor. Just start typing. Type !end to end the session, and !help for more help",
        "The text shown when entering a editor from the editor command",
        Seq("cmd", "editor", "success")
      )
      override val commandEditorFileSuccess = ConfigValue(
        t"${GREEN}You are now in a file editor. Just start typing. Type !end to save and end the session, and !help for more help",
        "The text shown when entering a file editor from the editor file command",
        Seq("cmd", "editor", "file", "success")
      )
      override val commandEditorFilePermError = ConfigValue(
        t"You MUST have permission to use a file editor",
        "The message shown when trying to use the file editor command without permission",
        Seq("cmd", "editor", "file", "permError")
      )
      override val commandEditorFilePathNotFoundNoCreate = ConfigValue(
        t"No file with that name found, and file creation is off",
        "The message shown when trying to use the file editor and you pass inn a non-exisiting file, while file creation is off",
        Seq("cmd", "editor", "file", "pathNotFoundNoCreate")
      )

      override val commandErrorParseBoolean =
        ConfigValue(t"Failed to parse boolean", "The message shown when failing to parse a boolean", Seq("cmd", "error", "parseBoolean"))
      override val commandErrorInvalidPath =
        ConfigValue(t"Failed to parse path", "The message shown when failing to parse a path", Seq("cmd", "error", "parsePath"))

      override val eCommandMissingPerm = ConfigValue(
        t"You don't have the permission to use that command",
        "The message shown when a player tries to use an editor command they don't have permission for",
        Seq("eCommand", "error", "missingPerm")
      )
      override val eCommandIncompatible = ConfigValue(
        t"That command doesn't work with your editor",
        "The message shown when a player tries to use a command that is incompatible with their editor",
        Seq("eCommand", "error", "incompatibleEditor")
      )
      override val eCommandNotFound = ConfigValue(
        t"No command with that name found",
        "The message shown when a player tries to use a non existing command",
        Seq("eCommand", "error", "commandNotFound")
      )

      override val eCmdAddLine =
        ConfigValue(t"${GREEN}Added new line", "The message shown when a player uses the !addLine command", Seq("eCmd", "addLine"))

      override val eCmdHelpSpecifyCommand = ConfigValue(
        t"${RED}You need to specify a command",
        "The message shown when a player doesn't specify a command when using the help command. This permission node will be removed in a future version",
        Seq("eCmd", "help", "specifyCommand")
      )

      override val eCmdPosSelectInvalid = ConfigValue(
        t"${RED}The pos and select passed in are invalid",
        "The message shown when a player specifies an incorrect position or selection when using the posSelect command.",
        Seq("eCmd", "posSelect", "invalidArgs")
      )
      override val eCmdPosSelectSpecify = ConfigValue(
        t"${RED}You need to specify a position and a selection",
        "The message shown when a player doesn't specify a position and a selection when using the posSelect command.",
        Seq("eCmd", "posSelect", "specifyPosSelect")
      )

      override val eCmdCopyCutNoneSelected = ConfigValue(
        t"${RED}Please make a selection before using this command",
        "The message shown when a player tries to use a clipboard command with nothing selected.",
        Seq("eCmd", "copyPaste", "noneSelected")
      )
      override val eCmdCopy =
        ConfigValue(t"${GREEN}Copied the selected text", "The message shown when a player copies the text.", Seq("eCmd", "copy"))
      override val eCmdCut = ConfigValue(t"${GREEN}Cut the selected text", "The message shown when a player cuts the text.", Seq("eCmd", "cut"))
      override val eCmdPaste =
        ConfigValue(t"${GREEN}Pasted the text on the clipboard", "The message shown when a player pastes in text.", Seq("eCmd", "paste"))
      override val eCmdPasteClipboardEmpty = ConfigValue(
        t"${RED}The clipboard is empty",
        "The message shown when a player tries to paste something but the clipboard is empty",
        Seq("eCmd", "paste", "clipboardEmpty")
      )

      override val eCmdLintSuccess =
        ConfigValue(t"${GREEN}All good!", "The message shown when a player lints a text and there is no problem.", Seq("eCmd", "lint"))

      override val commandBlockErrorTileEntity = ConfigValue(
        t"${RED}Tile entity not found for commandBlock",
        "The message shown when a player tries to open an editor trough a command block, but the tileEntity is not found",
        Seq("editor", "end", "commandblock", "tileEntityNotFound")
      )
      override val commandBlockErrorLocation = ConfigValue(
        t"${RED}Could not get position for commandblock",
        "The message shown when a player tries to open an editor trough a command block, but the position is not present",
        Seq("editor", "end", "commandblock", "errorLocation")
      )

      override val behaviorMissing = ConfigValue(
        t"${RED}Please specify a behavior",
        "The message shown when a player tries to change an editor behavior, but doesn't provide it",
        Seq("eCmd", "behavior", "set", "missing")
      )
      override val behaviorUnknown = ConfigValue(
        t"${RED}Unknown behavior",
        "The message shown when a player tries to change an editor behavior, there is no such behavior",
        Seq("eCmd", "behavior", "set", "unknown")
      )
      override val behaviorMissingPerm = ConfigValue(
        t"${RED}You don't have the permission to use this behavior",
        "The message shown when a player tries to change an editor behavior, but doesn't have the permission for it",
        Seq("eCmd", "behavior", "set", "missingPerm")
      )

      override val endCommandNoCommand = ConfigValue(
        t"${RED}You need to specify some command before you can do this",
        "The message shown when a player tries to use !end with the command behavior when they don't have anything in they're editor",
        Seq("editor", "end", "command", "noCommand")
      )
      override val endCommandCommandNotFound = ConfigValue(
        t"${RED}Command not found",
        "The message shown when a player tries to use !end with the command behavior and no command is found",
        Seq("editor", "end", "command", "commandNotFound")
      )
      override val endCommandPermMissing = ConfigValue(
        t"${RED}You don't have the permission for that command",
        "The message shown when a player tries to use !end with the command behavior with a command they don't have permission for",
        Seq("editor", "end", "command", "permMissing")
      )

      override val endCommandBlockSet = ConfigValue(
        t"${GREEN}Set command for commandblock",
        "The message shown when a player sets a command for a commandblock",
        Seq("editor", "end", "commandblock", "set")
      )
      override val endCommandBlockSetError = ConfigValue(
        t"${RED}Error when setting command for commandblock",
        "The message shown when a player sets a command for a commandblock, and an error occurs",
        Seq("editor", "end", "commandblock", "setError")
      )
      override val endCommandBlockCommandNotFound = ConfigValue(
        t"${RED}Command not found",
        "The message shown when a player tries to use !end with the commandblock behavior and no command is found",
        Seq("editor", "end", "commandblock", "commandNotFound")
      )
      override val endCommandBlockPermMissing = ConfigValue(
        t"${RED}You don't have the permission for that command",
        "The message shown when a player tries to use !end with the commandblock behavior with a command they don't have permission for",
        Seq("editor", "end", "commandblock", "permMissing")
      )
      override val endCommandBlockNotFound = ConfigValue(
        t"${RED}Commandblock at that location not found",
        "The message shown when a player tries to set a command to a commandblock, but there is no commandblock at the specified location",
        Seq("editor", "end", "commandblock", "notFound")
      )

      override val endIncompatible = ConfigValue(
        t"${RED}Incompatible end behavior",
        "The message shown when the end behavior of an editor in incompatible with the editor",
        Seq("editor", "end", "incompatible")
      )

      override val endNOOP = ConfigValue(
        t"${GREEN}You have now exited the editor",
        "The message shown when using !end with the NOOP end behavior",
        Seq("editor", "end", "noop")
      )
      override val endSetNOOPHelp = ConfigValue(
        t"${GREEN}Type !end to exit the editor",
        "The message shown when setting the end behavior to NOOP",
        Seq("eCmd", "core", "setEnd", "noop")
      )

      override val endSetCommandBlockInvalidPos = ConfigValue(
        t"${RED}Invalid block pos",
        "The message shown when entering an invalid block pos when switching to the commandblock end behavior",
        Seq("eCmd", "core", "setEnd", "commandblock", "invalidPos")
      )
      override val endSetCommandBlockSpecifyPos = ConfigValue(
        t"${RED}Please specify a block position",
        "The message shown when entering not entering block pos when switching to the commandblock end behavior",
        Seq("eCmd", "core", "setEnd", "commandblock", "specifyPos")
      )

      override val endFileSaveSuccess = ConfigValue(
        t"${GREEN}File saved. Exited editor",
        "The message shown when using saving a file successfully with !end and file behavior",
        Seq("editor", "end", "file")
      )
      override val endFileSaveFailed = ConfigValue(
        t"${RED}Error when saving file.",
        "The message shown when using saving a file successfully with !end and file behavior and an error occurs",
        Seq("editor", "end", "file", "failed")
      )
      override val fileMissingPerm = ConfigValue(
        t"${RED}You don't have the permissions to do this",
        "The message shown when trying to do file stuff, and not having enough permission",
        Seq("file", "missingPerm")
      )
      override val fileNotOpen =
        ConfigValue(t"${GREEN}File not open", "The message shown when trying to do file stuff, and a file is not open", Seq("file", "notOpen"))
      override val fileSaved    = ConfigValue(t"${GREEN}File saved.", "The message shown when saving a file", Seq("file", "save"))
      override val fileReloaded = ConfigValue(t"${GREEN}File reloaded", "The message shown when reloading a file", Seq("file", "reload"))
    }
  }
}
