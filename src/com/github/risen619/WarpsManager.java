package com.github.risen619;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import com.github.risen619.Collections.PersonalWarpCollection;
import com.github.risen619.Collections.UserWarps;
import com.github.risen619.Collections.Users;
import com.github.risen619.Collections.Warps;
import com.github.risen619.Database.DatabaseManager;
import com.github.risen619.Models.ModelHelpers;
import com.github.risen619.Models.Teleporter;
import com.github.risen619.Models.User;
import com.github.risen619.Models.UserWarpModel;
import com.github.risen619.Models.Warp;

public class WarpsManager
{
	private DatabaseManager dm = null;
	private static WarpsManager instance = null;
	private static Server server = null;
	
	private HashMap<String, String> serverProperties;
	
	private Warps warps;
	private Users users;
	private UserWarps userWarps;
	
	private WarpsManager()
	{
		serverProperties = new HashMap<>();
		try
		{
			List<String> props = Files.readAllLines(Paths.get("server.properties"));
			for(String s : props)
			{
				if(s.matches(".*?=.+"))
				{
					String[] kv = s.split("=");
					serverProperties.put(kv[0], kv[1]);
				}
			}
		}
		catch (IOException e) { System.out.println("Cannot read server.properties file!"); }
	}
	
	public String getProperty(String name)
	{
		if(serverProperties.containsKey(name))
			return serverProperties.get(name);
		throw new IllegalArgumentException("Unknown propery \"" + name + "\"");
	}
	
	private void setup()
	{
		dm = new DatabaseManager("plugins/personalWarps.sqlite");

		dm.createTable(User.class);
		dm.createTable(Warp.class);
		dm.createTable(UserWarpModel.class);
		dm.createTable(Teleporter.class);
		
		PersonalWarpCollection.setDatabaseManager(dm);
		users = Users.getInstance();
		userWarps = UserWarps.getInstance();
		warps = Warps.getInstance();
		
		setupTeleporters();
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
	
	public void addUser(User u) { users.add(u); }
	public User getUserByID(int id) { return users.getByID(id); };
	public int getUserIdByUUID(String uuid) { return users.getByUUID(uuid).id(); }
	public List<User> getMembersForWarp(int id)
	{
		return userWarps.getMembersOfWarp(id).stream()
			.map(u -> users.getByID(u.user())).collect(Collectors.toList());
	};
	
	public static Server getServer() { return server; }
	public static void setServer(Server s) { server = s; }
	
	public boolean isWarpPublic(String name)
	{
		return getPublicWarps().stream().anyMatch(w -> w.name().equalsIgnoreCase(name) && w.isPublic());
	}
	public boolean warpExists(String name) { return warps.exists(name); }
	public void addWarp(Warp w) { warps.add(w); }
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
		return warp.owner().uuid().equals(uuid) || 
				warp.members().stream().anyMatch(m -> m.uuid().equals(uuid));
	}
	
	public List<Warp> getMyWarps(String uuid) { return warps.getByOwnerId(users.getByUUID(uuid).id()); }
	public List<Warp> getWarpsByMemberUUID(String uuid) { return warps.getByMemberUUID(uuid); }
	public Warp getWarpByName(String name) { return warps.getByName(name); }
	
	public void deleteWarpByName(String name)
	{
		Warp w = getWarpByName(name);
		warps.delete(w.id());
	}
	
	public void addMemberToWarp(String userUUID, String warpName)
	{
		int warpId = warps.getByName(warpName).id();
		userWarps.add(new UserWarpModel(
			users.getByUUID(userUUID).id(), warpId
		));
		warps.refresh(warpId);
		sendSuccess(server.getPlayer(UUID.fromString(userUUID)), "You have been granted access to warp " + warpName);
	}
	
	public void setupTeleporters()
	{
		String ids = "";
		List<Teleporter> tps = dm.select(Teleporter.class).stream().map(v -> (Teleporter)v).collect(Collectors.toList());
		
		for(Teleporter tp : tps)
		{
			Location l = ModelHelpers.deserializeLocation(tp.location());
			if(l.getBlock().getType() == Material.AIR || !warpExists(tp.metadata().split("/")[0]))
				ids += tp.id() + ",";
			else
				l.getBlock().setMetadata("warp", new FixedMetadataValue(Main.getPlugin(Main.class), tp.metadata()));
		}
		
		if(!ids.isEmpty())
		{
			ids = ids.substring(0,ids.length()-1);
			String query = "delete from Teleporters where Teleporters.id in ("+ids+");";
			dm.delete(query);
		}
	}
	
	public void addTeleporter(Location l, String metadata)
	{
		Teleporter t = new Teleporter(ModelHelpers.serializeLocation(l), metadata);
		dm.insert(t);
	}
	
	public void deleteTeleporter(Location l)
	{
		dm.delete(String.format("delete from Teleporters where Teleporters.location like \"%s\";",
			ModelHelpers.serializeLocation(l)));
	}
	
	public void teleport(Player p, Warp w)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				if(!w.location().getChunk().isLoaded())
					w.location().getChunk().load();
				
				try { Thread.sleep(500); }
				catch (InterruptedException e) { e.printStackTrace(); }
				finally { p.teleport(w.location()); }
			}
		}).start();
	}
	
}