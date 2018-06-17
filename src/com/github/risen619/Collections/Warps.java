package com.github.risen619.Collections;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.github.risen619.Models.Warp;

public class Warps extends PersonalWarpCollection
{
	private static Warps instance = null;
	private HashMap<Integer, Warp> map;
	
	private Warps()
	{
		map = new HashMap<>();
		fetchWarps();
	}
	
	synchronized public static Warps getInstance()
	{
		if(instance == null)
			instance = new Warps();
		return instance;
	}
	
	@Override
	public Collection<Warp> get() { return map.values(); }
	
	private void fetchWarps()
	{
		List<Warp> ws = dm.select(Warp.class);
		for(Warp w : ws) map.put(w.id(), w);
	}

	public void refresh() { fetchWarps(); }
	public void refresh(int id)
	{
		map.remove(id);
		List<Warp> ws = dm.selectBy(Warp.class, String.format("Warps.id=%d;", id));
		for(Warp w : ws) map.put(w.id(), w);
	}
	
	public List<Warp> getPublic()
	{
		return map.values().stream().filter(w -> ((Warp)w).isPublic()).collect(Collectors.toList());
	}
	
	public List<Warp> getByOwnerId(int id)
	{
		return map.values().stream().filter(w -> w.owner().id() == id).collect(Collectors.toList());
	}
	
	public List<Warp> getByMemberUUID(String uuid)
	{
		return map.values().stream().filter(w -> {
			return w.members().stream().anyMatch(m -> m.uuid().equals(uuid));
		}).collect(Collectors.toList());
	}
	
	public Warp getByName(String name)
	{
		List<Warp> ws = map.values().stream().filter(w -> w.name().equalsIgnoreCase(name))
			.limit(1).collect(Collectors.toList());
		if(ws == null || ws.size() == 0) return null;
		return ws.get(0);
	}
	
	public void delete(int id)
	{
		dm.delete(String.format("delete from Warps where Warps.id=%d;", id));
		map.remove(id);
		UserWarps.getInstance().refresh();
	}
	
	public boolean exists(String name)
	{
		return map.values().stream().anyMatch(w -> w.name().equalsIgnoreCase(name));
	}
	
	public void add(Warp w)
	{
		if(w == null) return;
		dm.insert(w);
		fetchWarps();
	}
	
}