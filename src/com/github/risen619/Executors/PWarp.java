package com.github.risen619.Executors;

import java.util.List;
import java.util.stream.Collectors;

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
		if(args.length == 0 || args.length > 2) return false;
		WarpsManager wm = WarpsManager.getInstance();
		Player p = (Player)s;
		
		if(args.length > 1)
		{
			List<Player> players = WarpsManager.getServer().getOnlinePlayers().stream()
				.filter(pl -> pl.getName().equalsIgnoreCase(args[1])).limit(1).collect(Collectors.toList());
			if(!players.isEmpty())
				p = players.get(0);
			else
			{
				wm.sendError(p, "This player is not online!");
				return true;
			}
		}
		
		try
		{
			Warp w = wm.getWarpByName(args[0]);
			if(args.length == 1)
			{
				if(wm.uuidCanUseWarp(p.getUniqueId().toString(), args[0]))
					wm.teleport(p, w);
				else wm.sendError(p, "You don't have access to this warp");
			}
			else wm.teleport(p, w);
		}
		catch(NullPointerException e) { wm.sendError(p, e.getMessage()); }
		return true;
	}

}
