package com.github.risen619.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.github.risen619.WarpsManager;
import com.github.risen619.Models.Warp;

public class TeleporterClickListenerer implements Listener
{
	public TeleporterClickListenerer() { }
	
	private void checkOwner(String accessibleBy, Warp warp, Player p)
	{
		WarpsManager wm = WarpsManager.getInstance();
		if(
			accessibleBy.equals("owner") && !warp.owner().uuid().equals(p.getUniqueId().toString()) ||
			!wm.uuidCanUseWarp(p.getUniqueId().toString(), warp.name())
		)
		{
			wm.sendError(p, "You don't have access to this warp!");
			return;
		}
		checkAll(accessibleBy, warp, p);
	}
	
	private void checkAll(String accessibleBy, Warp warp, Player p)
	{
		if(accessibleBy.equals("all"))
		{
			WarpsManager.getInstance().teleport(p, warp);
			return;
		}
		checkMembers(accessibleBy, warp, p);
	}
	
	private void checkMembers(String accessibleBy, Warp warp, Player p)
	{
		Bukkit.dispatchCommand(p, "pwarp " + warp.name());
	}
	
	@EventHandler
	public void onSignClick(PlayerInteractEvent e)
	{
		if(e == null || e.getClickedBlock() == null) return;
		if(e.getAction() != Action.RIGHT_CLICK_BLOCK || e.getPlayer().isSneaking()) return;
		
		Block b = e.getClickedBlock();
		
		if(!b.hasMetadata("warp")) return;

		WarpsManager wm = WarpsManager.getInstance();
		
		String metadata = b.getMetadata("warp").get(0).asString();
		metadata = metadata.replaceAll("[\\[\\]]", "");
		String warpName = metadata.split("/")[0];
		String accessibleBy = metadata.split("/")[1];
		
		if(!wm.warpExists(warpName)) return;
		
		Warp warp = wm.getWarpByName(warpName);
		checkOwner(accessibleBy, warp, e.getPlayer());
	}
}