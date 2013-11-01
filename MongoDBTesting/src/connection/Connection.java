package connection;

import java.net.UnknownHostException;

import com.google.gson.*;
import com.mongodb.CommandResult;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;
import com.mongodb.ServerAddress;

public class Connection {
	
	MongoClient client;
	DB db;
	public Connection(){
		
		try {
			client = new MongoClient("localhost:27017");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db = client.getDB("local");
	}
	
	public void addData(String data){
		DBCollection coll = db.getCollection("testData");
		
		coll.insert(new BasicDBObject("Data", data));
		
		
		
	}
	
	public void printData(){
		DBCollection coll = db.getCollection("testData");
		
		DBCursor cursor = coll.find();
		
		try{
			while(cursor.hasNext()){
				System.out.println(cursor.next());
			}
		}
		finally{
			cursor.close();
		}
		
		
	}
	
	

}
