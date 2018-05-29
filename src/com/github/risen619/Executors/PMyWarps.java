package com.github.risen619.Executors;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.risen619.WarpsManager;
import com.github.risen619.Models.Warp;

public class PMyWarps implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender s, Command c, String l, String[] args)
	{
		Player p = (Player)s;
		WarpsManager wm = WarpsManager.getInstance();
		List<Warp> warps = wm.getMyWarps(p.getUniqueId().toString());
		if(warps == null)
			p.sendMessage("You don't have any warps");
		else
			p.sendMessage(warps.stream().map(w -> new String(w.getName())).toArray(String[]::new));
		return true;
	}

}