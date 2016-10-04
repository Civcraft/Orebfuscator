/*
 * Copyright (C) 2011-2014 lishid.  All rights reserved.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation,  version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.lishid.orebfuscator;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.lishid.orebfuscator.cache.CacheCleaner;
import com.lishid.orebfuscator.cache.ObfuscatedDataCache;
import com.lishid.orebfuscator.commands.OrebfuscatorCommandExecutor;
import com.lishid.orebfuscator.hithack.BlockHitManager;
import com.lishid.orebfuscator.hook.ProtocolLibHook;
import com.lishid.orebfuscator.listeners.OrebfuscatorBlockListener;
import com.lishid.orebfuscator.listeners.OrebfuscatorChunkListener;
import com.lishid.orebfuscator.listeners.OrebfuscatorEntityListener;
import com.lishid.orebfuscator.listeners.OrebfuscatorPlayerListener;
import com.lishid.orebfuscator.nms.INmsManager;

/**
 * Orebfuscator Anti X-RAY
 *
 * @author lishid
 */
public class Orebfuscator extends JavaPlugin {

    public static final Logger logger = Logger.getLogger("Minecraft.OFC");
    public static Orebfuscator instance;
    
    public static INmsManager nms;
    
    private boolean isProtocolLibFound;
    public boolean getIsProtocolLibFound() {
    	return this.isProtocolLibFound;
    }

    @SuppressWarnings("deprecation")
	@Override
    public void onEnable() {
        // Get plugin manager
        PluginManager pm = getServer().getPluginManager();

        instance = this;
        nms = createNmsManager();
        
        // Load configurations
        OrebfuscatorConfig.load();
        
        this.isProtocolLibFound = pm.getPlugin("ProtocolLib") != null;

        if (!this.isProtocolLibFound) {
            Orebfuscator.log("ProtocolLib is not found! Plugin cannot be enabled.");
            return;
        }
        
        // Orebfuscator events
        pm.registerEvents(new OrebfuscatorPlayerListener(), this);
        pm.registerEvents(new OrebfuscatorEntityListener(), this);
        pm.registerEvents(new OrebfuscatorBlockListener(), this);
        pm.registerEvents(new OrebfuscatorChunkListener(), this);

        (new ProtocolLibHook()).register(this);
        
        // Run CacheCleaner
        getServer().getScheduler().scheduleAsyncRepeatingTask(this, new CacheCleaner(), 0, OrebfuscatorConfig.CacheCleanRate);        
    }
    
    private static INmsManager createNmsManager() {

        String serverVersion = org.bukkit.Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        
        if(serverVersion.equals("v1_10_R1")) {
            return new com.lishid.orebfuscator.nms.v1_10_R1.NmsManager();
        }
        else if(serverVersion.equals("v1_9_R2")) {
            return new com.lishid.orebfuscator.nms.v1_9_R2.NmsManager();
        }
        else if(serverVersion.equals("v1_9_R1")) {
            return new com.lishid.orebfuscator.nms.v1_9_R1.NmsManager();
        }
        else return null;
    }

    @Override
    public void onDisable() {
        ObfuscatedDataCache.closeCacheFiles();
        BlockHitManager.clearAll();
        getServer().getScheduler().cancelTasks(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return OrebfuscatorCommandExecutor.onCommand(sender, command, label, args);
    }

    public void runTask(Runnable task) {
        if (this.isEnabled()) {
            getServer().getScheduler().runTask(this, task);
        }
    }

    /**
     * Log an information
     */
    public static void log(String text) {
        logger.info("[OFC] " + text);
    }

    /**
     * Log an error
     */
    public static void log(Throwable e) {
        logger.severe("[OFC] " + e.toString());
        e.printStackTrace();
    }

    /**
     * Send a message to a player
     */
    public static void message(CommandSender target, String message) {
        target.sendMessage(ChatColor.AQUA + "[OFC] " + message);
    }
}
