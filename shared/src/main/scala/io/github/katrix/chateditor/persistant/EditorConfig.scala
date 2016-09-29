package io.github.katrix.chateditor.persistant

import org.spongepowered.api.text.{Text, TextTemplate}

import io.github.katrix.katlib.persistant.{Config, ConfigValue}

trait EditorConfig extends Config {

	val text: Texts

	trait Texts {

		final val Location = "location"
		final val Behavior = "behavior"
		final val Message  = "message"

		val textSet: ConfigValue[TextTemplate] //1 arg behavior
		val endSet: ConfigValue[TextTemplate] //1 arg behavior

		val commandBlockLocationSet: ConfigValue[TextTemplate] //1 arg location
		val commandBlockStart: ConfigValue[TextTemplate] //1 arg location

		val pathInvalid: ConfigValue[TextTemplate] //1 arg message


		val commandEditorSuccess                 : ConfigValue[Text]
		val commandEditorFileSuccess             : ConfigValue[Text]
		val commandEditorFilePathNotFoundNoCreate: ConfigValue[Text]
		val commandEditorFilePermError           : ConfigValue[Text]

		val commandErrorParseBoolean: ConfigValue[Text]
		val commandErrorInvalidPath : ConfigValue[Text]

		val eCommandMissingPerm : ConfigValue[Text]
		val eCommandNotFound    : ConfigValue[Text]
		val eCommandIncompatible: ConfigValue[Text]

		val eCmdAddLine: ConfigValue[Text]

		val eCmdHelpSpecifyCommand: ConfigValue[Text]

		val eCmdPosSelectInvalid: ConfigValue[Text]
		val eCmdPosSelectSpecify: ConfigValue[Text]

		val eCmdCopyCutNoneSelected: ConfigValue[Text]
		val eCmdCopy               : ConfigValue[Text]
		val eCmdCut                : ConfigValue[Text]
		val eCmdPaste              : ConfigValue[Text]
		val eCmdPasteClipboardEmpty: ConfigValue[Text]

		val eCmdLintSuccess: ConfigValue[Text]

		val commandBlockErrorTileEntity: ConfigValue[Text]
		val commandBlockErrorLocation  : ConfigValue[Text]

		val pathMissing        : ConfigValue[Text]
		val behaviorMissing    : ConfigValue[Text]
		val behaviorUnknown    : ConfigValue[Text]
		val behaviorMissingPerm: ConfigValue[Text]

		val endCommandNoCommand      : ConfigValue[Text]
		val endCommandCommandNotFound: ConfigValue[Text]
		val endCommandPermMissing    : ConfigValue[Text]

		val endCommandBlockSet            : ConfigValue[Text]
		val endCommandBlockSetError       : ConfigValue[Text]
		val endCommandBlockCommandNotFound: ConfigValue[Text]
		val endCommandBlockPermMissing    : ConfigValue[Text]
		val endCommandBlockNotFound       : ConfigValue[Text]

		val endIncompatible: ConfigValue[Text]

		val endNOOP       : ConfigValue[Text]
		val endSetNOOPHelp: ConfigValue[Text]

		val endSetCommandBlockInvalidPos: ConfigValue[Text]
		val endSetCommandBlockSpecifyPos: ConfigValue[Text]

		val endFileSaveSuccess: ConfigValue[Text]
		val endFileSaveFailed : ConfigValue[Text]
		val fileMissingPerm   : ConfigValue[Text]
		val fileNotOpen       : ConfigValue[Text]
		val fileSaved         : ConfigValue[Text]
		val fileReloaded      : ConfigValue[Text]
	}

	override def seq: Seq[ConfigValue[_]] = Seq(
		version,

		text.textSet,
		text.endSet,

		text.commandBlockLocationSet,
		text.commandBlockStart,

		text.pathInvalid,

		text.commandEditorSuccess,
		text.commandEditorFileSuccess,
		text.commandEditorFilePathNotFoundNoCreate,
		text.commandEditorFilePermError,
		text.commandEditorFilePermError,

		text.commandErrorParseBoolean,
		text.commandErrorInvalidPath,

		text.eCmdAddLine,

		text.eCmdHelpSpecifyCommand,

		text.eCmdPosSelectInvalid,
		text.eCmdPosSelectSpecify,

		text.eCmdCopyCutNoneSelected,
		text.eCmdCopy,
		text.eCmdCut,
		text.eCmdPaste,
		text.eCmdPasteClipboardEmpty,

		text.eCmdLintSuccess,

		text.commandBlockErrorTileEntity,
		text.commandBlockErrorLocation,

		text.pathMissing,
		text.behaviorMissing,
		text.behaviorUnknown,
		text.behaviorMissingPerm,

		text.endCommandNoCommand,
		text.endCommandCommandNotFound,
		text.endCommandPermMissing,

		text.endCommandBlockSet,
		text.endCommandBlockSetError,
		text.endCommandCommandNotFound,
		text.endCommandBlockPermMissing,
		text.endCommandBlockNotFound,

		text.endIncompatible,

		text.endNOOP,
		text.endSetNOOPHelp,

		text.endSetCommandBlockInvalidPos,
		text.endSetCommandBlockSpecifyPos,

		text.endFileSaveSuccess,
		text.endFileSaveFailed,
		text.fileMissingPerm,
		text.fileNotOpen,
		text.fileSaved,
		text.fileReloaded
	)
}
