package com.github.risen619.Collections;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.github.risen619.WarpsManager;
import com.github.risen619.Database.DatabaseCompatible;
import com.github.risen619.Models.User;

public class Users extends PersonalWarpsCollections
{
	private static Users instance = null;
	
	private List<User> users = null;
	
	private Users() { fetchUsers(); }
	
	synchronized public static Users getInstance()
	{
		if(instance == null)
			instance = new Users();
		return instance;
	}
	
	public List<User> getUsers() { return users; }
	
	private void fetchUsers()
	{
		List<DatabaseCompatible> dcs = dm.select(User.class);
		users = dcs.stream().map(dc -> (User)dc).collect(Collectors.toList());
	}
	
	public void refresh() { fetchUsers(); }
	public void refresh(int id)
	{
		users.removeIf(w -> w.id() == id);
		List<DatabaseCompatible> dcs = dm.selectBy(User.class, String.format("Users.id=%d;", id));
		users.addAll(dcs.stream().map(dc -> (User)dc).collect(Collectors.toList()));
	}
	
	public User getByUUID(String uuid)
	{
		if(users == null)
			fetchUsers();
		
		List<User> us = users.stream().filter(u -> u.uuid().equals(uuid)).collect(Collectors.toList());
		if(us == null || us.size() == 0)
		{
			User user = new User(WarpsManager.getServer().getPlayer(UUID.fromString(uuid)).getName(), uuid);
			dm.insert(user);
			return (User)dm.selectBy(User.class, String.format("Users.uuid=\"%s\";", uuid)).get(0);
		}
		else return us.get(0);
	}
	
	public User getByID(int id)
	{
		if(users == null)
			fetchUsers();
		return users.stream().filter(u -> u.id() == id).limit(1).collect(Collectors.toList()).get(0);
	}
	
	public User getByName(String name)
	{
		if(users == null)
			fetchUsers();
		return users.stream().filter(u -> u.name().equalsIgnoreCase(name)).limit(1).collect(Collectors.toList()).get(0);
	}
	
	public void add(User u)
	{
		if(u == null) return;
		dm.insert(u);
		fetchUsers();
	}
}