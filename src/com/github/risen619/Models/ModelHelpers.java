package com.github.risen619.Models;

import org.bukkit.Location;

import com.github.risen619.Main;

public class ModelHelpers
{
	public static String serializeLocation(Location l)
	{
		return String.format("%s;%f;%f;%f;%f;%f", l.getWorld().getName(), l.getX(), l.getY(), l.getZ(),
			l.getYaw(), l.getPitch());
	}
	
	public static Location deserializeLocation(String l)
	{
		if(l == null) return null;
		String world;
		double x, y, z;
		float yaw, pitch;
		String[] parts = l.split(";");
		world = parts[0];
		x = Double.parseDouble(parts[1]);
		y = Double.parseDouble(parts[2]);
		z = Double.parseDouble(parts[3]);
		yaw = Float.parseFloat(parts[4]);
		pitch = Float.parseFloat(parts[5]);
		return new Location(Main.getPlugin(Main.class).getServer().getWorld(world), x, y, z, yaw, pitch);
	}
}