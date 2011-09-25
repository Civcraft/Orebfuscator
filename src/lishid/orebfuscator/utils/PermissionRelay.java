<<<<<<< HEAD
package lishid.orebfuscator.utils;

import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;


public class PermissionRelay {
    public static PermissionHandler handler;
    
    public static void Setup(PluginManager pm)
    {
        if (handler == null) {
            if (pm.getPlugin("Permissions") != null) {
            	handler = ((Permissions) pm.getPlugin("Permissions")).getHandler();
            } else {
                //log.info("Permission system not detected, defaulting to OP");
            }
        }
    }
    
	public static boolean hasPermission(Player player, String permission)
	{
		if (handler == null) {
			return player.isOp();
        }else{
        	return handler.has(player, permission);
        }
	}

}
=======
package lishid.orebfuscator.utils;

import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;


public class PermissionRelay {
    public static PermissionHandler handler;
    
    public static void Setup(PluginManager pm)
    {
        if (handler == null) {
            if (pm.getPlugin("Permissions") != null) {
            	handler = ((Permissions) pm.getPlugin("Permissions")).getHandler();
            } else {
                //log.info("Permission system not detected, defaulting to OP");
            }
        }
    }
    
	public static boolean hasPermission(Player player, String permission)
	{
		if (handler == null) {
			return player.isOp();
        }else{
        	return handler.has(player, permission);
        }
	}

}
>>>>>>> f2a74dba45cffa3cfa52db44f62e029351ddccac
