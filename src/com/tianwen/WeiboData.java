package com.tianwen;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class WeiboData {


	public static void main(String[] args) {
		
		System.out.println("开始输出微博数据分析结果...");
		System.out.println();

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

	    /*Iterator<Map.Entry<String, String>> userEntries = users.entrySet().iterator(); 
	    while (userEntries.hasNext()) { 
	        Map.Entry<String, String> entry = userEntries.next();  
	      
	        String uid = entry.getKey();
	        String name = rh.getUser(uid);
	        String ftnum = entry.getValue();
	        
	        System.out.println(uid+"\t"+name+"\t"+ftnum);
	    } */
	    System.out.println();
	    
	    //取得转发最多的微博TOP20
	    Map<String, String> weibos = rh.getZhuanFaWeibosTop20();
	    List<Entry<String, String>> newWeibos = DataHandler.orderMapWithDesc(weibos);
	    System.out.println("转发最多的微博TOP20如下：");
	    System.out.println("微博ID\t微博内容\t转发次数");
	    for (Entry<String, String> mapping : newWeibos) {
			String mid = mapping.getKey();
			String text = rh.getWeibo(mid);
	        String rpnum = mapping.getValue();
	        
	        System.out.println(mid+"\t"+text+"\t"+rpnum);  
		}
	    /*Iterator<Map.Entry<String, String>> weiboEntries = weibos.entrySet().iterator();  
	    while (weiboEntries.hasNext()) { 
	        Map.Entry<String, String> entry = weiboEntries.next();  
	      
	        String mid = entry.getKey();
	        String text = rh.getWeibo(mid);
	        String rpnum = entry.getValue();
	        
	        System.out.println(mid+"\t"+text+"\t"+rpnum);  
	    }*/
	    System.out.println();
	    
	    //取得最热门主题TOP3
	    Map<String, String> topics = rh.getHotTopicsTop3();
	    List<Entry<String, String>> newTopics = DataHandler.orderMapWithDesc(topics);
	    System.out.println("最热门主题TOP3如下：");
	    System.out.println("主题\t评论数");
	    for (Entry<String, String> mapping : newTopics) {
			String topic = mapping.getKey();
	        String cnum = mapping.getValue();
	        
	        System.out.println(topic+"\t"+cnum);  
		}
	    
	    /*Iterator<Map.Entry<String, String>> topicEntries = topics.entrySet().iterator();  
	    while (topicEntries.hasNext()) { 
	        Map.Entry<String, String> entry = topicEntries.next();  
	      
	        String topic = entry.getKey();
	        String cnum = entry.getValue();
	        
	        System.out.println(topic+"\t"+cnum);  
	    }*/
	    System.out.println();

	    //取得拥有相同朋友最多的用户对TOP5
	    Map<String, String> userPairs = rh.getHaveSameFriendsUserPairsTop5();
	    List<Entry<String, String>> newUserPairs = DataHandler.orderMapWithDesc(userPairs);
	    System.out.println("拥有相同朋友最多的用户对TOP5如下：");
	    System.out.println("用户1\t用户2\t拥有相同朋友数量");
	    for (Entry<String, String> mapping : newUserPairs) {
	        String userPair = mapping.getKey();
	        String[] userIds = userPair.split("\\|");
	        String user1 = rh.getUser(userIds[0]);
	        String user2 = rh.getUser(userIds[1]);
	        String fnum = mapping.getValue();
	        
	        System.out.println(user1+"\t"+user2+"\t"+fnum); 
		}
	    /*Iterator<Map.Entry<String, String>> userPairsEntries = userPairs.entrySet().iterator();  
	    while (userPairsEntries.hasNext()) { 
	        Map.Entry<String, String> entry = userPairsEntries.next();  
	      
	        String userPair = entry.getKey();
	        String[] userIds = userPair.split("\\|");
	        String user1 = rh.getUser(userIds[0]);
	        String user2 = rh.getUser(userIds[1]);
	        String fnum = entry.getValue();
	        
	        System.out.println(user1+"\t"+user2+"\t"+fnum);  
	    }*/
	    System.out.println();
	    
	    //取得转发相同微博最多的用户对TOP5
	    Map<String, String> userPairs1 = rh.getRepostSameWeibosUserPairsTop5();
	    List<Entry<String, String>> newUserPairs1 = DataHandler.orderMapWithDesc(userPairs1);
	    System.out.println("转发相同微博最多的用户对TOP5如下：");
	    System.out.println("用户1\t用户2\t转发相同微博数量");
	    for (Entry<String, String> mapping : newUserPairs1) {
	        String userPair = mapping.getKey();
	        String[] userIds = userPair.split("\\|");
	        String user1 = rh.getUser(userIds[0]);
	        String user2 = rh.getUser(userIds[1]);
	        String num = mapping.getValue();
	        
	        System.out.println(user1+"\t"+user2+"\t"+num); 
		}
	    /*Iterator<Map.Entry<String, String>> userPairs1Entries = userPairs1.entrySet().iterator();
	    while (userPairs1Entries.hasNext()) { 
	        Map.Entry<String, String> entry = userPairs1Entries.next();  
	      
	        String userPair = entry.getKey();
	        String[] userIds = userPair.split("\\|");
	        String user1 = rh.getUser(userIds[0]);
	        String user2 = rh.getUser(userIds[1]);
	        String num = entry.getValue();
	        
	        System.out.println(user1+"\t"+user2+"\t"+num); 
	    }*/
	    System.out.println("输出结束。");

		rh.close();
	}

}
