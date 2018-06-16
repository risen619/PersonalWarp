package com.github.risen619.Listeners;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.github.risen619.WarpsManager;

public class DestroyTeleporterListener implements Listener
{
	public DestroyTeleporterListener() { }
	
	@EventHandler
	public void onBlockDestroy(BlockBreakEvent e)
	{
		if(e.getBlock() == null) return;
		Block b = e.getBlock();
		if(b.hasMetadata("warp"))
		{
			WarpsManager wm = WarpsManager.getInstance();
			String meta = b.getMetadata("warp").get(0).asString();
			wm.deleteTeleporter(b.getLocation());
			wm.sendSuccess(e.getPlayer(), String.format("Teleporter to %s destroyed!", meta.split("/")[0]));
		}
	}
}