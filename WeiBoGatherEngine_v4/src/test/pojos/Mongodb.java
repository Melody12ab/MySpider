package test.pojos;

import java.net.UnknownHostException;
import java.util.Iterator;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class Mongodb {

	public static Mongo mongo;
	private DB db;

	static {
		try {
			// mongo = new Mongo("127.0.0.1", 10000);
			mongo = new Mongo("127.0.0.1", 27017);
			// mongo = new Mongo("127.0.0.1", 30000);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public DBCursor getCursor(DBCollection collection) {
		return collection.find();
	}

	public DB getDB(String dbName) {
		db = mongo.getDB(dbName);
		return db;
	}

	public boolean isExist(String collectionName) {
		return db.collectionExists(collectionName);
	}

	public DBCollection getCollection(String collectionName) {
		DBCollection dbCollection = db.getCollection(collectionName);
		return dbCollection;
	}

	public void saveObj(DBCollection table, DBObject obj) {
		table.save(obj);
	}

	public static JSONParser parser = new JSONParser();

	public static void testSaveStringPair() throws Exception {
		String dbName = "zel";
		DB db = mongo.getDB(dbName);
		String collectionName = "uuid";

		DBCollection table = db.getCollection(collectionName);

		BasicDBObject doc = new BasicDBObject();
		doc.put("username", "two");

		table.save(doc);
	}

	public static void testSaveOneObj() {
		String dbName = "zel";
		DB db = mongo.getDB(dbName);
		String collectionName = "uuid";

		DBCollection table = db.getCollection(collectionName);

		BasicDBObject doc = new BasicDBObject();

		BasicDBObject innerObj = new BasicDBObject();

		innerObj.put("keyStr", "valueStr");

		doc.put("inner", innerObj);

		table.save(doc);
	}

	public static void testFindOneObj() throws Exception {
		String dbName = "zel";
		DB db = mongo.getDB(dbName);
		db.authenticate("zel","zhouking".toCharArray());
		String collectionName = "comments";

		DBCollection table = db.getCollection(collectionName);
		DBCursor cursor = table.find();

		Iterator<DBObject> iter = cursor.iterator();
		while (iter.hasNext()) {
			String str = iter.next().toString();
			JSONObject jsonObj = (JSONObject) parser.parse(str);
//			Object value = jsonObj.get("_id");
//			Object getObj = jsonObj.get("inner");
//			System.out.println("id--" + value);
			System.out.println("jsonObj--" + jsonObj);
		}
	}

	public static void testBigDataInsert(int num) throws Exception {
		String dbName = "uuid";
		DB db = mongo.getDB(dbName);
		String collectionName = "uuid";
		System.out.println("Ҫ������������Ϊ---" + num);

		DBCollection table = db.getCollection(collectionName);

		BasicDBObject doc = null;
		long beginTime = System.currentTimeMillis();
		System.out.println("������---" + beginTime);
		for (int i = 0; i < num; i++) {
			doc = new BasicDBObject();
			doc.put("username", "two" + i);
			doc.put("password", "two" + i);
			table.insert(doc);
		}

		long endTime = System.currentTimeMillis();
		System.out.println("������---" + endTime);
		System.out.println("����ʱ----" + (endTime - beginTime) / 1000 + " s "
				+ ((endTime - beginTime) % 1000) + " ms");
	}

	public static void main(String[] args) throws Exception {
		// testSaveStringPair();
		// testSaveOneObj();
		testFindOneObj();

		// testBigDataInsert(1000000);

	}
}