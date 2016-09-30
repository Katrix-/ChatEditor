package io.github.katrix.chateditor.persistant

import io.github.katrix.chateditor.EditorPlugin
import io.github.katrix.katlib.persistant.ConfigValue
import ninja.leaping.configurate.commented.CommentedConfigurationNode

class EditorConfigV1(cfgRoot: CommentedConfigurationNode, default: EditorConfig)(implicit plugin: EditorPlugin) extends EditorConfig {

	override val version = ConfigValue(cfgRoot, default.version)

	override val text = new Texts {
		override val textSet = ConfigValue(cfgRoot, default.text.textSet)
		override val endSet  = ConfigValue(cfgRoot, default.text.endSet)

		override val commandBlockLocationSet = ConfigValue(cfgRoot, default.text.commandBlockLocationSet)
		override val commandBlockStart       = ConfigValue(cfgRoot, default.text.commandBlockStart)

		override val pathInvalid = ConfigValue(cfgRoot, default.text.pathInvalid)

		override val commandEditorSuccess                  = ConfigValue(cfgRoot, default.text.commandEditorSuccess)
		override val commandEditorFileSuccess              = ConfigValue(cfgRoot, default.text.commandEditorFileSuccess)
		override val commandEditorFilePermError            = ConfigValue(cfgRoot, default.text.commandEditorFilePermError)
		override val commandEditorFilePathNotFoundNoCreate = ConfigValue(cfgRoot, default.text.commandEditorFilePathNotFoundNoCreate)

		override val commandErrorParseBoolean = ConfigValue(cfgRoot, default.text.commandErrorParseBoolean)
		override val commandErrorInvalidPath  = ConfigValue(cfgRoot, default.text.commandErrorInvalidPath)

		override val eCommandMissingPerm  = ConfigValue(cfgRoot, default.text.eCommandMissingPerm)
		override val eCommandIncompatible = ConfigValue(cfgRoot, default.text.eCommandIncompatible)
		override val eCommandNotFound     = ConfigValue(cfgRoot, default.text.eCommandNotFound)

		override val eCmdAddLine = ConfigValue(cfgRoot, default.text.eCmdAddLine)

		override val eCmdHelpSpecifyCommand = ConfigValue(cfgRoot, default.text.eCmdHelpSpecifyCommand)

		override val eCmdPosSelectInvalid = ConfigValue(cfgRoot, default.text.eCmdPosSelectInvalid)
		override val eCmdPosSelectSpecify = ConfigValue(cfgRoot, default.text.eCmdPosSelectSpecify)

		override val eCmdCopyCutNoneSelected = ConfigValue(cfgRoot, default.text.eCmdCopyCutNoneSelected)
		override val eCmdCopy                = ConfigValue(cfgRoot, default.text.eCmdCopy)
		override val eCmdCut                 = ConfigValue(cfgRoot, default.text.eCmdCut)
		override val eCmdPaste               = ConfigValue(cfgRoot, default.text.eCmdPaste)
		override val eCmdPasteClipboardEmpty = ConfigValue(cfgRoot, default.text.eCmdPasteClipboardEmpty)

		override val eCmdLintSuccess = ConfigValue(cfgRoot, default.text.eCmdLintSuccess)

		override val commandBlockErrorTileEntity = ConfigValue(cfgRoot, default.text.commandBlockErrorTileEntity)
		override val commandBlockErrorLocation   = ConfigValue(cfgRoot, default.text.commandBlockErrorLocation)

		override val behaviorMissing     = ConfigValue(cfgRoot, default.text.behaviorMissing)
		override val behaviorUnknown     = ConfigValue(cfgRoot, default.text.behaviorUnknown)
		override val behaviorMissingPerm = ConfigValue(cfgRoot, default.text.behaviorMissingPerm)

		override val endCommandNoCommand       = ConfigValue(cfgRoot, default.text.endCommandNoCommand)
		override val endCommandCommandNotFound = ConfigValue(cfgRoot, default.text.endCommandCommandNotFound)
		override val endCommandPermMissing     = ConfigValue(cfgRoot, default.text.endCommandPermMissing)

		override val endCommandBlockSet             = ConfigValue(cfgRoot, default.text.endCommandBlockSet)
		override val endCommandBlockSetError        = ConfigValue(cfgRoot, default.text.endCommandBlockSetError)
		override val endCommandBlockCommandNotFound = ConfigValue(cfgRoot, default.text.endCommandBlockCommandNotFound)
		override val endCommandBlockPermMissing     = ConfigValue(cfgRoot, default.text.endCommandBlockPermMissing)
		override val endCommandBlockNotFound        = ConfigValue(cfgRoot, default.text.endCommandBlockNotFound)

		override val endIncompatible = ConfigValue(cfgRoot, default.text.endIncompatible)

		override val endNOOP        = ConfigValue(cfgRoot, default.text.endNOOP)
		override val endSetNOOPHelp = ConfigValue(cfgRoot, default.text.endSetNOOPHelp)

		override val endSetCommandBlockInvalidPos = ConfigValue(cfgRoot, default.text.endSetCommandBlockInvalidPos)
		override val endSetCommandBlockSpecifyPos = ConfigValue(cfgRoot, default.text.endSetCommandBlockSpecifyPos)

		override val endFileSaveSuccess = ConfigValue(cfgRoot, default.text.endFileSaveSuccess)
		override val endFileSaveFailed  = ConfigValue(cfgRoot, default.text.endFileSaveFailed)
		override val fileMissingPerm    = ConfigValue(cfgRoot, default.text.fileMissingPerm)
		override val fileNotOpen        = ConfigValue(cfgRoot, default.text.fileNotOpen)
		override val fileSaved          = ConfigValue(cfgRoot, default.text.fileSaved)
		override val fileReloaded       = ConfigValue(cfgRoot, default.text.fileReloaded)
	}
}
