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
		warps.addAll(wm.getWarpsByMemberUUID(p.getUniqueId().toString()));
		if(warps == null || warps.isEmpty())
			wm.sendError(p, "You don't have any warps");
		else
			wm.sendInformation(p,
				warps.stream().map(w -> String.format("%s, Owner: %s", w.name(), w.owner().name()))
				.toArray(String[]::new)
			);
		return true;
	}

}