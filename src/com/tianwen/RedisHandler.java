package com.tianwen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import redis.clients.jedis.Jedis;

public class RedisHandler {
	
	private final static String USER_KEY_PREFIX = "U_";
	private final static String WEIBO_KEY_PREFIX = "M_";
	private final static String FATIE_USERS_TOP5 = "FT_USER_TOP5";
	private final static String ZF_WEIBO_TOP20 = "ZF_WEIBO_TOP20";
	private final static String HOT_TOPIC_TOP3 = "HOT_TOPIC_TOP3";
	private final static String HAVE_SAME_FRIENDS_USER_PAIRS_TOP5 = "HSFUP_TOP5";
	private final static String REPOST_SAME_WEIBOS_USER_PAIRS_TOP5 = "RSWUP_TOP5";
	
	
	private static Jedis jedis = null;

    private RedisHandler() {}
	private static class LazyHolder {    
        private static final RedisHandler INSTANCE = new RedisHandler();    
    }
    public static final RedisHandler getInstance() {    
       return LazyHolder.INSTANCE;    
    }
    
    public void connect() {
    	jedis = new Jedis(Config.REDIS_HOST, Config.REDIS_PORT);
	}
    
    public void close(){
		if(jedis != null) {
			jedis = null;
		}
	}
    
	public void loadUsersInfo(List<HashMap<String, Object>> usersInfo) {
		// TODO Auto-generated constructor stub
		
        for(int i=0;i<usersInfo.size();i++)
        {
        	String uid = (String)usersInfo.get(i).get("uid");
        	String name = (String)usersInfo.get(i).get("name");
        	
        	jedis.set(USER_KEY_PREFIX+uid, name);
        	//System.out.println(USER_KEY_PREFIX+":"+uid+"="+name);
        }
	}
	
	public String getUser(String uid) {
        return jedis.get(USER_KEY_PREFIX + uid);
	}
	
	public void loadWeibosInfo(List<HashMap<String, Object>> weibosInfo) {
		// TODO Auto-generated constructor stub
		
        for(int i=0;i<weibosInfo.size();i++)
        {
        	String mid = (String)weibosInfo.get(i).get("mid");
        	String text = (String)weibosInfo.get(i).get("text");
        	
        	jedis.set(WEIBO_KEY_PREFIX+mid, text);
        	//System.out.println(WEIBO_KEY_PREFIX+":"+mid+"="+text);
        }
	}
	
	public String getWeibo(String mid) {
        return jedis.get(WEIBO_KEY_PREFIX + mid);
	}

	public void loadFaTieUsersTop5(List<HashMap<String, Object>> faTieUsersTop5) {
		// TODO Auto-generated method stub
		for(int i=0;i<faTieUsersTop5.size();i++)
        {
        	String uid = (String)faTieUsersTop5.get(i).get("uid");
        	int ftnum = (int)faTieUsersTop5.get(i).get("ftnum");
        	
        	jedis.hset(FATIE_USERS_TOP5, uid, String.valueOf(ftnum));
        	//System.out.println(FATIE_USERS_TOP5+":"+uid+"="+ftnum);
        }
	}
	
	public Map<String, String> getFaTieUsersTop5() {
		return jedis.hgetAll(FATIE_USERS_TOP5);
	}

	public void loadZhuanFaWeibosTop20(List<HashMap<String, Object>> zfWeibosTop20) {
		// TODO Auto-generated method stub
		for(int i=0;i<zfWeibosTop20.size();i++)
        {
        	String mid = (String)zfWeibosTop20.get(i).get("mid");
        	int zfnum = (int)zfWeibosTop20.get(i).get("zfnum");
        	
        	jedis.hset(ZF_WEIBO_TOP20, mid, String.valueOf(zfnum));
        	//System.out.println(ZF_WEIBO_TOP20+":"+mid+"="+zfnum);
        }
	}
	
	public Map<String, String> getZhuanFaWeibosTop20() {
        return jedis.hgetAll(ZF_WEIBO_TOP20);
	}

	public void loadHotTopicsTop3(List<HashMap<String, Object>> hotTopicsTop3) {
		// TODO Auto-generated method stub
		for(int i=0;i<hotTopicsTop3.size();i++)
        {
        	String topic = (String)hotTopicsTop3.get(i).get("topic");
        	int cnum = (int)hotTopicsTop3.get(i).get("cnum");
        	
        	jedis.hset(HOT_TOPIC_TOP3, topic, String.valueOf(cnum));
        	//System.out.println(HOT_TOPIC_TOP3+":"+topic+"="+cnum);
        }
	}
	
	public Map<String, String> getHotTopicsTop3() {
        return jedis.hgetAll(HOT_TOPIC_TOP3);
	}

	public void loadHaveSameFriendsUserPairsTop5(List<Entry<String, Integer>> userPairs) {
		// TODO Auto-generated method stub
		for (Map.Entry<String, Integer> mapping : userPairs) {
			
			String key = mapping.getKey();
        	int value = mapping.getValue();
        	
        	jedis.hset(HAVE_SAME_FRIENDS_USER_PAIRS_TOP5, key, String.valueOf(value));
        	//System.out.println(HAVE_SAME_FRIENDS_USER_PAIRS_TOP5+":"+key+"="+value);
		}
	}
	
	public Map<String, String> getHaveSameFriendsUserPairsTop5() {
        return jedis.hgetAll(HAVE_SAME_FRIENDS_USER_PAIRS_TOP5);
	}

	public void loadRepostSameWeibosUserPairsTop5(List<Entry<String, Integer>> userPairs) {
		// TODO Auto-generated method stub
        for (Map.Entry<String, Integer> mapping : userPairs) {
			
			String key = mapping.getKey();
        	int value = mapping.getValue();
        	
        	jedis.hset(REPOST_SAME_WEIBOS_USER_PAIRS_TOP5, key, String.valueOf(value));
        	//System.out.println(REPOST_SAME_WEIBOS_USER_PAIRS_TOP5+":"+key+"="+value);
		}
	}
	
	public Map<String, String> getRepostSameWeibosUserPairsTop5() {
		return jedis.hgetAll(REPOST_SAME_WEIBOS_USER_PAIRS_TOP5);
	}
}
