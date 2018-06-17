package com.github.risen619.TabCompleters;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.github.risen619.WarpsManager;
import com.github.risen619.Models.Warp;

public class PWarpCompeleter implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender s, Command c, String l, String[] args)
	{
		if(args.length > 2) return new ArrayList<String>();
		if(args.length == 2) return null;
		
		Player p = (Player)s;
		WarpsManager wm = WarpsManager.getInstance();
		
		List<Warp> warps = wm.getMyWarps(p.getUniqueId().toString());
		warps.addAll(wm.getWarpsByMemberUUID(p.getUniqueId().toString()));
		
		return warps.stream().map(v -> v.name())
		.filter(v -> args[0].isEmpty() || v.toLowerCase().startsWith(args[0].toLowerCase()))
		.distinct().sorted().collect(Collectors.toList());
	}

}
