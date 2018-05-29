package com.github.risen619.Models;

import java.util.List;

import org.bukkit.Location;

import com.github.risen619.WarpsManager;

public class Warp extends WarpModel
{
	private int id;
	private User owner;
	private Location location;
	private List<User> members = null;
	
	public Warp(WarpModel warp)
	{
		WarpsManager wm = WarpsManager.getInstance();
		id = warp.id;
		name = warp.name;
		owner = wm.getUserByID(warp.getOwnerId());
		isPublic = warp.isPublic;
		location = new Location(WarpsManager.getServer().getWorld(warp.getWorld()),
			warp.getX(), warp.getY(), warp.getZ());
		members = wm.getMembersForWarp(id);
	}
	
	public int getId() { return id; }
	public User getOwner() { return owner; }
	public Location getLocation() { return location; }
	public List<User> getMembers() { return members; }
	
	@Override
	public String toString()
	{
		return String.format("%d: %s - %s located in " + getLocation(), id, name, owner.getName());
	}
}