package com.github.risen619.Collections;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.github.risen619.User;
import com.github.risen619.WarpsManager;
import com.github.risen619.Database.DatabaseCompatible;

public class Users extends PersonalWarpsCollections
{
	private static Users instance = null;
	
	private List<User> users = null;
	
	private Users() { fetchUsers(); }
	
	public static Users getInstance()
	{
		if(instance == null)
			instance = new Users();
		return instance;
	}
	
	public List<User> getUsers() { return users; }
	
	private void fetchUsers()
	{
		List<DatabaseCompatible> dcs = dm.select(User.selectFromTableSQL(), rs -> User.fromResultSet(rs));
		users = dcs.stream().map(dc -> (User)dc).collect(Collectors.toList());
	}
	
	public User getByUUID(String uuid)
	{
		if(users == null)
			fetchUsers();
		List<User> us = users.stream().filter(u -> u.getUUID().equals(uuid)).collect(Collectors.toList());
		if(us == null || us.size() == 0)
		{
			User user = new User(WarpsManager.getServer().getPlayer(UUID.fromString(uuid)).getName(), uuid);
			dm.insert(user);
			return (User)dm.select(
				String.format("select * from Users where Users.uuid=\"%s\"", uuid),
				rs -> User.fromResultSet(rs)
			).get(0);
		}
		else return us.get(0);
	}
	
	public void add(User u)
	{
		if(u == null) return;
		dm.insert(u);
		users.add(
			(User)dm.select(
				String.format("select * from Users where Users.name=\"%s\"", u.getName()), 
				rs -> User.fromResultSet(rs)
			)
			.get(0)
		);
	}
}