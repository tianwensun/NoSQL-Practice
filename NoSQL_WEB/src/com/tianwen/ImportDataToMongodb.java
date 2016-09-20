package com.tianwen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Set;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

/**
 * @author tianwen
 *
 */
public class ImportDataToMongodb {

	/**
	 * 导入指定文件数据到mongodb表中
	 */
	public static void ImportCPI(DBCollection coll,String dataFile) {
		try {
			FileInputStream in = new FileInputStream(dataFile);  
			byte[] b = new byte[3];  
			in.read(b);  
			String code = "GBK";  
			if (b[0] == -17 && b[1] == -69 && b[2] == -65){  
			    code = "UTF-8";  
			}  
			  
			InputStreamReader inputStreamReader = new InputStreamReader(in,code);
			BufferedReader reader = new BufferedReader(inputStreamReader);
			
            String firstLine = reader.readLine(); //第一行信息，为英文标题，可以作为数据项的键 
            String keys[] = firstLine.split(",");
            
            reader.readLine();//第二行信息，为标题信息，不用,如果需要，注释掉 
            
            String line = null;
            while((line = reader.readLine()) != null) {
            	String item[] = line.split(",");
                //System.out.println(line);
                
                BasicDBObject doc = null;
                for(int i=0;i<item.length;i++) {
                	if(i == 0){
                		doc = new BasicDBObject("indicID", item[i]);
                	} else {
                	    doc.append(keys[i], String.valueOf(item[i]));
                	}
                }
                coll.insert(doc);
            }  
            
            in.close();
            reader.close();
        } catch (Exception e) {  
            e.printStackTrace();  
        }
	}
	
	public static void importFilesToMongo(String host, int port, String dbName, String dirPath)
	{
		// TODO Auto-generated method stub
		try{
			@SuppressWarnings("deprecation")
			Mongo mongo = new Mongo(host, port);
	        
			@SuppressWarnings("deprecation")
			DB db = mongo.getDB(dbName);
			
			// 遍历所有集合的名字
	        Set<String> colls = db.getCollectionNames();
	        for (String s : colls) {
	            // 先删除所有Collection(类似于关系数据库中的"表")
	            if (!s.equals("system.indexes")) {
	                db.getCollection(s).drop();
	            }
	        }
	        
	        File dir = new File(dirPath);
	        String[] fileList = dir.list();
	        for (int i = 0; i < fileList.length; i++) {
	        	String filePath = dirPath +"/" + fileList[i];
	            File file = new File(filePath);
	            String fileName = file.getName();
	            String temp[] = fileName.split("\\.");
	            if(temp.length != 2 || !temp[1].equals("csv"))
	            {
	            	continue;
	            }
	            fileName = temp[0];

	            // 取得集合CPI(若：CPI不存在，mongodb将自动创建该集合)
	            DBCollection coll = db.getCollection(fileName);

	            // delete all
	            DBCursor dbCursor = coll.find();
	            for (DBObject dbObject : dbCursor) {
	                coll.remove(dbObject);
	            }
	                
	            //String CPI = "/home/tianwen/Documents/bigdata/CPI.csv";
	            ImportCPI(coll, filePath);
	        }
	        
	        mongo.close();
		} catch (Exception e) {  
            e.printStackTrace();  
        }
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String host = Config.MONGODB_HOST;
		int port = Config.MONGODB_PORT;
		String dbName = Config.MONGODB_DBNAME;
		String dirPath = Config.DATA_DIR; 
		
		importFilesToMongo(host, port, dbName, dirPath);
	}
}
