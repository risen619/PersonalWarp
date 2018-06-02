package com.github.risen619.Executors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.risen619.WarpsManager;

public class PDelWarp implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender s, Command c, String l, String[] args)
	{
		if(args.length < 1) return false;
		String warpName = args[0];
		WarpsManager wm = WarpsManager.getInstance();
		Player p = (Player)s;
		if(!wm.warpExists(warpName))
		{
			wm.sendError(p, "Warp with such name does not exist");
			return true;
		}
		wm.deleteWarpByName(warpName);
		wm.sendSuccess(p, String.format("Warp \"%s\" has been deleted!", warpName));
		return true;
	}

}
