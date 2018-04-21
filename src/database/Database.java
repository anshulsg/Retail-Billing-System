package database;

import java.sql.*;

public  class Database {
    private static Statement stmt = null;
    synchronized public static void init(String address, String database, String user, String password)
            throws DatabaseHandlerException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection temp =
                    DriverManager.getConnection("jdbc:mysql://" + address + "/" + database, user, password);
            stmt = temp.createStatement();
        }
        catch (ClassNotFoundException|SQLException exc){
            throw new DatabaseHandlerException("Database.init", exc);
        }
    }
    synchronized public static void init() throws DatabaseHandlerException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection temp =
                    DriverManager.getConnection("jdbc:mysql://127.0.0.1/RetailStore", "root", "gilchrist");
            stmt = temp.createStatement();
        }
        catch (ClassNotFoundException|SQLException exc){
            throw new DatabaseHandlerException("Database.init", exc);
        }
    }
    synchronized public static void createTable(String table_name, String arg) throws DatabaseHandlerException {
        try{
            stmt.executeUpdate("CREATE TABLE " + table_name + "(" + arg + ")");
        }
        catch (Exception exc){
            throw new DatabaseHandlerException("Database.createTable", exc);
        }
    }
    synchronized public static boolean hasTable(String table_name) throws DatabaseHandlerException {
        try {
            ResultSet res = stmt.executeQuery("SHOW TABLES");
            while (res.next()) {
                if (res.getString(1).equals(table_name)) return true;
            }
            return false;
        }
        catch (Exception exc){
            throw new DatabaseHandlerException("Database.hasTable", exc);
        }
    }
    synchronized public static int insert(String table_name, String values) throws DatabaseHandlerException{
        try {
            return stmt.executeUpdate("INSERT INTO " + table_name + " VALUES(" + values + ")");
        }
        catch (Exception exc){
            throw new DatabaseHandlerException("Database.insert", exc);
        }
    }
    synchronized public static int delete(String table_name, String attribute, String value)
    throws DatabaseHandlerException{
    	try{
    		return stmt.executeUpdate("DELETE FROM " + table_name + " WHERE " + attribute + "=" + value);
    	}
    	catch(Exception exc){
    		throw new DatabaseHandlerException("Database.delete", exc);
    	}
    }
    synchronized public static ResultSet getTable(String table_name) throws DatabaseHandlerException{
    	try{
    		return stmt.executeQuery("SELECT * FROM "+ table_name);
    	}
    	catch(Exception exc){
    		throw new DatabaseHandlerException("Database.getTable()", exc);
    	}
    }
    synchronized public static ResultSet searchFieldInTable(String table_name, String attribute, String value)
            throws DatabaseHandlerException{
        try {
            return stmt.executeQuery("SELECT * FROM " + table_name + " WHERE " + attribute + "=" + value);
        }
        catch (Exception exc){
            throw new DatabaseHandlerException("Database.searchFieldInTable()", exc);
        }
    }
    synchronized public static void update(String table_name, String attribute, String value, String where)
    		throws DatabaseHandlerException{
    	try{
    		stmt.executeUpdate("UPDATE "+ table_name + " SET "+attribute+"="+value+" WHERE "+ where);
    	}
    	catch(Exception exc){
    		throw new DatabaseHandlerException("Database.update", exc);
    	}
    	
    }
    synchronized public static ResultSet query(String query) throws DatabaseHandlerException
    {
    	try{
    		return stmt.executeQuery(query);
    	}
    	catch(SQLException e){
    		throw new DatabaseHandlerException("Database.query", e);
    	}
    	
    }
    synchronized public static int update(String query) throws DatabaseHandlerException
    {
    	try{
    		return stmt.executeUpdate(query);
    	}
    	catch(SQLException e){
    		throw new DatabaseHandlerException("Database.update", e);
    	}
    }
}