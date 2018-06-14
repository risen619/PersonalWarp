package com.github.risen619.Executors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.risen619.WarpsManager;
import com.github.risen619.Models.Warp;

public class PTool implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender s, Command c, String l, String[] args)
	{
		if(args.length < 1 || args.length > 3) return false;
		
		WarpsManager wm = WarpsManager.getInstance();
		Player p = (Player)s;
		String accessibleBy = "owner";
		Material material = Material.SIGN;
		
		if(!wm.warpExists(args[0]))
		{
			wm.sendError(p, "Warp with such name does not exist!");
			return true;
		}
		
		if(args.length > 1)
		{
			args[1] = args[1].toLowerCase();
			if(!args[1].equals("owner") && !args[1].equals("members") && !args[1].equals("all"))
				return false;
			accessibleBy = args[1];
		}
		Warp warp = wm.getWarpByName(args[0]);
		if(!p.getUniqueId().toString().equals(warp.owner().uuid()))
		{
			wm.sendError(p, "You don't have access to this warp!");
			return true;
		}
		
		if(args.length > 2)
		{
			try
			{
				material = Arrays.asList(Material.values()).stream()
					.filter(v -> v.name().equalsIgnoreCase(args[2])).limit(1).collect(Collectors.toList()).get(0);
			}
			catch(IndexOutOfBoundsException|NullPointerException e) { return false; }
		}
		
		ItemStack is = new ItemStack(material);
		
		List<String> lore = new ArrayList<String>();
		lore.add(args[0] + "/" + accessibleBy);
		ItemMeta im = is.getItemMeta();
		im.setLore(lore);
		im.setDisplayName("[WARP]");
		
		is.setItemMeta(im);
		p.getInventory().addItem(is);
		
		return true;
	}

}
