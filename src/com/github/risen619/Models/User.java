package com.github.risen619.Models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.github.risen619.Database.DatabaseCompatible;
import com.github.risen619.Database.PrimaryKey;
import com.github.risen619.Database.Table;
import com.github.risen619.Database.TableField;

@Table(tableName = "Users")
public class User implements DatabaseCompatible
{
	@PrimaryKey
	@TableField(name = "id", type = "integer")
	protected int id;
	
	@TableField(name = "name", type = "text", unique = true)
	protected String name;
	
	@TableField(name = "uuid", type = "text", unique = true)
	protected String uuid;

	public User() { }
	
	public User(String name, String uuid) { this(-1, name, uuid); }

	private User(int id, String name, String uuid)
	{
		this.id = id;
		this.name = name;
		this.uuid = uuid;
	}
	
	public int id() { return id; }
	public String uuid() { return uuid; }
	public String name() { return name; }

	@Override
	public String toString() { return String.format("%d: %s - %s", id, name, uuid); }
	
	public List<DatabaseCompatible> fromResultSet(ResultSet rs)
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
