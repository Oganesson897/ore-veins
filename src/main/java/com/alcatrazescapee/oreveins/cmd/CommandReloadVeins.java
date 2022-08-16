/*
 * Part of the Ore Veins Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.oreveins.cmd;

import com.alcatrazescapee.oreveins.vein.VeinRegistry;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class CommandReloadVeins extends CommandBase
{
    @Override
    @Nonnull
    public String getName()
    {
        return "reloadveins";
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender sender)
    {
        return "/reloadveins -> Reloads all registered veins";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args)
    {
        sender.sendMessage(new TextComponentString(TextFormatting.GREEN +"Reloading veins..."));
        VeinRegistry.reloadVeins();
    }
}
