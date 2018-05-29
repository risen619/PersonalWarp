package com.github.risen619.Models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.github.risen619.Database.DatabaseCompatible;

public class WarpModel implements DatabaseCompatible
{
	protected int id;
	protected String name;
	private int owner;
	protected boolean isPublic;
	private String world;
	private int x;
	private int y;
	private int z;
	
	protected WarpModel() {}
	
	public WarpModel(String name, int owner, boolean isPublic, String world, int x, int y, int z)
	{ this(-1, name, owner, isPublic, world, x, y, z); }

	private WarpModel(int id, String name, int owner, boolean isPublic, String world, int x, int y, int z)
	{
		this.id = id;
		this.name = name;
		this.owner = owner;
		this.isPublic = isPublic;
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public int getId() { return id; }
	public String getName() { return name; }
	public boolean getIsPublic() { return isPublic; }
	
	protected int getOwnerId() { return owner; }
	protected int getX() { return x; }
	protected int getY() { return y; }
	protected int getZ() { return z; }
	protected String getWorld() { return world; }
	
	@Override
	public String toString()
	{
		return String.format("%s: %s (%s) located at %d;%d;%d", owner, name,
			(isPublic ? "public" : "private"), x, y, z);
	}
	
	public static String createTableSQL() 
	{
		return "create table if not exists Warps (" + 
				" id integer primary key autoincrement," + 
				" name text not null," + 
				" owner integer not null," + 
				" isPublic boolean not null," + 
				" world text not null," +
				" x integer not null," + 
				" y integer not null," + 
				" z integer not null" + 
		"); create unique index if not exists Warps_Name on Warps(name);";
	}

	public static String dropTableSQL()
	{ return "drop table if exists Warps;"; }
	
	public static String truncateTableSQL()
	{ return "delete from Warps;"; }
	
	public static String selectFromTableSQL()
	{ return "select * from Warps;"; }
	
	public static List<DatabaseCompatible> fromResultSet(ResultSet rs)
	{
		List<DatabaseCompatible> warps = new ArrayList<DatabaseCompatible>();
		try
		{
			while(rs.next())
			{
				Integer id = rs.getInt("id");
				String name = rs.getString("name");
				Integer owner = rs.getInt("owner");
				Boolean isPublic = rs.getBoolean("isPublic");
				String world = rs.getString("world");
				Integer x = rs.getInt("x");
				Integer y = rs.getInt("y");
				Integer z = rs.getInt("z");
				warps.add(new WarpModel(id, name, owner, isPublic, world, x, y, z));
			}
		}
		catch (SQLException e) { e.printStackTrace(); }
		return warps;
	}
	
	@Override
	public String insertIntoTableSQL()
	{
		return String.format("insert or ignore into Warps " + 
				"(name, owner, isPublic, world, x,y,z) " + 
				"values (\"%s\", %d, %d, \"%s\", %d, %d, %d);",
				name, owner, isPublic ? 1 : 0, world, x, y, z);
	}

	@Override
	public String deleteFromTableSQL() { return "delete from Warps where id=" + id + ";"; }
}