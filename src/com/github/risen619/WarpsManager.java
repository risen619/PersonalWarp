package com.github.risen619;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import com.github.risen619.Collections.PersonalWarpsCollections;
import com.github.risen619.Collections.UserWarps;
import com.github.risen619.Collections.Users;
import com.github.risen619.Collections.Warps;
import com.github.risen619.Database.DatabaseManager;
import com.github.risen619.Models.User;
import com.github.risen619.Models.UserModel;
import com.github.risen619.Models.UserWarpModel;
import com.github.risen619.Models.Warp;
import com.github.risen619.Models.WarpModel;

public class WarpsManager
{
	private DatabaseManager dm = null;
	private static WarpsManager instance = null;
	private static Server server = null;
	
	private Warps warps;
	private Users users;
	private UserWarps userWarps;
	
	private WarpsManager()
	{
		System.out.println("WM GET INSTANCE");
	}
	
	private void setup()
	{
		dm = new DatabaseManager("plugins/personalWarps.sqlite");
		
		dm.createTable(WarpModel.createTableSQL());
		dm.createTable(UserModel.createTableSQL());
		dm.createTable(UserWarpModel.createTableSQL());
		
		PersonalWarpsCollections.setDatabaseManager(dm);
		users = Users.getInstance();
		userWarps = UserWarps.getInstance();
		warps = Warps.getInstance();
	}
	
	synchronized public static WarpsManager getInstance()
	{
		if(instance == null)
		{
			instance = new WarpsManager();
			instance.setup();
		}
		return instance;
	}
	
	public void closeDb() { dm.closeConnection(); }
	
	private void sendColorfulMessage(Player p, String message, ChatColor color) { p.sendMessage(color + message); }
	private void sendColorfulMessage(Player p, String[] message, ChatColor color)
	{
		for(int i=0; i<message.length; i++)
			message[i] = color + message[i];
		p.sendMessage(message);
	}
	
	public void sendError(Player p, String message) { sendColorfulMessage(p, message, ChatColor.RED); }
	public void sendError(Player p, String[] message) { sendColorfulMessage(p, message, ChatColor.RED); }
	
	public void sendWarning(Player p, String message) { sendColorfulMessage(p, message, ChatColor.GOLD); }
	public void sendWarning(Player p, String[] message) { sendColorfulMessage(p, message, ChatColor.GOLD); }
	
	public void sendSuccess(Player p, String message) { sendColorfulMessage(p, message, ChatColor.GREEN); }
	public void sendSuccess(Player p, String[] message) { sendColorfulMessage(p, message, ChatColor.GREEN); }

	public void sendInformatoin(Player p, String message) { sendColorfulMessage(p, message, ChatColor.WHITE); }
	public void sendInformation(Player p, String[] message) { sendColorfulMessage(p, message, ChatColor.WHITE); }
	
	public void addUser(UserModel u) { users.add(u); }
	public User getUserByID(int id) { return new User(users.getByID(id)); };
	public int getUserIdByUUID(String uuid) { return users.getByUUID(uuid).getId(); }
	public List<User> getMembersForWarp(int id)
	{
		return userWarps.getMembersOfWarp(id).stream()
			.map(u -> users.getByID(u.getUser())).collect(Collectors.toList());
	};
	
	public static Server getServer() { return server; }
	public static void setServer(Server s) { server = s; }
	
	public boolean isWarpPublic(String name)
	{
		return getPublicWarps().stream().anyMatch(w -> w.getName().equalsIgnoreCase(name) && w.getIsPublic());
	}
	public boolean warpExists(String name) { return warps.exists(name); }
	public void addWarp(WarpModel w) { warps.add(w); }
	public List<Warp> getPublicWarps()
	{
		return warps.getPublic();
	}
	public boolean uuidCanUseWarp(String uuid, String warpName) throws NullPointerException
	{
		if(isWarpPublic(warpName)) return true;
		Warp warp = warps.getByName(warpName);
		if(warp == null)
			throw new NullPointerException("Warp with such name does not exist");
		return warp.getOwner().getUUID().equals(uuid) || 
				warp.getMembers().stream().anyMatch(m -> m.getUUID().equals(uuid));
	}
	
	public List<Warp> getMyWarps(String uuid) { return warps.getByOwnerId(users.getByUUID(uuid).getId()); }
	public List<Warp> getWarpsByMemberUUID(String uuid) { return warps.getByMemberUUID(uuid); }
	public Warp getWarpByName(String name) { return warps.getByName(name); }
	
	public void addMemberToWarp(String userUUID, String warpName)
	{
		int warpId = warps.getByName(warpName).getId();
		userWarps.add(new UserWarpModel(
			users.getByUUID(userUUID).getId(), warpId
		));
		warps.refresh(warpId);
		sendSuccess(server.getPlayer(UUID.fromString(userUUID)), "You have been granted access to warp " + warpName);
	}
}