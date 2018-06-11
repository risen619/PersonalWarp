package com.github.risen619.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BlockPlaceListener implements Listener
{
	public BlockPlaceListener() {}
	
	@EventHandler
	public void onSignChange(SignChangeEvent e)
	{
		ItemStack inHand = e.getPlayer().getInventory().getItemInMainHand();
		ItemMeta meta = inHand.hasItemMeta() ? inHand.getItemMeta() : null;
		if(inHand.getType().equals(Material.SIGN) && meta != null && meta.hasLore() &&
			meta.getLore().get(0).matches("^\\w+\\/(owner|members|all)$"))
		{
			String lore = meta.getLore().get(0);
			String warpName = lore.split("/", 2)[0];
			String accessibleBy = lore.split("/", 2)[1];
			
			e.setLine(0, ChatColor.BOLD + "[WARP]");
			e.setLine(1, ChatColor.BOLD + warpName);
			e.setLine(2, ChatColor.BOLD + "Accessible by: ");
			e.setLine(3, ChatColor.ITALIC + accessibleBy);
		}
	}
}