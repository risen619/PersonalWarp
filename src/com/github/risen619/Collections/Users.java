package com.github.risen619.Collections;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.github.risen619.WarpsManager;
import com.github.risen619.Models.User;

public class Users extends PersonalWarpCollection
{
	private static Users instance = null;

	private HashMap<Integer, User> map;
	
	private Users()
	{
		map = new HashMap<>();
		fetchUsers();
	}
	
	synchronized public static Users getInstance()
	{
		if(instance == null)
			instance = new Users();
		return instance;
	}
	
	@Override
	public Collection<User> get() { return map.values(); }
	
	private void fetchUsers()
	{
		List<User> us = dm.select(User.class);
		for(User u : us)
			map.put(u.id(), u);
	}
	
	public void refresh() { fetchUsers(); }
	public void refresh(int id)
	{
		map.remove(id);
		List<User> us = dm.selectBy(User.class, String.format("Users.id=%d;", id));
		for(User u : us)
			map.put(u.id(), u);
	}
	
	public User getByUUID(String uuid)
	{	
		List<User> us = map.values().stream().filter(u -> u.uuid().equals(uuid)).collect(Collectors.toList());
		if(us == null || us.size() == 0)
		{
			User user = new User(WarpsManager.getServer().getPlayer(UUID.fromString(uuid)).getName(), uuid);
			dm.insert(user);
			return dm.selectBy(User.class, String.format("Users.uuid=\"%s\";", uuid)).get(0);
		}
		else return us.get(0);
	}
	
	public User getByID(int id)
	{
		return map.values().stream().filter(u -> u.id() == id).limit(1).collect(Collectors.toList()).get(0);
	}
	
	public User getByName(String name)
	{
		return map.values().stream().filter(u -> u.name().equalsIgnoreCase(name))
			.limit(1).collect(Collectors.toList()).get(0);
	}
	
	public void add(User u)
	{
		if(u == null) return;
		dm.insert(u);
		fetchUsers();
	}
}