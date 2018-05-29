package com.github.risen619.Executors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.risen619.WarpsManager;

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
			if(wm.uuidCanUseWarp(p.getUniqueId().toString(), args[1]))
				p.sendMessage("I will teleport you later...");
			p.sendMessage("You don't have access to this warp");
		}
		catch(NullPointerException e) { p.sendMessage(e.getMessage()); }
		return true;
	}

}
