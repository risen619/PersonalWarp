package com.github.risen619;

import java.util.List;

import org.bukkit.Server;

import com.github.risen619.Collections.PersonalWarpsCollections;
import com.github.risen619.Collections.Users;
import com.github.risen619.Collections.Warps;
import com.github.risen619.Database.DatabaseManager;

public class WarpsManager
{
	private DatabaseManager dm = null;
	private static WarpsManager instance = null;
	private static Server server = null;
	
	private Warps warps;
	private Users users;
	
	private WarpsManager()
	{
		dm = new DatabaseManager("plugins/personalWarps.sqlite");
		
		dm.createTable(Warp.createTableSQL());
		dm.createTable(User.createTableSQL());
		
		PersonalWarpsCollections.setDatabaseManager(dm);
		warps = Warps.getInstance();
		users = Users.getInstance();
	}
	
	public static WarpsManager getInstance()
	{
		if(instance == null)
			instance = new WarpsManager();
		return instance;
	}
	
	public void closeDb() { dm.closeConnection(); }
	
	public void addUser(User u) { users.add(u); }
	
	public int getUserIdByUUID(String uuid) { return users.getByUUID(uuid).getId(); }
	
	public static Server getServer() { return server; }
	public static void setServer(Server s) { server = s; }
	
	public boolean warpExists(String name) { return warps.exists(name); }
	public void addWarp(Warp w) { warps.add(w); }
	public List<Warp> getPublicWarps() { return warps.getPublic(); }
	
	public List<Warp> getMyWarps(String uuid) { return warps.getByOwnerId(users.getByUUID(uuid).getId()); }
}