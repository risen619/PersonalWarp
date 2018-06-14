package com.github.risen619.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
	}

	public Connection connection() { return connection; }
	
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

	synchronized public ResultSet executeQuery(String sql)
	{
		try(Statement s = connection.createStatement())
		{
			ResultSet rs = s.executeQuery(sql);
			return rs;
		}
		catch(SQLException e) { e.printStackTrace(); return null; }
	}
	
	synchronized public List<DatabaseCompatible> executeQuery(String sql, Function<ResultSet, List<DatabaseCompatible>> processor)
	{
		try(Statement s = connection.createStatement())
		{
			ResultSet rs = s.executeQuery(sql);
			return processor.apply(rs);
		}
		catch(SQLException e) { e.printStackTrace(); return null; }
	}
	
	synchronized public void executeUpdate(String sql)
	{
		try(Statement s = connection.createStatement()) { s.executeUpdate(sql); }
		catch(SQLException e) { e.printStackTrace(); }
	}
	
	synchronized public void createTable(Class<?> c)
	{
		String createQuery = SqlHelpers.create(c);
		String uniqueQuery = SqlHelpers.unique(c);
		
		executeUpdate(createQuery + uniqueQuery);
	}
	
	public void truncateTable(Class<?> c)
	{
		String truncateQuery = SqlHelpers.truncate(c);
		
		executeUpdate(truncateQuery);
	}
	
	public void dropTable(Class<?> c)
	{
		String dropQuery = SqlHelpers.drop(c);
		
		executeUpdate(dropQuery);
	}
	
	public List<DatabaseCompatible> select(Class<? extends DatabaseCompatible> c)
	{		
		try
		{
			DatabaseCompatible dc = c.newInstance();
			Statement s;
			try
			{
				s = connection.createStatement();
				ResultSet rs = s.executeQuery(SqlHelpers.select(c));
				return dc.fromResultSet(rs);
			}
			catch (SQLException e) {}
		}
		catch (InstantiationException | IllegalAccessException e) {}
		return new ArrayList<DatabaseCompatible>();
	}
	
	public List<DatabaseCompatible> select(Class<? extends DatabaseCompatible> c, Function<ResultSet, List<DatabaseCompatible>> processor)
	{
		try(Statement s = connection.createStatement())
		{
			ResultSet rs = s.executeQuery(SqlHelpers.select(c));
			return processor.apply(rs);
		}
		catch (SQLException e) {}
		return new ArrayList<DatabaseCompatible>();
	}
	
	public List<DatabaseCompatible> selectBy(Class<? extends DatabaseCompatible> c, String clause)
	{
		String selectQuery = SqlHelpers.select(c).replaceAll(";", "") + " where " + clause;
		try
		{
			DatabaseCompatible dc = c.newInstance();
			Statement s;
			try
			{
				s = connection.createStatement();
				ResultSet rs = s.executeQuery(selectQuery);
				return dc.fromResultSet(rs);
			}
			catch (SQLException e) {}
		}
		catch (InstantiationException | IllegalAccessException e) {}
		return new ArrayList<DatabaseCompatible>();
	}
	
	public void insert(String sql) { executeUpdate(sql); }
	public void insert(DatabaseCompatible dc) { executeUpdate(dc.insertIntoTableSQL()); }
	
	public void delete(String sql) { executeUpdate(sql); }
	public void delete(DatabaseCompatible dc) { executeUpdate(dc.deleteFromTableSQL()); }
	
	public void update(String sql) { executeUpdate(sql); }

	@Deprecated
	public List<DatabaseCompatible> select(String sql, Function<ResultSet, List<DatabaseCompatible>> processor)
	{ return executeQuery(sql, processor); }
	
}