package com.github.risen619.TabCompleters;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.github.risen619.WarpsManager;

public class PWarpCompeleter implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender s, Command c, String l, String[] args)
	{
		if(args.length > 1) return new ArrayList<String>();
		
		Player p = (Player)s;
		WarpsManager wm = WarpsManager.getInstance();
		
		List<String> warps = wm.getMyWarps(p.getUniqueId().toString()).stream()
		.map(v -> v.getName()).collect(Collectors.toList());
		warps.addAll(wm.getPublicWarps().stream().map(v -> v.getName()).collect(Collectors.toList()));
		warps = warps.stream().distinct().sorted().collect(Collectors.toList());
		
		return warps;
	}

}
