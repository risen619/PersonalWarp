package com.github.risen619.Collections;

import java.util.List;
import java.util.stream.Collectors;

import com.github.risen619.Database.DatabaseCompatible;
import com.github.risen619.Models.User;
import com.github.risen619.Models.UserWarpModel;

public class UserWarps extends PersonalWarpsCollections
{
	private static UserWarps instance = null;
	
	private List<UserWarpModel> userWarps = null;
	
	private UserWarps() { fetchUserWarps(); }
	
	synchronized public static UserWarps getInstance()
	{
		if(instance == null)
			instance = new UserWarps();
		return instance;
	}
	
	public List<UserWarpModel> getUserWarps() { return userWarps; }
	
	private void fetchUserWarps()
	{
		List<DatabaseCompatible> dcs = dm.select(UserWarpModel.class);
		userWarps = dcs.stream().map(dc -> ((UserWarpModel)dc)).collect(Collectors.toList());
	}
	
	public void refresh() { fetchUserWarps(); }
	public void refresh(int id)
	{
		userWarps.removeIf(w -> w.getId() == id);
		List<DatabaseCompatible> dcs = dm.selectBy(User.class, String.format("UserWarps.id=%d;", id));
		userWarps.addAll(dcs.stream().map(dc -> (UserWarpModel)dc).collect(Collectors.toList()));
	}
	
	public List<UserWarpModel> getMembersOfWarp(int warpId)
	{
		if(userWarps == null)
			fetchUserWarps();
		return userWarps.stream().filter(w -> w.getWarp() == warpId).collect(Collectors.toList());
	}
	
	public void deleteWarpWithId(int id)
	{
		dm.delete(String.format("delete * from UserWarps where UserWarps.warp=%d;", id));
		fetchUserWarps();
	}
	
	public boolean exists(int user, int warp)
	{
		if(userWarps == null)
			fetchUserWarps();
		return userWarps.stream().anyMatch(w -> w.getUser() == user && w.getWarp() == warp);
	}
	
	public void add(UserWarpModel w)
	{
		if(w == null) return;
		dm.insert(w);
		fetchUserWarps();
	}
}