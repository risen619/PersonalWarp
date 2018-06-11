package com.github.risen619.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.github.risen619.WarpsManager;
import com.github.risen619.Models.Warp;

public class SignClickListener implements Listener
{
	public SignClickListener() { }
	
	private void checkOwner(String accessibleBy, Warp warp, Player p)
	{
		WarpsManager wm = WarpsManager.getInstance();
		if(
			accessibleBy.equals("owner") && !warp.getOwner().getUUID().equals(p.getUniqueId().toString()) ||
			!wm.uuidCanUseWarp(p.getUniqueId().toString(), warp.getName())
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
		Bukkit.dispatchCommand(p, "pwarp " + warp.getName());
	}
	
	@EventHandler
	public void onSignClick(PlayerInteractEvent e)
	{
		Block b = e.getClickedBlock();
		if(b == null || b.getType() != Material.SIGN && b.getType() != Material.SIGN_POST &&
			e.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;

		Sign sign = (Sign)b.getState();
		
		if(!ChatColor.stripColor(sign.getLine(0)).equals("[WARP]")) return;
		
		WarpsManager wm = WarpsManager.getInstance();
		String warpName = ChatColor.stripColor(sign.getLine(1));
		String accessibleBy = ChatColor.stripColor(sign.getLine(3));
		
		if(!wm.warpExists(warpName)) return;
		
		Warp warp = wm.getWarpByName(warpName);
		checkOwner(accessibleBy, warp, e.getPlayer());
	}
}