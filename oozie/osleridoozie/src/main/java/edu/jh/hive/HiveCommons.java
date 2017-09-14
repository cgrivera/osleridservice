package edu.jh.hive;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.hadoop.security.UserGroupInformation;

public class HiveCommons {
	private static String driverName = "org.apache.hive.jdbc.HiveDriver";
	//String url = "jdbc:hive2://d01-hgm6.win.ad.jhu.edu:2181/default;principal=hive/d01-hgm6.win.ad.jhu.edu@WIN.AD.JHU.EDU;serviceDiscoveryMode=zooKeeper;zooKeeperNamespace=hiveserver2";
	String url = "jdbc:hive2://d01-hgm6.win.ad.jhu.edu:10000/default;principal=hive/d01-hgm6.win.ad.jhu.edu@WIN.AD.JHU.EDU";
	String user = "pmsvc-oid";
	String pass = "Azure0712";
	String poc = "jdbc:hive2://mrthdpnn1.hosts.jhmi.edu:2181,mrthdpadm1.hosts.jhmi.edu:2181,mrthdpnn2.hosts.jhmi.edu:2181/;serviceDiscoveryMode=zooKeeper;zooKeeperNamespace=hiveserver2";
	private Connection con;
	
	public HiveCommons(){
		try {
            Class.forName(driverName);
          } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
          }

			try {
				
				  System.out.println("getting connection");
				   con = DriverManager.getConnection("jdbc:hive2://d01-hgm6.win.ad.jhu.edu:10000/;principal=hive/d01-hgm6.win.ad.jhu.edu@WIN.AD.JHU.EDU");
				  System.out.println("got connection");
				  con.close();
				//con = DriverManager.getConnection(url);
			}  catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		
	}
	
	private ResultSet runSQLResultSet(String sql)  throws SQLException{
		
	 
		//note:  the username parameter is required in the connection string; otherwise an error is thrown  ***
		//Connection con = DriverManager.getConnection("jdbc:hive2://mrthdpnn1.hosts.jhmi.edu:2181,mrthdpadm1.hosts.jhmi.edu:2181,mrthdpnn2.hosts.jhmi.edu:2181/;serviceDiscoveryMode=zooKeeper;zooKeeperNamespace=hiveserver2", "pmsvc-oid", "");
		
		Statement stmt = con.createStatement();

		System.out.println(sql+"\n");
		ResultSet res = stmt.executeQuery(sql);
		return res;
	}
	
	private void runSQL(String sql)  throws SQLException{
		
		Statement stmt = con.createStatement();

		System.out.println(sql+"\n");
		stmt.execute(sql);

	}
	
	public static void main(String args[]){
		try {
			HiveCommons hive = new HiveCommons();
			ResultSet rs = hive.runSQLResultSet("select * from take_off");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
