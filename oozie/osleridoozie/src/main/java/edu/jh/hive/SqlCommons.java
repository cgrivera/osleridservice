package edu.jh.hive;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlCommons {
	final String JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	//final String JDBC_CONNECTION = "jdbc:sqlserver://esmpmdbdev2.esm.johnshopkins.edu;authenticationScheme=NativeAuthentication;integratedSecurity=true;databaseName=PM_IDManagement";
	//JavaKerberos
	final String JDBC_CONNECTION = "jdbc:sqlserver://esmpmdbdev2.esm.johnshopkins.edu;authenticationScheme=JavaKerberos;integratedSecurity=true;databaseName=PM_IDManagement";
	final String user = "pmsvc-oid";
	private Connection conn;
	
	public SqlCommons(){
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection(JDBC_CONNECTION);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Create a statement
	}
	
	public ResultSet queryResultSet(String sql){
		
		try {
			Statement stmt = this.conn.createStatement();
			// Execute the query
			ResultSet rs = stmt.executeQuery(sql);
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
public void query(String sql){
		
		try {
			Statement stmt = this.conn.createStatement();
			// Execute the query
			stmt.executeQuery(sql);
	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]){
		SqlCommons conn = new SqlCommons();
		conn.query("create table oslerid (index varchar(12)) ");
	}
}
