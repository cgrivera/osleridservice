package edu.jh.oslerids;

public class IdMappingOozie {
	String table = "";
	String col1 = "";
	String domain1 = "";
	String col2 = "";
	String domain2 = "";
	String condition = "";
	
	
	
	public void setCondition(String condition) {
		this.condition = condition;
	}



	public IdMappingOozie(String table, String col1, String domain1, String col2, String domain2) {
		super();
		this.table = table;
		this.col1 = col1;
		this.domain1 = domain1;
		this.col2 = col2;
		this.domain2 = domain2;
	}

	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		IdMappingOozie idmap = new IdMappingOozie(args[0],args[1],args[2],args[3],args[4]);
		if(args.length>4){
			//contains condition
			idmap.setCondition(args[5]);
		}
		idmap.run();
	}

	private void convertOslerID(String from, String to){
		
	}
	
	
	private void addMissing(String table,String col,String conditional){
		
	}


	private void run() {
		// TODO Auto-generated method stub
		
	}

}
