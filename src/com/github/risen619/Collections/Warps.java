package com.github.risen619.Collections;

import java.util.List;
import java.util.stream.Collectors;

import com.github.risen619.Warp;
import com.github.risen619.Database.DatabaseCompatible;

public class Warps extends PersonalWarpsCollections
{
	private static Warps instance = null;
	
	private List<Warp> warps = null;
	
	private Warps() { fetchWarps(); }
	
	public static Warps getInstance()
	{
		if(instance == null)
			instance = new Warps();
		return instance;
	}
	
	public List<Warp> getWarps() { return warps; }
	
	private void fetchWarps()
	{
		List<DatabaseCompatible> dcs = dm.select(Warp.selectFromTableSQL(), rs -> Warp.fromResultSet(rs));
		warps = dcs.stream().map(dc -> (Warp)dc).collect(Collectors.toList());
	}
	
	public List<Warp> getPublic()
	{
		if(warps == null)
			fetchWarps();
		return warps.stream().filter(w -> ((Warp)w).getIsPublic()).collect(Collectors.toList());
	}
	
	public List<Warp> getByOwnerId(int id)
	{
		if(warps == null)
			fetchWarps();
		return warps.stream().filter(w -> ((Warp)w).getOwner() == id).collect(Collectors.toList());
	}
	
	public boolean exists(String name)
	{
		if(warps == null)
			fetchWarps();
		return warps.stream().anyMatch(w -> ((Warp)w).getName().equalsIgnoreCase(name));
	}
	
	public void add(Warp w)
	{
		if(w == null) return;
		dm.insert(w);
		warps.add(
			(Warp)dm.select(
				String.format("select * from Warps where Warps.name=\"%s\"", w.getName()), 
				rs -> Warp.fromResultSet(rs)
			)
			.get(0)
		);
	}
	
}