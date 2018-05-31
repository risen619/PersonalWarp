package com.github.risen619.Executors;

import com.github.risen619.WarpsManager;
import com.github.risen619.Models.WarpModel;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PSetWarp implements CommandExecutor
{
	@Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] args)
    {
		WarpsManager wm = WarpsManager.getInstance();
		Player p = (Player)s;
		if(args.length == 0) return false;
		
		String name = args[0];
		if(wm.warpExists(name))
		{
			wm.sendError(p, "Warp with such name already exists");
			return true;
		}
		String visibility = "";
		
		if(args.length > 1)
		{
			visibility = args[1];
			if(!visibility.equalsIgnoreCase("private") && !visibility.equalsIgnoreCase("public")) return false;
		}
        else visibility = "private";
        WarpModel w = new WarpModel(
            name,
            wm.getUserIdByUUID(p.getUniqueId().toString()),
            (visibility.equalsIgnoreCase("private") ? false : true),
            p.getWorld().getName(),
            p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ()
        );
        wm.addWarp(w);

        wm.sendSuccess(p, String.format("Warp %s (%s) created!", w.getName(), visibility));
        
        return true;
	}

}