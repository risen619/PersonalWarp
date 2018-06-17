package com.github.risen619.Collections;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.github.risen619.Models.UserWarpModel;

public class UserWarps extends PersonalWarpCollection
{
	private static UserWarps instance = null;
	private HashMap<Integer, UserWarpModel> map;
	
	private UserWarps()
	{
		map = new HashMap<>();
		fetchUserWarps();
	}
	
	synchronized public static UserWarps getInstance()
	{
		if(instance == null)
			instance = new UserWarps();
		return instance;
	}

	public Collection<UserWarpModel> get() { return map.values(); }
	
	private void fetchUserWarps()
	{
		List<UserWarpModel> uws = dm.select(UserWarpModel.class);
		for(UserWarpModel uw : uws)
			map.put(uw.id(), uw);
	}
	
	public void refresh() { fetchUserWarps(); }
	public void refresh(int id)
	{
		map.remove(id);
		List<UserWarpModel> uws = dm.selectBy(UserWarpModel.class, String.format("UserWarps.id=%d;", id));
		for(UserWarpModel uw : uws)
			map.put(uw.id(), uw);
	}
	
	public List<UserWarpModel> getMembersOfWarp(int warpId)
	{
		return map.values().stream().filter(w -> w.warp() == warpId).collect(Collectors.toList());
	}
	
	public void deleteWarpWithId(int id)
	{
		dm.delete(String.format("delete * from UserWarps where UserWarps.warp=%d;", id));
		fetchUserWarps();
	}
	
	public boolean exists(int user, int warp)
	{
		return map.values().stream().anyMatch(w -> w.user() == user && w.warp() == warp);
	}
	
	public void add(UserWarpModel w)
	{
		if(w == null) return;
		dm.insert(w);
		fetchUserWarps();
	}
}