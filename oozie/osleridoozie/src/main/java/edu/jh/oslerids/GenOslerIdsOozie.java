package edu.jh.oslerids;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class GenOslerIdsOozie {

	private static String driverName = "org.apache.hive.jdbc.HiveDriver";
	//private static String[][] props=new String[2][2];
	Properties prop = new Properties();
	String tableName = "";
	String columnName = "";
	String domainName = "";
	
	
	
	public GenOslerIdsOozie(String tableName, String columnName, String domainName)  throws SQLException {
		super();
		this.tableName = tableName;
		this.columnName = columnName;
		this.domainName = domainName;
		
		System.out.println("Operating on Table: "+this.tableName+ " column: "+this.columnName+ " and domain: "+this.domainName);
		
		//this.runSQL("DROP function if exists osler.GetOslerIds");
		//this.runSQL("DROP function if exists NUMBER_ROWS");
		 
		try {
			this.runSQL("CREATE FUNCTION osler.GetOslerIds AS 'edu.jh.GenOslerIds.NextOslerIds' USING JAR 'hdfs:///user/osler/udf/edu.jh.GenOslerIds-0.0.1-SNAPSHOT.jar' ");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace(); 
		}
		try {
			this.runSQL("CREATE FUNCTION NUMBER_ROWS AS 'edu.jh.GenOslerIds.UDFNumberRows' USING JAR 'hdfs:///user/osler/udf/edu.jh.GenOslerIds-0.0.1-SNAPSHOT.jar' ");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} 
		 
		int idcount = this.getNumOslerIdsNeeded();
		if(idcount>0){
			String insertsql = "insert into osler.id_map select \""+this.domainName+"\",n.id,\"oslerid\",o.oslerids,\"active\",CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP() from "+
					"(select osler.GetOslerIds("+idcount+")) as o, "+
					"(select NUMBER_ROWS() as rowid, * from "+
					"	(select  distinct(p."+this.columnName+") as id from "+this.tableName+" as p "+
					"	left outer join osler.id_map as m "+
					"	on p."+this.columnName+"=m.source_id  and m.source_domain=\""+this.domainName+"\" "+
					"	where m.source_id is null) as s ) as n "+
					"where n.rowid=o.rowid ";
			this.runSQL(insertsql);
		}
	}

		
	public static void main(String[] args)  throws SQLException {

    	String tableName = args[0];
    	String columnName = args[1];
    	String domainName = args[2];
    	
    	GenOslerIdsOozie gen = new GenOslerIdsOozie(tableName,columnName,domainName);
    	

    	
	}
	
	private  int getNumOslerIdsNeeded()  throws SQLException{
		int idcount = 0;   		
		String sql = "select  count(distinct(p."+this.columnName+")) as id from "+this.tableName+" as p "+
				"left outer join osler.id_map as m "+
				"on p."+this.columnName+"=m.source_id  and m.source_domain=\""+this.domainName+"\" "+
				"where m.source_id is null";
		ResultSet res = this.runSQLResultSet(sql);
		if (res.next()) { idcount = res.getInt(1); }
		System.out.println("# missing oslerids: "+idcount);
		return idcount;
	}
	
	private ResultSet runSQLResultSet(String sql)  throws SQLException{
		try {
	            Class.forName(driverName);
	          } catch (ClassNotFoundException e) {
	            e.printStackTrace();
	            System.exit(1);
	          }
	 
		//note:  the username parameter is required in the connection string; otherwise an error is thrown  ***
		Connection con = DriverManager.getConnection("jdbc:hive2://mrthdpnn1.hosts.jhmi.edu:2181,mrthdpadm1.hosts.jhmi.edu:2181,mrthdpnn2.hosts.jhmi.edu:2181/;serviceDiscoveryMode=zooKeeper;zooKeeperNamespace=hiveserver2", "rackerm3", "");
		Statement stmt = con.createStatement();

		System.out.println(sql+"\n");
		ResultSet res = stmt.executeQuery(sql);
		return res;
	}
	
	private void runSQL(String sql)  throws SQLException{
		try {
	            Class.forName(driverName);
	          } catch (ClassNotFoundException e) {
	            e.printStackTrace();
	            System.exit(1);
	          }
	 
		//note:  the username parameter is required in the connection string; otherwise an error is thrown  ***
		Connection con = DriverManager.getConnection("jdbc:hive2://mrthdpnn1.hosts.jhmi.edu:2181,mrthdpadm1.hosts.jhmi.edu:2181,mrthdpnn2.hosts.jhmi.edu:2181/;serviceDiscoveryMode=zooKeeper;zooKeeperNamespace=hiveserver2", "rackerm3", "");
		Statement stmt = con.createStatement();

		System.out.println(sql+"\n");
		stmt.execute(sql);

	}
	
	private void writeOozieProperty(){
      try{
	         File file = new File(System.getProperty("oozie.action.output.properties"));
	         OutputStream os = new FileOutputStream(file);
	         this.prop.store(os, "");
	         os.close();
	         System.out.println(file.getAbsolutePath());
	      }
	      catch (Exception e) {
	         e.printStackTrace();
	      }	
	}

}
