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

package com.guntherdw.bukkit.tweakcraft;

import com.guntherdw.bukkit.tweakcraft.Ban.BanHandler;
import com.guntherdw.bukkit.tweakcraft.Chat.ChatHandler;
import com.guntherdw.bukkit.tweakcraft.Chat.ChatMode;
import com.guntherdw.bukkit.tweakcraft.Exceptions.ChatModeException;
import com.guntherdw.bukkit.tweakcraft.Packages.Ban;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * @author GuntherDW
 */
public class TweakcraftPlayerListener extends PlayerListener {

    //private final Logger log = Logger.getLogger("Minecraft");
    private final TweakcraftUtils plugin;
    private List<String> invisplayers;


    public TweakcraftPlayerListener(TweakcraftUtils instance) {
        plugin = instance;
        invisplayers = new ArrayList<String>();
    }

    public List<String> getInvisplayers() {
        return invisplayers;
    }

    public void onPlayerChat(PlayerChatEvent event) {

        Player player = event.getPlayer();
        String message = event.getMessage();
        String name = player.getName();
        player.setDisplayName(plugin.getPlayerColor(name, false) + name + ChatColor.WHITE);

        ChatHandler ch = plugin.getChathandler();
        ChatMode cm = ch.getPlayerChatMode(player);



        if (ch.getMutedPlayers().contains(player.getName().toLowerCase())) {
            player.sendMessage(ChatColor.GOLD + "You are muted! No one can hear you.");
            plugin.getLogger().info("[TweakcraftUtils] Muted player message : <" + event.getPlayer().getName() + "> " + event.getMessage());
            event.setCancelled(true);
        } else {


            if (cm != null) {
                if (!message.startsWith(plugin.getChathandler().getBypassChar())) {
                    cm.sendMessage(player, message);
                    event.setCancelled(true);
                } else {
                    event.setMessage(message.substring(1));
                }
            } else if(cm == null && getInvisplayers().contains(event.getPlayer().getName())) {
                event.getPlayer().sendMessage(ChatColor.RED + "Are you insane? You're invisible, set a chatmode!");
                event.setCancelled(true);
            }
        }
    }

    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Location floc = event.getFrom();
        Location tloc = event.getTo();
        if (floc.getWorld() != tloc.getWorld()) { // The world is different, make a check!
            Player player = event.getPlayer();
            if (!plugin.check(player, "worlds." + tloc.getWorld().getName())) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You don't have access to this world!");
            }
        }
    }


    public void onPlayerLogin(PlayerLoginEvent event) {
        BanHandler handler = plugin.getBanhandler();
        Ban isBanned = handler.searchBan(event.getPlayer().getName());
        if (isBanned != null) {
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, isBanned.getReason());
        }
    }


    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        String name = p.getName();
        event.getPlayer().setDisplayName(plugin.getPlayerColor(name, false) + name + ChatColor.WHITE);
        // p.sendMessage("Ohai thar!");
        for (String m : plugin.getMOTD())
            p.sendMessage(m);

        if(getInvisplayers().contains(event.getPlayer().getName())) { // Invisible players do not send out a "joined" message
            event.setJoinMessage(null);
            p.sendMessage(ChatColor.AQUA + "You has joined STEALTHILY!");
            if (plugin.getCraftIRC() != null) {
                plugin.getCraftIRC().sendMessageToTag("STEALTH JOIN : " +event.getPlayer().getName() ,"mchatadmin");
            }
            /* try {
                ChatHandler ch = plugin.getChathandler();
                ChatMode    cm = ch.getChatMode("admin");
                AdminChat   am = (AdminChat) cm; */
                for(Player play : plugin.getServer().getOnlinePlayers())
                {
                    if(plugin.check(play, "tpinvis"))
                    {
                        play.sendMessage(ChatColor.AQUA+"Stealth join : "+event.getPlayer().getDisplayName());
                    }
                }
                // am.broadcastMessageRealAdmins(ChatColor.AQUA+"Stealth join : "+event.getPlayer().getDisplayName());
            /* } catch (ChatModeException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } */
        }
    }

    public void onPlayerQuit(PlayerQuitEvent event) {
        if (plugin.getConfigHandler().enableSeenConfig) {
            Calendar cal = Calendar.getInstance();
            String time = String.valueOf(cal.getTime().getTime());
            plugin.getConfigHandler().getSeenconfig().setProperty(event.getPlayer().getName().toLowerCase(), time);
            plugin.getConfigHandler().getSeenconfig().save();
            plugin.getLogger().info("[TweakcrafUtils] Stored " + event.getPlayer().getName() + "'s logout!");
        }
        plugin.getChathandler().removePlayer(event.getPlayer());
        try {
            plugin.getChathandler().setPlayerchatmode(event.getPlayer().getName(), null);
        } catch (ChatModeException e) {
            plugin.getLogger().severe("[TweakcraftUtils] Error setting ChatMode to null after the logout!");
        }
        if(getInvisplayers().contains(event.getPlayer().getName())) { // Invisible players do not send out a "left" message
            event.setQuitMessage(null);
            if (plugin.getCraftIRC() != null) {
                plugin.getCraftIRC().sendMessageToTag("STEALTH QUIT : " +event.getPlayer().getName() ,"mchatadmin");
            }
            /* try {
                ChatHandler ch = plugin.getChathandler();
                ChatMode    cm = ch.getChatMode("admin");
                AdminChat   am = (AdminChat) cm; */
                for(Player play : plugin.getServer().getOnlinePlayers())
                {
                    if(plugin.check(play, "tpinvis"))
                    {
                        play.sendMessage(ChatColor.AQUA+"Stealth quit : "+event.getPlayer().getDisplayName());
                    }
                }
                // am.broadcastMessageRealAdmins(ChatColor.AQUA+"Stealth join : "+event.getPlayer().getDisplayName());
            /* } catch (ChatModeException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } */
        }
    }

    public void onPlayerInteract(PlayerInteractEvent event) {
        String playername = event.getPlayer().getName();
        if(plugin.getConfigHandler().getLsbindmap().containsKey(playername)) {
            Map<Integer, Boolean> bind = plugin.getConfigHandler().getLsbindmap().get(playername);
            for(Integer i : bind.keySet()) {

                if(((event.getItem()==null)&&i==0)
                        || (event.getItem().getTypeId() == i)) {
                    Action a = event.getAction();
                    if((!bind.get(i) && (a.equals(Action.RIGHT_CLICK_AIR) || a.equals(Action.RIGHT_CLICK_BLOCK)))
                    || ( bind.get(i) && (a.equals(Action. LEFT_CLICK_AIR) || a.equals(Action. LEFT_CLICK_BLOCK)))) {
                        Location target = null;
                        if(plugin.getConfigHandler().getLockdowns().containsKey(playername)) {
                            target = plugin.getConfigHandler().getLockdowns().get(playername).getTarget();
                        } else {
                            Location loc = event.getPlayer().getTargetBlock(null, 200).getLocation();
                            loc.setY(loc.getY()+1);
                            target = loc.clone();
                        }
                        if(target!=null) {
                            target.getWorld().strikeLightning(target);
                        }
                    }
                }
                
            }
        }
    }

    public void reloadInvisTable() {
        List<String> lijst = plugin.getConfiguration().getStringList("invisible-playerlist", null);
        this.invisplayers.clear();
        if(lijst != null)
        {
            this.invisplayers.addAll(lijst);
        }
        for(String s : lijst) {
            plugin.getLogger().info("[TweakcraftUtils] Adding "+s+" to the invisble playerlist!");
        }
    }

}
