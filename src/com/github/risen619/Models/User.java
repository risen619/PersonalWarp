package com.github.risen619.Models;

@Deprecated
public class User extends UserModel
{	
	public User(UserModel user)
	{
		id = user.id;
		name = user.name;
		uuid = user.uuid;
	}
	
	public int getId() { return id; }
	public String getName() { return name; }
	public String getUUID() { return uuid; }
	
	@Override
	public String toString()
	{
		return String.format("%d: %s - %s", id, name, uuid);
	}
}