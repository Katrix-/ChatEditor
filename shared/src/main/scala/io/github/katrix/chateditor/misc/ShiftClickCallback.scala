package io.github.katrix.chateditor.misc

import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.text.action.ShiftClickAction

case class ShiftClickCallback(callback: CommandSource => Unit) extends ShiftClickAction[CommandSource => Unit](callback)