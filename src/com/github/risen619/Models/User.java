package com.github.risen619.Models;

public class User extends UserModel
{	
	public User(UserModel user)
	{
		id = user.id;
		name = user.name;
		uuid = user.uuid;
	}
}