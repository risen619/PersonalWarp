package com.github.risen619.TabCompleters;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.github.risen619.WarpsManager;

public class PAddMemberCompleter implements TabCompleter
{

	@Override
	public List<String> onTabComplete(CommandSender s, Command c, String l, String[] args)
	{
		if(args.length != 1) return null;
		
		WarpsManager wm = WarpsManager.getInstance();
		Player p = (Player)s;
		
		List<String> warps = new ArrayList<String>();
		
		if(args.length == 1)
			warps = wm.getMyWarps(p.getUniqueId().toString())
			.stream().map(v -> v.name()).sorted().collect(Collectors.toList());
		
		return warps;
	}

}
