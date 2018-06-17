package com.github.risen619.Collections;

import java.util.Collection;
import java.util.HashMap;

import com.github.risen619.Database.DatabaseCompatible;
import com.github.risen619.Database.DatabaseManager;

public abstract class PersonalWarpCollection
{
	protected static DatabaseManager dm;
	protected HashMap<Integer, DatabaseCompatible> map;
	
	protected PersonalWarpCollection()
	{
		map = new HashMap<>();
	}
	
	public static void setDatabaseManager(DatabaseManager d) { dm = d; }
	
	public abstract void refresh();
	public abstract void refresh(int id);
	
	public abstract Collection<? extends DatabaseCompatible> get();
}