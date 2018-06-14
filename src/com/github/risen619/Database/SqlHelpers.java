package com.github.risen619.Database;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class SqlHelpers
{
	private static String createFieldStatement(Field f)
	{
		TableField tf = f.getAnnotation(TableField.class);
		String result = String.format("%s %s", tf.name(), tf.type()); 
		
		if(f.isAnnotationPresent(PrimaryKey.class))
		{
			result += " primary key";
			if(f.getAnnotation(PrimaryKey.class).autoincrement())
				result += " autoincrement";
		}
		else if(!tf.nullable()) result += " not null";
		
		return result;
	}
	
	private static String createForeignKeyStatement(Field f)
	{
		TableField tf = f.getAnnotation(TableField.class);
		ForeignKey fk = f.getAnnotation(ForeignKey.class);
		
		String s = String.format("constraint fk_%s foreign key (%s) references %s(%s) on delete %s on update %s",
			fk.refTable()+"_"+fk.refField(), tf.name(), fk.refTable(), fk.refField(),
			fk.onDelete().toString(), fk.onUpdate().toString());
		
		return s;
	}
	
	public static String create(Class<?> c) throws IllegalArgumentException
	{
		if(!c.isAnnotationPresent(Table.class))
			throw new IllegalArgumentException("Class must be decorated with @Table");
		
		String name = c.getAnnotation(Table.class).tableName();
		List<Field> fields = Arrays.asList(c.getDeclaredFields()).stream()
		.filter(v -> v.isAnnotationPresent(TableField.class)).collect(Collectors.toList());
		
		if(fields.isEmpty()) return "";
		
		String query = "create table if not exists " + name + " (";
		List<String> fks = new ArrayList<String>();
		for(Field f : fields)
		{
			query += createFieldStatement(f) + ", ";
			if(f.isAnnotationPresent(ForeignKey.class))
				fks.add(createForeignKeyStatement(f));
		}
		
		for(String s : fks)
			query += s + ", ";
		
		query = query.substring(0, query.length() - 2) + ");";
		
		return query;
	}
	
	public static String unique(Class<?> c) throws IllegalArgumentException
	{
		if(!c.isAnnotationPresent(Table.class))
			throw new IllegalArgumentException("Class must be decorated with @Table");
		
		String name = c.getAnnotation(Table.class).tableName();
		List<TableField> fields = Arrays.asList(c.getDeclaredFields()).stream()
		.filter(v -> v.isAnnotationPresent(TableField.class) && v.getAnnotation(TableField.class).unique())
		.map(v -> v.getAnnotation(TableField.class)).collect(Collectors.toList());
		
		String query = "";
		if(!fields.isEmpty())
		{
			String uniqueName = "create unique index if not exists " + name;
			String uniqueOn = " on " + name + "(";
			for(TableField f : fields)
			{
				uniqueName += "_" + f.name();
				uniqueOn += f.name() + ", ";
			}
			query = uniqueName + uniqueOn.substring(0, uniqueOn.length() - 2) + ");";
		}
		
		return query;
	}
	
	public static String select(Class<?> c) throws IllegalArgumentException
	{
		if(!c.isAnnotationPresent(Table.class))
			throw new IllegalArgumentException("Class must be decorated with @Table");
		
		String name = c.getAnnotation(Table.class).tableName();
		return "select * from " + name + ";";
	}
	
	public static String truncate(Class<?> c) throws IllegalArgumentException
	{
		if(!c.isAnnotationPresent(Table.class))
			throw new IllegalArgumentException("Class must be decorated with @Table");
		
		String name = c.getAnnotation(Table.class).tableName();
		return "delete from " + name + ";";
	}
	
	public static String drop(Class<?> c) throws IllegalArgumentException
	{
		if(!c.isAnnotationPresent(Table.class))
			throw new IllegalArgumentException("Class must be decorated with @Table");
		
		String name = c.getAnnotation(Table.class).tableName();
		return "drop table if exists " + name + ";";
	}
	
}