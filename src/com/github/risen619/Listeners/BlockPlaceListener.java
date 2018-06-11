package com.github.risen619.Listeners;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import com.github.risen619.Main;

public class BlockPlaceListener implements Listener
{
	public BlockPlaceListener() {}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e)
	{
		ItemStack inHand = e.getPlayer().getInventory().getItemInMainHand();
		ItemMeta meta = inHand.hasItemMeta() ? inHand.getItemMeta() : null;
		if(meta == null) return;
		
		Block placed = e.getBlockPlaced();
		placed.setMetadata("warp", new FixedMetadataValue(Main.getPlugin(Main.class), meta.getLore()));
	}
}