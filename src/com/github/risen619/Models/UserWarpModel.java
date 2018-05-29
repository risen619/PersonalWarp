package com.github.risen619.Models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.github.risen619.Database.DatabaseCompatible;

public class UserWarpModel implements DatabaseCompatible
{
	private int id;
	private int user;
	private int warp;
	
	public UserWarpModel(int user, int warp) { this(-1, user, warp); }
	public UserWarpModel(int id, int user, int warp)
	{
		this.id = id;
		this.user = user;
		this.warp = warp;
	}
	
	public int getId() { return id; }
	public int getUser() { return user; }
	public int getWarp() { return warp; }

	public static String createTableSQL() 
	{
		return "create table if not exists UserWarps (" + 
				" id integer primary key autoincrement," + 
				" user integer not null," + 
				" warp integer not null" + 
		"); create unique index if not exists UserWarps_UserWarp on UserWarps(user, warp);";
	}

	public static String dropTableSQL()
	{ return "drop table if exists UserWarps;"; }
	
	public static String truncateTableSQL()
	{ return "delete from UserWarps;"; }
	
	public static String selectFromTableSQL()
	{ return "select * from UserWarps;"; }
	
	public static List<DatabaseCompatible> fromResultSet(ResultSet rs)
	{
		List<DatabaseCompatible> recs = new ArrayList<>();
		try
		{
			while(rs.next())
			{
				Integer id = rs.getInt("id");
				Integer user = rs.getInt("user");
				Integer warp = rs.getInt("warp");
				recs.add(new UserWarpModel(id, user, warp));
			}
		}
		catch (SQLException e) { e.printStackTrace(); }
		return recs;
	}
	
	@Override
	public String insertIntoTableSQL()
	{
		return String.format("insert or ignore into UserWarps " + 
				"(user, warp) " +  "values (%d, %d);", user, warp);
	}
	
	@Override
	public String deleteFromTableSQL() { return "delete from UserWarps where id=" + id + ";"; }
	
}