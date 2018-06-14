package com.github.risen619.Models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import com.github.risen619.Main;
import com.github.risen619.WarpsManager;
import com.github.risen619.Database.DatabaseCompatible;
import com.github.risen619.Database.ForeignKey;
import com.github.risen619.Database.PrimaryKey;
import com.github.risen619.Database.Table;
import com.github.risen619.Database.TableField;
import com.github.risen619.Database.ForeignKey.Action;

@Table(tableName = "Warps")
public class Warp implements DatabaseCompatible
{
	@PrimaryKey
	@TableField(name = "id", type = "integer")
	protected int id;
	
	@TableField(name = "name", type = "text", unique = true)
	protected String name;

	@ForeignKey(refTable = "Users", onDelete = Action.CASCADE, onUpdate = Action.CASCADE)
	@TableField(name = "owner", type = "integer")
	private int _owner;

	@TableField(name = "isPublic", type = "boolean")
	protected boolean isPublic;
	
	@TableField(name = "location", type="text")
	private String loc;
	
	private User owner;
	private Location location;
	private List<User> members;
	
	public Warp() {}
	
	public Warp(String name, int owner, boolean isPublic, String loc)
	{ this(-1, name, owner, isPublic, loc); }

	public Warp(String name, int owner, boolean isPublic, Location loc)
	{
		this(name, owner, isPublic, (String)null);
		this.loc = serializeLocation(loc);
		this.location = loc;
	}
	
	public Warp(int id, String name, int owner, boolean isPublic, String loc, boolean migrate)
	{
		this.id = id;
		this.name = name;
		this._owner = owner;
		this.isPublic = isPublic;
		this.loc = loc;
	}
	
	public Warp(int id, String name, int owner, boolean isPublic, String loc)
	{
		this.id = id;
		this.name = name;
		this._owner = owner;
		this.isPublic = isPublic;
		this.loc = loc;
		
		this.owner = WarpsManager.getInstance().getUserByID(_owner);
		this.location = deserializeLocation(loc);
		this.members = WarpsManager.getInstance().getMembersForWarp(id);
	}
	
	public int id() { return id; }
	public String name() { return name; }
	public boolean isPublic() { return isPublic; }
	public User owner() { return owner; }
	public Location location() { return location; }
	public List<User> members() { return members; }
	
	public static String serializeLocation(Location l)
	{
		return String.format("%s;%f;%f;%f;%f;%f", l.getWorld().getName(), l.getX(), l.getY(), l.getZ(),
			l.getYaw(), l.getPitch());
	}
	
	public static Location deserializeLocation(String l)
	{
		if(l == null) return null;
		String world;
		double x, y, z;
		float yaw, pitch;
		String[] parts = l.split(";");
		world = parts[0];
		x = Double.parseDouble(parts[1]);
		y = Double.parseDouble(parts[2]);
		z = Double.parseDouble(parts[3]);
		yaw = Float.parseFloat(parts[4]);
		pitch = Float.parseFloat(parts[5]);
		return new Location(Main.getPlugin(Main.class).getServer().getWorld(world), x, y, z, yaw, pitch);
	}
	
	@Override
	public String toString()
	{
		return String.format("%d: %s - %s located in " + loc, id, name, owner.name());
	}
	
	public List<DatabaseCompatible> fromResultSet(ResultSet rs)
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
				String location = rs.getString("location");
				warps.add(new Warp(id, name, owner, isPublic, location));
			}
		}
		catch (SQLException e) { e.printStackTrace(); }
		return warps;
	}
	
	@Override
	public String insertIntoTableSQL()
	{
		return String.format("insert or ignore into Warps " + 
				"(name, owner, isPublic, location) " + 
				"values (\"%s\", %d, %d, \"%s\");",
				name, _owner, isPublic ? 1 : 0, loc);
	}

	@Override
	public String deleteFromTableSQL() { return "delete from Warps where id=" + id + ";"; }
}