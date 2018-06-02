package com.github.risen619.TabCompleters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class PSetWarpCompleter implements TabCompleter
{
	@Override
	public List<String> onTabComplete(CommandSender s, Command c, String l, String[] args)
	{
		if(args.length != 2) return new ArrayList<String>();
		return new ArrayList<String>(Arrays.asList( new String[] { "private", "public" } ));
	}

}
