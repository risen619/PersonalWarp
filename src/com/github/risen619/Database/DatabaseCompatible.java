package com.github.risen619.Database;

import java.sql.ResultSet;
import java.util.List;

public interface DatabaseCompatible
{
	public static String createTableSQL() { throw new UnsupportedOperationException("Method not implemented"); }
	public static String dropTableSQL() { throw new UnsupportedOperationException("Method not implemented"); }
	public static String truncateTableSQL() { throw new UnsupportedOperationException("Method not implemented"); }
	public static String selectFromTableSQL() { throw new UnsupportedOperationException("Method not implemented"); }
	
	public <T extends DatabaseCompatible>List<T> fromResultSet(ResultSet rs);
	public String insertIntoTableSQL();
	public String deleteFromTableSQL();
}