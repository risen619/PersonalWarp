package com.github.risen619.Collections;

import com.github.risen619.Database.DatabaseManager;

public abstract class PersonalWarpsCollections
{
	protected static DatabaseManager dm;
	
	public static void setDatabaseManager(DatabaseManager d) { dm = d; }
}