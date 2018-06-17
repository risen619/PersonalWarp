package com.github.risen619.Executors;

import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.risen619.WarpsManager;

public class PRandom implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender s, Command c, String l, String[] args)
	{
		if(args.length > 0) return false;
		WarpsManager wm = WarpsManager.getInstance();
		Player p = (Player)s;
		Block b;
		
//		int max = Integer.parseInt(((DedicatedServer)MinecraftServer.getServer())
//				.getPropertyManager().properties.getProperty("max-world-size"));
		try
		{
			int max = Integer.parseInt(wm.getProperty("max-world-size"));
			int rx = (int)(Math.random() * 1000000) % max;
			int rz = (int)(Math.random() * 1000000) % max;
			b = p.getWorld().getHighestBlockAt(rx, rz);

			p.teleport(b.getLocation());
		}
		catch(IllegalArgumentException e) { wm.sendError(p, "Something went wrong"); return true; }
		
		return true;
	}

}
