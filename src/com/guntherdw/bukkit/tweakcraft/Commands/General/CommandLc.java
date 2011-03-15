package com.guntherdw.bukkit.tweakcraft.Commands.General;

import com.guntherdw.bukkit.tweakcraft.ChatMode;
import com.guntherdw.bukkit.tweakcraft.Command;
import com.guntherdw.bukkit.tweakcraft.Exceptions.*;
import com.guntherdw.bukkit.tweakcraft.TweakcraftUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author GuntherDW
 */
public class CommandLc implements Command {

    public boolean executeCommand(CommandSender sender, String command, String[] args, TweakcraftUtils plugin)
            throws PermissionsException, CommandSenderException, CommandUsageException, CommandException {
        if(sender instanceof Player)
        {
            if(!plugin.check((Player)sender, "localchat"))
                throw new PermissionsException(command);

            try {
                ChatMode cm = plugin.getChathandler().getChatMode("local");
                List<String> sublist = cm.getSubscribers();
                if(!sublist.contains(((Player) sender).getName()))
                {
                    cm.addRecipient(((Player) sender).getName());
                    plugin.getChathandler().setPlayerchatmode(((Player) sender).getName(),"local");
                    sender.sendMessage(ChatColor.YELLOW + "You will now chat locally!");
                } else {
                    cm.removeRecipient(((Player) sender).getName());
                    plugin.getChathandler().setPlayerchatmode(((Player) sender).getName(), null);
                    sender.sendMessage(ChatColor.YELLOW + "You will now chat globally!");
                }

            } catch (ChatModeException e) {
                throw new CommandException("Exception thrown when setting chatmode!");
            }
        } else {
            // It's the console!
            throw new CommandSenderException("You need to be a player to use LocalChat!");
        }
        return true;
    }
}
