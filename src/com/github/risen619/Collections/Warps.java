package com.github.risen619.Collections;

import java.util.List;
import java.util.stream.Collectors;

import com.github.risen619.Database.DatabaseCompatible;
import com.github.risen619.Models.Warp;

public class Warps extends PersonalWarpsCollections
{
	private static Warps instance = null;
	
	private List<Warp> warps = null;
	
	private Warps() { fetchWarps(); }
	
	synchronized public static Warps getInstance()
	{
		if(instance == null)
			instance = new Warps();
		return instance;
	}
	
	public List<Warp> getWarps() { return warps; }
	
	private void fetchWarps()
	{
		List<DatabaseCompatible> dcs = dm.select(Warp.class);
		warps = dcs.stream().map(dc -> (Warp)dc).collect(Collectors.toList());
	}

	public void refresh() { fetchWarps(); }
	public void refresh(int id)
	{
		warps.removeIf(w -> w.id() == id);
		List<DatabaseCompatible> dcs = dm.selectBy(Warp.class, String.format("Warps.id=%d;", id));
		warps.addAll(dcs.stream().map(dc -> (Warp)dc).collect(Collectors.toList()));
	}
	
	public List<Warp> getPublic()
	{
		if(warps == null)
			fetchWarps();
		return warps.stream().filter(w -> w.isPublic()).collect(Collectors.toList());
	}
	
	public List<Warp> getByOwnerId(int id)
	{
		if(warps == null)
			fetchWarps();
		return warps.stream().filter(w -> w.owner().id() == id).collect(Collectors.toList());
	}
	
	public List<Warp> getByMemberUUID(String uuid)
	{
		if(warps == null)
			fetchWarps();
		return warps.stream().filter(w -> {
			return w.members().stream().anyMatch(m -> m.uuid().equals(uuid));
		}).collect(Collectors.toList());
	}
	
	public Warp getByName(String name)
	{
		if(warps == null)
			fetchWarps();
		List<Warp> ws = warps.stream().filter(w -> w.name().equalsIgnoreCase(name))
			.limit(1).collect(Collectors.toList());
		if(ws == null || ws.size() == 0) return null;
		return ws.get(0);
	}
	
	public void delete(int id)
	{
		dm.delete(String.format("delete from Warps where Warps.id=%d;", id));
		fetchWarps();
		UserWarps.getInstance().refresh();
	}
	
	public boolean exists(String name)
	{
		if(warps == null)
			fetchWarps();
		return warps.stream().anyMatch(w -> w.name().equalsIgnoreCase(name));
	}
	
	public void add(Warp w)
	{
		if(w == null) return;
		System.out.println("WARP: " + w.toString());
		dm.insert(w);
		fetchWarps();
	}
	
}