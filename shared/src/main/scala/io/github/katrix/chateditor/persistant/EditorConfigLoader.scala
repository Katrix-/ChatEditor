package io.github.katrix.chateditor.persistant

import java.nio.file.Path

import org.spongepowered.api.text.Text
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

	override protected val default: EditorConfig = new EditorConfig {
		override val version = ConfigValue("1", "Please don't touch this", Seq("version"))
		override val text    = new Texts {
			override val textSet = ConfigValue(
				tt"${GREEN}Text behavior set to $Behavior",
				s"The text shown when switching text behavior",
				Seq("eCmd", "core", "setText"))
			override val endSet  = ConfigValue(
				tt"${GREEN}End behavior set to $Behavior",
				"The text shown when switching end behavior",
				Seq("eCmd", "core", "setEnd"))

			override val commandBlockLocationSet = ConfigValue(
				tt"${GREEN}Commandblock location set to $Location",
				"The text shown when setting a new command block position",
				Seq("editor", "end", "commandblock", "setpos"))
			override val commandBlockStart       = ConfigValue(
				tt"${GREEN}Created a new editor for the commandblock at $Location",
				"The text shown when creating a new editor for a commandblock",
				Seq("editor", "end", "commandblock", "create")
			)

			override val pathInvalid = ConfigValue(
				tt"${RED}The path specified is not valid: $Message",
				"The text shown when a path inserted is not valid",
				Seq("ECmd", "core", "setText", "file", "invalidPath")
			)

			override val commandEditorSuccess                 : ConfigValue[Text] = ???
			override val commandEditorFileSuccess             : ConfigValue[Text] = ???
			override val commandEditorFilePermError           : ConfigValue[Text] = ???
			override val commandEditorFilePathNotFoundNoCreate: ConfigValue[Text] = ???

			override val commandErrorParseBoolean: ConfigValue[Text] = ???
			override val commandErrorInvalidPath : ConfigValue[Text] = ???

			override val eCommandMissingPerm : ConfigValue[Text] = ???
			override val eCommandIncompatible: ConfigValue[Text] = ???
			override val eCommandNotFound    : ConfigValue[Text] = ???

			override val eCmdAddLine: ConfigValue[Text] = ???

			override val eCmdHelpSpecifyCommand: ConfigValue[Text] = ???

			override val eCmdPosSelectInvalid: ConfigValue[Text] = ???
			override val eCmdPosSelectSpecify: ConfigValue[Text] = ???

			override val eCmdCopyCutNoneSelected: ConfigValue[Text] = ???
			override val eCmdCopy               : ConfigValue[Text] = ???
			override val eCmdCut                : ConfigValue[Text] = ???
			override val eCmdPaste              : ConfigValue[Text] = ???
			override val eCmdPasteClipboardEmpty: ConfigValue[Text] = ???

			override val eCmdLintSuccess: ConfigValue[Text] = ???

			override val commandBlockErrorTileEntity: ConfigValue[Text] = ???
			override val commandBlockErrorLocation  : ConfigValue[Text] = ???

			override val pathMissing        : ConfigValue[Text] = ???
			override val behaviorMissing    : ConfigValue[Text] = ???
			override val behaviorUnknown    : ConfigValue[Text] = ???
			override val behaviorMissingPerm: ConfigValue[Text] = ???

			override val endCommandNoCommand      : ConfigValue[Text] = ???
			override val endCommandCommandNotFound: ConfigValue[Text] = ???
			override val endCommandPermMissing    : ConfigValue[Text] = ???

			override val endCommandBlockSet            : ConfigValue[Text] = ???
			override val endCommandBlockSetError       : ConfigValue[Text] = ???
			override val endCommandBlockCommandNotFound: ConfigValue[Text] = ???
			override val endCommandBlockPermMissing    : ConfigValue[Text] = ???
			override val endCommandBlockNotFound       : ConfigValue[Text] = ???

			override val endIncompatible: ConfigValue[Text] = ???

			override val endNOOP       : ConfigValue[Text] = ???
			override val endSetNOOPHelp: ConfigValue[Text] = ???

			override val endSetCommandBlockInvalidPos: ConfigValue[Text] = ???
			override val endSetCommandBlockSpecifyPos: ConfigValue[Text] = ???

			override val endFileSaveSuccess: ConfigValue[Text] = ???
			override val endFileSaveFailed : ConfigValue[Text] = ???
			override val fileMissingPerm   : ConfigValue[Text] = ???
			override val fileNotOpen       : ConfigValue[Text] = ???
			override val fileSaved         : ConfigValue[Text] = ???
			override val fileReloaded      : ConfigValue[Text] = ???
		}
	}
}
