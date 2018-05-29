package com.github.risen619.Executors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.risen619.WarpsManager;

public class PAddMember implements CommandExecutor
{

	private String getPlayerUUID(String playerName)
	{
		for(Player pl : WarpsManager.getServer().getOnlinePlayers())
			if(pl.getName().equalsIgnoreCase(playerName))
				return pl.getUniqueId().toString();
		return "";
	}
	
	private boolean playerValid(String name)
	{
		for(Player pl : WarpsManager.getServer().getOnlinePlayers())
			if(pl.getName().equalsIgnoreCase(name))
				return true;
		return false;
	}
	
	private boolean warpValid(String name)
	{
		WarpsManager wm = WarpsManager.getInstance();
		return wm.warpExists(name);
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command c, String l, String[] args)
	{
		if(args.length < 2) return false;
		WarpsManager wm = WarpsManager.getInstance();
		Player p = (Player)s;
		
		String warpName = args[0];
		String playerName = args[1];
		
		if(playerName.equalsIgnoreCase(p.getName()))
		{
			p.sendMessage("You cannot add yourself as a member");
			return true;
		}
		
		if(!warpValid(warpName))
		{
			p.sendMessage("Warp with such name does not exists");
			return true;
		}
		
		if(wm.getWarpByName(warpName).getIsPublic())
		{
			p.sendMessage("You cannot add members to public warp");
			return true;
		}
		
		if(!playerValid(playerName))
		{
			p.sendMessage("Player with such name is not online");
			return true;
		}
		
		wm.addMemberToWarp(getPlayerUUID(playerName), warpName);
		
		return true;
	}

}
