package com.tianwen;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class NoSQL {

	public NoSQL() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String act  = "";
		String date = "";
		String dir  = "";

		for(int i=0; i < args.length; i=i+2)
		{
			if("-a".equals(args[i]) && i+1 < args.length)
			{
				act = String.valueOf(args[i+1]);
				
				String[] actions = {"ImportDataToMongodb","ImportWeiboDataToRedis","EconomicSituation","FaTieUsersTop5","ZhuanFaWeibosTop20","HotTopicsTop3","HaveSameFriendsUserPairsTop5","RepostSameWeibosUserPairsTop5"};
				if(!Arrays.asList(actions).contains(act)){
					System.out.println("操作不存在，请重新输入！");
					System.out.println("参考帮助：java -jar NoSQL.jar -h");
					System.exit(0);
				}
			}
			else if("-d".equals(args[i]) && i+1 < args.length)
			{
				date = String.valueOf(args[i+1]);
				if(date.equals("")){
					System.out.println("日期为空，请输入！");
					System.out.println("参考帮助：java -jar NoSQL.jar -h");
					System.exit(0);
				}
				
				if(!date.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}")){
					System.out.println("日期格式不正确，请重新输入！");
					System.out.println("参考帮助：java -jar NoSQL.jar -h");
					System.exit(0);
				}
			}
			else if("-p".equals(args[i]) && i+1 < args.length)
			{
				dir = String.valueOf(args[i+1]);
				if(dir.equals("")){
					System.out.println("数据目录未输入！");
					System.out.println("参考帮助：java -jar NoSQL.jar -h");
					System.exit(0);
				}
				File file = new File(dir);
				if(!file.exists()){
					System.out.println("指定的数据目录无效，请重新输入！");
					System.out.println("参考帮助：java -jar NoSQL.jar -h");
					System.exit(0);
				}
			}
			else if("-h".equals(args[i]))
			{
				System.out.println("NoSQL作业程序1.0版本");
				System.out.println("-a\t行为，支持以下三种：");
				System.out.println("\tImportDataToMongodb 导入数据到MongoDB");
				System.out.println("\tImportWeiboDataToRedis 导入微博数据到Redis");
				System.out.println("\tEconomicSituation 显示指定日期的经济情况");
				System.out.println("\tFaTieUsersTop5 输出发帖最多的5个用户");
				System.out.println("\tZhuanFaWeibosTop20 输出转发次数最多的20个微博");
				System.out.println("\tHotTopicsTop3 输出最热门的3个主题");
				System.out.println("\tHaveSameFriendsUserPairsTop5 输出具有相同朋友最多的5对用户");
				System.out.println("\tRepostSameWeibosUserPairsTop5 输出转发过相同微博最多的5对用户");
				System.out.println("-d\t日期，查询经济情况的日期，格式：yyyy-mm-dd");
				System.out.println("-p\t数据目录，需要导入Mongodb的数据目录，只会读取其中.csv文件，每个文件名生成一个collection");
				System.out.println("Example:");
				System.out.println("将csv数据导入mongodb：\n\tjava -jar NoSQL.jar -a ImportDataToMongodb -p /home/tianwen/Documents/bigdata");
				System.out.println("将MySQL中的微博数据导入到Redis中：\n\tjava -jar NoSQL.jar -a ImportWeiboDataToRedis");
				System.out.println("显示指定日期的经济情况：\n\tjava -jar NoSQL.jar -a EconomicSituation -d 2014-08-31");
				System.out.println("输出发帖最多的5个用户：\n\tjava -jar NoSQL.jar -a FaTieUsersTop5");
				System.exit(0);
			}
			else
			{
				System.out.println("输入参数错误！");
				System.out.println("参考帮助：java -jar NoSQL.jar -h");
				System.exit(0);
			}
		}
	    
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		switch(act){
		case "ImportDataToMongodb":
			System.out.println(df.format(new Date()) + " 导入数据开始");
			
			String dirPath = Config.DATA_DIR; 
			if(!"".equals(dir)){
				dirPath = dir;
			}
			ImportDataToMongodb.importFilesToMongo(Config.MONGODB_HOST, Config.MONGODB_PORT, Config.MONGODB_DBNAME, dirPath);
			
			System.out.println(df.format(new Date()) + " 导入数据完成");
			break;
		case "ImportWeiboDataToRedis":
			
			System.out.println(df.format(new Date()) + " 导入数据开始");
			
			DataLoader dl = new DataLoader();
			dl.loadAllData();
			
			System.out.println(df.format(new Date()) + " 导入数据完成");
			break;
		case "EconomicSituation":
			
			String strDate = "2014-08-31";
			if(!date.equals("")){
				strDate = date;
			}
			
			HashMap<String, Float> ret = EconomicSituation.getEconomicSituation(Config.MONGODB_HOST, Config.MONGODB_PORT, Config.MONGODB_DBNAME, strDate);
			System.out.println(df.format(new Date()) + " 输出结果：");
			
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
	        System.out.print("，环比");
	        
	        Float 失业环比变化 = ret.get("失业环比变化");
	        if(失业环比变化 == 0){
	        	System.out.println("无变化。");
	        }else{
		        DecimalFormat decimalFormat=new DecimalFormat(".00");//小数保留2位,不足用0补足.
		        if(失业环比变化>0){
		        	System.out.print("上涨"+decimalFormat.format(失业环比变化)+"%，");
		        }else{
		        	System.out.print("下降"+decimalFormat.format(Math.abs(失业环比变化))+"%，");
		        }
		        if(Math.abs(失业环比变化) > 2){
		        	System.out.print("变化较快");
		        }else{
		        	System.out.print("变化缓慢");
		        }
		        System.out.println("。");
	        }
			break;
		case "FaTieUsersTop5":
			RedisHandler rh = RedisHandler.getInstance();
			rh.connect();
			
			//取得发帖最多用户Top5
			Map<String, String> users = rh.getFaTieUsersTop5();
			List<Entry<String, String>> newUsers = DataHandler.orderMapWithDesc(users);
			System.out.println("发帖最多用户TOP5如下：");
		    System.out.println("用户ID\t用户名\t发帖数量");
	        for (Entry<String, String> mapping : newUsers) {
				String uid = mapping.getKey();
				String name = rh.getUser(uid);
				String ftnum = mapping.getValue();
	        	System.out.println(uid+"\t"+name+"\t"+ftnum);
			}
	        
	        rh.close();
			break;
		case "ZhuanFaWeibosTop20":
			RedisHandler rh1 = RedisHandler.getInstance();
			rh1.connect();
			
			Map<String, String> weibos = rh1.getZhuanFaWeibosTop20();
		    List<Entry<String, String>> newWeibos = DataHandler.orderMapWithDesc(weibos);
		    System.out.println("转发最多的微博TOP20如下：");
		    System.out.println("微博ID\t微博内容\t转发次数");
		    for (Entry<String, String> mapping : newWeibos) {
				String mid = mapping.getKey();
				String text = rh1.getWeibo(mid);
		        String rpnum = mapping.getValue();
		        
		        System.out.println(mid+"\t"+text+"\t"+rpnum);  
			}
		    
		    rh1.close();
			break;
		case "HotTopicsTop3":
			RedisHandler rh2 = RedisHandler.getInstance();
			rh2.connect();
			
			Map<String, String> topics = rh2.getHotTopicsTop3();
		    List<Entry<String, String>> newTopics = DataHandler.orderMapWithDesc(topics);
		    System.out.println("最热门主题TOP3如下：");
		    System.out.println("主题\t评论数");
		    for (Entry<String, String> mapping : newTopics) {
				String topic = mapping.getKey();
		        String cnum = mapping.getValue();
		        
		        System.out.println(topic+"\t"+cnum);  
			}
		    
		    rh2.close();
			break;
		case "HaveSameFriendsUserPairsTop5":
			RedisHandler rh3 = RedisHandler.getInstance();
			rh3.connect();
			
			Map<String, String> userPairs = rh3.getHaveSameFriendsUserPairsTop5();
		    List<Entry<String, String>> newUserPairs = DataHandler.orderMapWithDesc(userPairs);
		    System.out.println("拥有相同朋友最多的用户对TOP5如下：");
		    System.out.println("用户1\t用户2\t拥有相同朋友数量");
		    for (Entry<String, String> mapping : newUserPairs) {
		        String userPair = mapping.getKey();
		        String[] userIds = userPair.split("\\|");
		        String user1 = rh3.getUser(userIds[0]);
		        String user2 = rh3.getUser(userIds[1]);
		        String fnum = mapping.getValue();
		        
		        System.out.println(user1+"\t"+user2+"\t"+fnum); 
			}
		    
		    rh3.close();
			break;
		case "RepostSameWeibosUserPairsTop5":
			RedisHandler rh4 = RedisHandler.getInstance();
			rh4.connect();
			
			Map<String, String> userPairs1 = rh4.getRepostSameWeibosUserPairsTop5();
		    List<Entry<String, String>> newUserPairs1 = DataHandler.orderMapWithDesc(userPairs1);
		    System.out.println("转发相同微博最多的用户对TOP5如下：");
		    System.out.println("用户1\t用户2\t转发相同微博数量");
		    for (Entry<String, String> mapping : newUserPairs1) {
		        String userPair = mapping.getKey();
		        String[] userIds = userPair.split("\\|");
		        String user1 = rh4.getUser(userIds[0]);
		        String user2 = rh4.getUser(userIds[1]);
		        String num = mapping.getValue();
		        
		        System.out.println(user1+"\t"+user2+"\t"+num); 
			}
		    
		    rh4.close();
			break;
		}
	}

}
