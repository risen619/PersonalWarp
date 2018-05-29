package com.github.risen619.Collections;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.github.risen619.WarpsManager;
import com.github.risen619.Database.DatabaseCompatible;
import com.github.risen619.Models.User;
import com.github.risen619.Models.UserModel;

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
		List<DatabaseCompatible> dcs = dm.select(UserModel.selectFromTableSQL(), rs -> UserModel.fromResultSet(rs));
		users = dcs.stream().map(dc -> new User((UserModel)dc)).collect(Collectors.toList());
	}
	
	public User getByUUID(String uuid)
	{
		if(users == null)
			fetchUsers();
		List<User> us = users.stream().filter(u -> u.getUUID().equals(uuid)).collect(Collectors.toList());
		if(us == null || us.size() == 0)
		{
			UserModel user = new UserModel(WarpsManager.getServer().getPlayer(UUID.fromString(uuid)).getName(), uuid);
			dm.insert(user);
			return new User(
				(UserModel)dm.select(
					String.format("select * from Users where Users.uuid=\"%s\"", uuid),
					rs -> UserModel.fromResultSet(rs)
				).get(0)
			);
		}
		else return us.get(0);
	}
	
	public User getByID(int id)
	{
		if(users == null)
			fetchUsers();
		return users.stream().filter(u -> u.getId() == id).limit(1).collect(Collectors.toList()).get(0);
	}
	
	public User getByName(String name)
	{
		if(users == null)
			fetchUsers();
		return users.stream().filter(u -> u.getName().equalsIgnoreCase(name)).limit(1).collect(Collectors.toList()).get(0);
	}
	
	public void add(UserModel u)
	{
		if(u == null) return;
		dm.insert(u);
		users.add(new User(
			(UserModel)dm.select(
				String.format("select * from Users where Users.name=\"%s\"", u.getName()), 
				rs -> UserModel.fromResultSet(rs)
			)
			.get(0)
		));
	}
}