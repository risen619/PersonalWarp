package com.github.risen619.TabCompleters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.github.risen619.WarpsManager;

public class PToolCompleter implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender s, Command c, String argl, String[] args)
	{
		WarpsManager wm = WarpsManager.getInstance();
		Player p = (Player)s;
		if(args.length == 1)
			return wm.getMyWarps(p.getUniqueId().toString()).stream().map(v -> v.getName()).collect(Collectors.toList());
		if(args.length == 2)
			return new ArrayList<String>(Arrays.asList(new String[] { "owner", "members", "all" }));
		
		return new ArrayList<String>();
	}

}
