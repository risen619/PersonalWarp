package com.github.risen619.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.function.Function;

public class DatabaseManager
{
	private Connection connection = null;
	private String dbPath;
	
	public DatabaseManager(String dbPath)
	{
		this.dbPath = "jdbc:sqlite:" + dbPath;
		try { connection = DriverManager.getConnection(this.dbPath); }
		catch (SQLException e)
		{
			System.out.println("Could not establish connection to database");
			e.printStackTrace();
			return;
		}
		System.out.println("Connection with database " + this.dbPath + " is established");
	}

	public void closeConnection()
	{
		try { this.connection.close(); }
		catch (SQLException e)
		{
			System.out.println("Connection cannot be closed right now");
			e.printStackTrace();
			return;
		}
		System.out.println("Connection with " + this.dbPath + " closed");
	}

	public List<DatabaseCompatible> executeQuery(String sql, Function<ResultSet, List<DatabaseCompatible>> processor)
	{
		try(Statement s = connection.createStatement())
		{
			ResultSet rs = s.executeQuery(sql);
			return processor.apply(rs);
		}
		catch(SQLException e) { e.printStackTrace(); return null; }
	}
	
	public void executeUpdate(String sql)
	{
		try(Statement s = connection.createStatement()) { s.executeUpdate(sql); }
		catch(SQLException e) { e.printStackTrace(); }
	}
	
	public void createTable(Class<?> c)
	{
		String createQuery = SqlHelpers.create(c);
		String uniqueQuery = SqlHelpers.unique(c);
		
		System.out.println("CREATE:" + createQuery);
		System.out.println("UNIQUE:" + uniqueQuery);
		
		executeUpdate(createQuery + uniqueQuery);
	}
	
	public void truncateTable(Class<?> c)
	{
		String truncateQuery = SqlHelpers.truncate(c);
		
		System.out.println("TRUNCATE:" + truncateQuery);
		
		executeUpdate(truncateQuery);
	}
	
	public void dropTable(Class<?> c)
	{
		String dropQuery = SqlHelpers.drop(c);
		
		System.out.println("DROP:" + dropQuery);
		
		executeUpdate(dropQuery);
	}
	
	public List<DatabaseCompatible> select(Class<?> c, Function<ResultSet, List<DatabaseCompatible>> processor)
	{
		String selectQuery = SqlHelpers.select(c);
		
		System.out.println("SELECT:" + selectQuery);
		
		return executeQuery(selectQuery, processor);
	}
	
	@Deprecated
	synchronized public void createTable(String sql) { executeUpdate(sql); }
	
	@Deprecated
	public void truncateTable(String sql) { executeUpdate(sql); };
	
	@Deprecated
	public void dropTable(String sql) { executeUpdate(sql); }
	
	public void insert(String sql) { executeUpdate(sql); }
	public void insert(DatabaseCompatible dc) { executeUpdate(dc.insertIntoTableSQL()); }
	
	public void delete(String sql) { executeUpdate(sql); }
	public void delete(DatabaseCompatible dc) { executeUpdate(dc.deleteFromTableSQL()); }
	
	public void update(String sql) { executeUpdate(sql); }

	@Deprecated
	public List<DatabaseCompatible> select(String sql, Function<ResultSet, List<DatabaseCompatible>> processor)
	{ return executeQuery(sql, processor); }
	
}