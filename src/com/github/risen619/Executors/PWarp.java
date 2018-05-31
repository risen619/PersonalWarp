package com.github.risen619.Executors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.risen619.WarpsManager;
import com.github.risen619.Models.Warp;

public class PWarp implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender s, Command c, String l, String[] args)
	{
		if(args.length == 0) return false;
		WarpsManager wm = WarpsManager.getInstance();
		Player p = (Player)s;
		try
		{
			if(wm.uuidCanUseWarp(p.getUniqueId().toString(), args[0]))
			{
				Warp w = wm.getWarpByName(args[0]);
				/* First take World from Warp object, then check for highest block 
				 * in warp location, then take block's location
				 * */
				p.teleport(w.getLocation().getWorld().getHighestBlockAt(w.getLocation()).getLocation());
				wm.sendSuccess(p, "I will teleport you later...");
			}
			else wm.sendError(p, "You don't have access to this warp");
		}
		catch(NullPointerException e) { wm.sendError(p, e.getMessage()); }
		return true;
	}

}
