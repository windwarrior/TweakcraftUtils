/*
 * Copyright (c) 2011 GuntherDW
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package com.guntherdw.bukkit.tweakcraft.Commands.Essentials;

import com.guntherdw.bukkit.tweakcraft.Commands.iCommand;
import com.guntherdw.bukkit.tweakcraft.Exceptions.CommandException;
import com.guntherdw.bukkit.tweakcraft.Exceptions.CommandSenderException;
import com.guntherdw.bukkit.tweakcraft.Exceptions.CommandUsageException;
import com.guntherdw.bukkit.tweakcraft.Exceptions.PermissionsException;
import com.guntherdw.bukkit.tweakcraft.Packages.Ban;
import com.guntherdw.bukkit.tweakcraft.TweakcraftUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author GuntherDW
 */
public class  CommandBanlist implements iCommand {
    public boolean executeCommand(CommandSender sender, String command, String[] args, TweakcraftUtils plugin)
            throws PermissionsException, CommandSenderException, CommandUsageException, CommandException {
        if (sender instanceof Player)
            if (!plugin.check((Player) sender, "ban"))
                throw new PermissionsException(command);

        String banned = "";
        if(args.length>0) {
            String tofind = args[0];
            Ban ban = plugin.getBanhandler().isBannedBan(tofind);
            if(ban!=null) {
                banned = ChatColor.YELLOW + tofind + " is still banned for "+plugin.getBanhandler().getRemainingTime(tofind);
            } else {
                banned = ChatColor.YELLOW + tofind +" isn't banned!";
            }
        } else {

            String banmsg = ChatColor.YELLOW + "Currently banned players : ";
            sender.sendMessage(banmsg);
            banned = "";

            for (String banname : plugin.getBanhandler().getBans().keySet()) {
                banned += banname + " ";
            }
            if (banned.length() > 1)
                banned = banned.substring(0, banned.length() - 1);

        }
        sender.sendMessage(banned);

        return true;
    }

    @Override
    public String getPermissionSuffix() {
       return "ban";
    }
}
