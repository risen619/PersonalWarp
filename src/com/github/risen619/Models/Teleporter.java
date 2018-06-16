package com.github.risen619.Models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.github.risen619.Database.DatabaseCompatible;
import com.github.risen619.Database.PrimaryKey;
import com.github.risen619.Database.Table;
import com.github.risen619.Database.TableField;

@Table(tableName = "Teleporters")
public class Teleporter implements DatabaseCompatible
{
	@PrimaryKey()
	@TableField(name = "id", type = "integer")
	private int id;
	
	@TableField(name = "location", type = "text", unique = true)
	private String location;
	
	@TableField(name = "metadata", type = "text")
	private String metadata;
	
	public Teleporter() {}
	
	public Teleporter(String location, String metadata)
	{ this(-1, location, metadata); }
	
	public Teleporter(int id, String location, String metadata)
	{
		this.id = id;
		this.location = location;
		this.metadata = metadata;
	}
	
	public int id() { return id; }
	public String location() { return location; }
	public String metadata() { return metadata; }
	
	@Override
	public String toString()
	{
		return String.format("id=%d, location=%s, metadata=%s ;", id, location, metadata);
	}
	
	@Override
	public List<DatabaseCompatible> fromResultSet(ResultSet rs)
	{
		List<DatabaseCompatible> teleporters = new ArrayList<>();
		try
		{
			while(rs.next())
			{
				Integer id = rs.getInt("id");
				String location = rs.getString("location");
				String metadata = rs.getString("metadata");
				teleporters.add(new Teleporter(id, location, metadata));
			}
		}
		catch (SQLException e) { e.printStackTrace(); }
		return teleporters;
	}

	@Override
	public String insertIntoTableSQL()
	{
		return String.format("insert or replace into Teleporters(location, metadata) values(\"%s\", \"%s\");", 
			location, metadata);
	}

	@Override
	public String deleteFromTableSQL()
	{
		return String.format("delete from Teleporters where Teleporters.id=%d;", id);
	}

}
