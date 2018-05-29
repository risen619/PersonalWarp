package com.github.risen619.Collections;

import java.util.List;
import java.util.stream.Collectors;

import com.github.risen619.Database.DatabaseCompatible;
import com.github.risen619.Models.Warp;
import com.github.risen619.Models.WarpModel;

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
		List<DatabaseCompatible> dcs = dm.select(WarpModel.selectFromTableSQL(), rs -> WarpModel.fromResultSet(rs));
		warps = dcs.stream().map(dc -> new Warp((WarpModel)dc)).collect(Collectors.toList());
	}

	public void refresh() { fetchWarps(); }
	public void refresh(int id)
	{
		warps.removeIf(w -> w.getId() == id);
		List<DatabaseCompatible> dcs = dm.select(String.format("select * from Warps where Warps.id=%d;", id),
			rs -> WarpModel.fromResultSet(rs));
		warps.addAll(dcs.stream().map(dc -> new Warp((WarpModel)dc)).collect(Collectors.toList()));
	}
	
	public List<Warp> getPublic()
	{
		if(warps == null)
			fetchWarps();
		return warps.stream().filter(w -> w.getIsPublic()).collect(Collectors.toList());
	}
	
	public List<Warp> getByOwnerId(int id)
	{
		if(warps == null)
			fetchWarps();
		return warps.stream().filter(w -> w.getOwner().getId() == id).collect(Collectors.toList());
	}
	
	public List<Warp> getByMemberUUID(String uuid)
	{
		if(warps == null)
			fetchWarps();
		return warps.stream().filter(w -> {
			return w.getMembers().stream().anyMatch(m -> m.getUUID().equals(uuid));
		}).collect(Collectors.toList());
	}
	
	public Warp getByName(String name)
	{
		if(warps == null)
			fetchWarps();
		List<Warp> ws = warps.stream().filter(w -> w.getName().equalsIgnoreCase(name))
			.limit(1).collect(Collectors.toList());
		if(ws == null || ws.size() == 0) return null;
		return ws.get(0);
	}
	
	public boolean exists(String name)
	{
		if(warps == null)
			fetchWarps();
		return warps.stream().anyMatch(w -> w.getName().equalsIgnoreCase(name));
	}
	
	public void add(WarpModel w)
	{
		if(w == null) return;
		dm.insert(w);
		fetchWarps();
	}
	
}