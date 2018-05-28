package com.github.risen619;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.github.risen619.Database.DatabaseCompatible;

public class User implements DatabaseCompatible
{
	private int id;
	private String name;
	private String uuid;
	
	public User(String name, String uuid) { this(-1, name, uuid); }

	private User(int id, String name, String uuid)
	{
		this.id = id;
		this.name = name;
		this.uuid = uuid;
	}
	
	public int getId() { return id; }
	public String getUUID() { return uuid; }
	public String getName() { return name; }
	
	@Override
	public String toString()
	{
		return String.format("%s | %s", uuid, name);
	}
	
	public static String createTableSQL() 
	{
		return "create table if not exists Users (" + 
				" id integer primary key autoincrement," + 
				" name text not null," + 
				" uuid text not null" + 
		"); create unique index if not exists Users_Name on Users(name, uuid);";
	}

	public static String dropTableSQL()
	{ return "drop table if exists Users;"; }
	
	public static String truncateTableSQL()
	{ return "delete from Users;"; }
	
	public static String selectFromTableSQL()
	{ return "select * from Users;"; }
	
	public static List<DatabaseCompatible> fromResultSet(ResultSet rs)
	{
		List<DatabaseCompatible> users = new ArrayList<>();
		try
		{
			while(rs.next())
			{
				Integer id = rs.getInt("id");
				String name = rs.getString("name");
				String uuid = rs.getString("uuid");
				users.add(new User(id, name, uuid));
			}
		}
		catch (SQLException e) { e.printStackTrace(); }
		return users;
	}
	
	@Override
	public String insertIntoTableSQL()
	{
		return String.format("insert or ignore into Users " + 
				"(name, uuid) " + 
				"values (\"%s\", \"%s\");",
				name, uuid);
	}

	public static String getByUUIDSQL(String uuid)
	{
		return String.format("select * from Users where Users.uuid=\"%s\"", uuid);
	}
	
	@Override
	public String deleteFromTableSQL() { return "delete from Users where id=" + id + ";"; }
}
