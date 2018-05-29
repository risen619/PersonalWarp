package com.github.risen619.Collections;

import java.util.List;
import java.util.stream.Collectors;

import com.github.risen619.Database.DatabaseCompatible;
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
		List<DatabaseCompatible> dcs = dm.select(UserWarpModel.selectFromTableSQL(),
			rs -> UserWarpModel.fromResultSet(rs));
		userWarps = dcs.stream().map(dc -> ((UserWarpModel)dc)).collect(Collectors.toList());
	}
	
	public List<UserWarpModel> getMembersOfWarp(int warpId)
	{
		if(userWarps == null)
			fetchUserWarps();
		return userWarps.stream().filter(w -> w.getWarp() == warpId).collect(Collectors.toList());
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
		userWarps.add(
			(UserWarpModel)dm.select(
				String.format("select * from UserWarps where UserWarps.user=%d & UserWarps.warp=%d;",
					w.getUser(), w.getWarp()), 
				rs -> UserWarpModel.fromResultSet(rs)
			)
			.get(0)
		);
	}
}