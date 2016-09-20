/**
 * 
 */
package com.tianwen;

import java.util.HashMap;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

/**
 * @author tianwen
 *
 */
public class EconomicSituation {

	/**
	 * 
	 */
	public static HashMap<String, Float> getEconomicSituation(String host, int port, String dbName, String date) {
		// TODO Auto-generated constructor stub
		HashMap<String, Float> ret = new HashMap<String, Float>();
		
		try{
			@SuppressWarnings("deprecation")
			Mongo mongo = new Mongo(host, port);
	        
			@SuppressWarnings("deprecation")
			DB db = mongo.getDB(dbName);
			
	        DBCollection coll = db.getCollection("CPI");
	        BasicDBObject docFind = new BasicDBObject("periodDate", date).append("indicID", "M030000021");
	        DBObject findResult = coll.findOne(docFind);
	        Float cpi = Float.parseFloat((String) findResult.get("dataValue"));
	        ret.put("CPI", cpi);
	        
	        coll = db.getCollection("PMI");
	        docFind = new BasicDBObject("periodDate", date).append("indicID", "M020000014");
	        findResult = coll.findOne(docFind);
	        Float pmi = Float.parseFloat((String) findResult.get("dataValue"));
	        ret.put("PMI", pmi);
	        
	        coll = db.getCollection("美国就业与失业");
	        docFind = new BasicDBObject("periodDate", date).append("indicID", "G010000069");
	        findResult = coll.findOne(docFind);
	        Float 失业率 = Float.parseFloat((String) findResult.get("dataValue"));
	        ret.put("失业率", 失业率);
	        
			mongo.close();
		} catch (Exception e) {  
            e.printStackTrace();  
        }
		
		return ret;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String host = Config.MONGODB_HOST;
		int port = Config.MONGODB_PORT;
		String dbName = Config.MONGODB_DBNAME;
		String date = "2014-08-31"; 
		
		HashMap<String, Float> ret = getEconomicSituation(host, port, dbName, date);
		
		
		Float cpi = ret.get("CPI");
		System.out.print("中国经济CPI为"+cpi+",");
        if(cpi > 5){
        	System.out.print("严重通胀");
        } else if(cpi > 3) {
        	System.out.print("通胀");
        } else {
        	System.out.print("不通胀");
        }
        
        Float pmi = ret.get("PMI");
        System.out.print("，PMI为"+pmi+"，趋于");
        if(pmi > 50){
        	System.out.print("上升");
        } else {
        	System.out.print("下降");
        }
        System.out.println("。");
        
        Float 失业率 = ret.get("失业率");
        System.out.print("美国失业率是"+失业率+"%，");
        if(失业率 > 5){
        	System.out.print("偏高");
        } else if(失业率 > 4){
        	System.out.print("略高");
        } else {
        	System.out.print("正常");
        }
        System.out.println("。");
	}

}
