/**
 * This file is part of PermissionBlock, licensed under the MIT License (MIT).
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
package io.github.katrix_.permissionblock.editor;

import java.util.List;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

public class EditorJson extends EditorLineAbstract {

	public EditorJson(List<String> stringList) {
		super(stringList);
	}

	public EditorJson(String string) {
		super(string);
	}

	@Override
	public List<Text> getFormattedText() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		JsonElement element = gson.toJsonTree(getBuiltString()); //TODO
		String formatted = gson.toJson(element);
		return super.getFormattedText();
	}

	@Override
	public boolean end(Player player) {
		return false;
	}

	public static void main(String[] args) {
		String nbt = "{DropChances:[0:0.085f,1:0.085f,2:0.085f,3:0.085f,4:0.085f],Age:0,UUIDLeast:-6587974176882625461L,"
				+ "Attributes:[0:{Name:\"generic.maxHealth\",Base:4.0d},1:{Name:\"generic.knockbackResistance\",Base:0.0d},2:{Name:\"generic"
				+ ".movementSpeed\",Base:0.25d},3:{Name:\"generic.followRange\",Base:16.0d,Modifiers:[0:{Name:\"Random spawn bonus\","
				+ "UUIDLeast:-7151323691541476538L,Operation:1,Amount:-0.019585756731058196d,UUIDMost:941504467614123631L}]}],IsChickenJockey:0b,"
				+ "Motion:[0:-3.719184344028112E-4d,1:-0.0784000015258789d,2:-0.0733363376799967d],test:2,Health:4s,HealF:4.0f,Bukkit.updateLevel:2,"
				+ "Fire:-1s,Invulnerable:0b,DeathTime:0s,ForcedAge:0,Equipment:[0:{},1:{},2:{},3:{},4:{}],AbsorptionAmount:0.0f,InLove:0,"
				+ "OnGround:1b,HurtTime:0s,AgeLocked:0b,UUIDMost:-8914269988870142553L,HurtByTimestamp:0,Dimension:0,"
				+ "WorldUUIDLeast:-8214179146566002376L,Air:300s,Pos:[0:328.5118353835865d,1:43.0d,2:-7152.507890682375d],CanPickUpLoot:0b,"
				+ "EggLayTime:5219,PortalCooldown:0,PersistenceRequired:1b,Leashed:0b,WorldUUIDMost:-6365660778832771081L,FallDistance:0.0f,"
				+ "Rotation:[0:179.36337f,1:0.0f],Spigot.ticksLived:1087}";
	}
}
