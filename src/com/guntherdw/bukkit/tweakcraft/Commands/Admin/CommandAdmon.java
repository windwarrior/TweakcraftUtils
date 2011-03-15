package com.guntherdw.bukkit.tweakcraft.Commands.Admin;

import com.guntherdw.bukkit.tweakcraft.Chat.Modes.AdminChat;
import com.guntherdw.bukkit.tweakcraft.ChatMode;
import com.guntherdw.bukkit.tweakcraft.Command;
import com.guntherdw.bukkit.tweakcraft.Exceptions.*;
import com.guntherdw.bukkit.tweakcraft.TweakcraftUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author GuntherDW
 */
public class CommandAdmon implements Command {
    public boolean executeCommand(CommandSender sender, String command, String[] args, TweakcraftUtils plugin)
            throws PermissionsException, CommandSenderException, CommandException, CommandUsageException {
        if(sender instanceof Player)
        {
            try {
                ChatMode cm = plugin.getChathandler().getChatMode("admin");
                if(cm.getSubscribers().contains(((Player) sender).getName())
                        || ((AdminChat)cm).getAdminsString().contains(((Player) sender).getName()))
                {
                    plugin.getChathandler().setPlayerchatmode(((Player) sender).getName(), "admin");
                    sender.sendMessage(ChatColor.YELLOW + "You will now automatically send admin-msges!");
                } else {
                    throw new PermissionsException(command);
                }

            } catch (ChatModeException e) {
                throw new CommandException("Error occured while trying to fetch ChatMode!");
            }
        } else {
            throw new CommandSenderException("Do you really need this as a console?");
        }

        return true;
    }
}
