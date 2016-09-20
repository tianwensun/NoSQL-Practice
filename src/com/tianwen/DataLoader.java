package com.tianwen;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class DataLoader {
	MysqlHandler mysqlHandler = null;
	RedisHandler rh = null;
	public DataLoader() {
		mysqlHandler = MysqlHandler.getInstance();
		rh = RedisHandler.getInstance();
	}
	
	public void loadAllData() {
		// TODO Auto-generated method stub
		mysqlHandler.connect();
		rh.connect();
		
		//导入用户信息
		this.loadUsersInfo();
		
		//导入微博信息
		this.loadWeibosInfo();
		
		//导入发帖最多的5个用户
		this.loadFaTieUsersTop5();
		
		//导入抓发微博最多的20个用户
		this.loadZhuanFaWeibosTop20();
		
		//导入最热门的3个主题
		this.loadHotTopicsTop3();
		
		//导入拥有相同朋友最多的5对用户
		this.loadHaveSameFriendsUserPairs();
		
		//导入转发相同帖子最多的5对用户
		this.loadRepostSameWeibosUserPairs();
		
	    mysqlHandler.close();
	    rh.close();
	}

	public void loadUsersInfo() {
		int userCount = mysqlHandler.getUsersCount();
		int page = 1;
		int pagesize = 100;
		int pageCount = (int) Math.ceil(userCount / pagesize);
		int start = 0;
		
		List<HashMap<String, Object>> usersInfo = null;
		while(page < pageCount) { 
			start = (page - 1) * pagesize;
			
			usersInfo = mysqlHandler.getUsersInfoByPage(start, pagesize);
			rh.loadUsersInfo(usersInfo);
			
			page++;
		}
	}

	public void loadWeibosInfo() {
		int weiboCount = mysqlHandler.getWeibosCount();
		int page = 1;
		int pagesize = 100;
		int pageCount = (int) Math.ceil(weiboCount / pagesize);
		int start = 0;
		
		List<HashMap<String, Object>> weibosInfo = null;
		while(page < pageCount) { 
			start = (page - 1) * pagesize;
			
			weibosInfo = mysqlHandler.getWeibosInfoByPage(start, pagesize);
			rh.loadWeibosInfo(weibosInfo);
			
			page++;
		}
	}
	
	public void loadFaTieUsersTop5() {
		List<HashMap<String, Object>> atUsersTop5 = mysqlHandler.getFaTieUsersTop5();
		rh.loadFaTieUsersTop5(atUsersTop5);
	}

	public void loadZhuanFaWeibosTop20() {
		List<HashMap<String, Object>> zfWeibosTop20 = mysqlHandler.getZhuanFaWeibosTop20();
		rh.loadZhuanFaWeibosTop20(zfWeibosTop20);
	}

	public void loadHotTopicsTop3() {
		List<HashMap<String, Object>> hotTopicsTop3 = mysqlHandler.getHotTopicsTop3();
		rh.loadHotTopicsTop3(hotTopicsTop3);
	}

	public void loadHaveSameFriendsUserPairs() {
		List<HashMap<String, Object>> list = mysqlHandler.getUserFriends();
		HashMap<String, Integer> map = DataHandler.getHaveSameFriendsUserPairs(list);
		List<Entry<String, Integer>> userPairs = DataHandler.getUserPairsTop5(map, "desc");
		rh.loadHaveSameFriendsUserPairsTop5(userPairs);
	}

	public void loadRepostSameWeibosUserPairs() {
		List<HashMap<String, Object>> list = mysqlHandler.getUserZFWeibos();
		HashMap<String, Integer> map = DataHandler.getRepostSameWeibosUserPairs(list);
		List<Entry<String, Integer>> userPairs = DataHandler.getUserPairsTop5(map, "desc");
		rh.loadRepostSameWeibosUserPairsTop5(userPairs);
	}
}
