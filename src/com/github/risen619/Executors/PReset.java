package com.github.risen619.Executors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.risen619.WarpsManager;
import com.github.risen619.Models.Warp;

public class PReset implements CommandExecutor
{
	@Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] args)
    {
		WarpsManager wm = WarpsManager.getInstance();
		Player p = (Player)s;
		if(args.length == 0) return false;
		
		String name = args[0];
		String visibility = "";
		
		if(args.length > 1)
		{
			visibility = args[1];
			if(!visibility.equalsIgnoreCase("private") && !visibility.equalsIgnoreCase("public")) return false;
		}
        else visibility = "private";
        Warp w = new Warp(
            name,
            wm.getUserIdByUUID(p.getUniqueId().toString()),
            (visibility.equalsIgnoreCase("private") ? false : true), p.getLocation()
        );
        wm.addWarp(w);

        wm.sendSuccess(p, String.format("Warp %s (%s) reset!", w.name(), visibility));
        
        return true;
	}

}
