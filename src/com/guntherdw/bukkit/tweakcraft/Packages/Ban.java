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

package com.guntherdw.bukkit.tweakcraft.Packages;

/**
 * @author GuntherDW
 */
public class Ban {

    String player;
    String reason;
    Long   toTime;

    public Ban(String Player, String Reason) {
        this.player = Player;
        this.reason = Reason;
        this.toTime = null;
    }

    public Ban(String Player, String Reason, Long ToTime) {
        this.player = Player;
        this.reason = Reason;
        this.toTime = ToTime;
    }

    public String getPlayer() {
        return player;
    }

    public String getReason() {
        if (reason == null || reason.isEmpty()) {
            return "You are banned!";
        } else {
            return reason;
        }
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Long getToTime() {
        return toTime;
    }

    public void setToTime(Long toTime) {
        this.toTime = toTime;
    }
}
