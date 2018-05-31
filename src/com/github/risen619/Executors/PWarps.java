package com.github.risen619.Executors;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.risen619.WarpsManager;
import com.github.risen619.Models.Warp;

public class PWarps implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender s, Command c, String l, String[] args)
	{
		WarpsManager wm = WarpsManager.getInstance();
		List<Warp> warps = wm.getPublicWarps();
		wm.sendInformation((Player)s,
			warps.stream().map(w -> new String(w.getName())).toArray(String[]::new)
		);
		return true;
	}

}
