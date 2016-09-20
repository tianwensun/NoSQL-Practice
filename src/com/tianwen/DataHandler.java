package com.tianwen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class DataHandler {

	/**
     * 取得拥有相同朋友的用户对
     * 
     * @param list 每个用户的朋友集合列表
	 */
	public static HashMap<String, Integer> getHaveSameFriendsUserPairs(List<HashMap<String, Object>> list) {

		HashMap<String, Integer> map = new HashMap<String, Integer>();
        for(int i=0;i<list.size()-1;i++)
        {
        	for(int j=i+1;j<list.size();j++)
        	{
        		@SuppressWarnings("unchecked")
				Set<String> set1  = (Set<String>) list.get(i).get("friends");
        		@SuppressWarnings("unchecked")
				Set<String> set2  = (Set<String>) list.get(j).get("friends");
        		//System.out.print("set1["+i+"]:");
        		//System.out.println(set1);
        		//System.out.print("set2["+j+"]::");
        		//System.out.println(set2);
        		
        		Set<String> resultSet = new HashSet<String>();
        		resultSet.addAll(set1);

        		if(resultSet.retainAll(set2) && resultSet.size() > 30)
        		{
        			//System.out.print("resultSet:");
            		//System.out.println(resultSet);
            		
        			String uid1 = (String) list.get(i).get("uid");
        			String uid2 = (String) list.get(j).get("uid");
        			String ukey = uid1+"|"+uid2;
        			
        			//System.out.println(ukey);
        			map.put(ukey, resultSet.size());
        		}
        	}
        }
        
		return map;
	}
	
	/**
     * 取得转发相同微博的用户对及转发相同微博数量
     * 
     * @param list 每个用户转发的微博集合列表
	 */
	public static HashMap<String, Integer> getRepostSameWeibosUserPairs(List<HashMap<String, Object>> list) {

		HashMap<String, Integer> map = new HashMap<String, Integer>();
        for(int i=0;i<list.size()-1;i++)
        {
        	for(int j=i+1;j<list.size();j++)
        	{
        		@SuppressWarnings("unchecked")
				Set<String> set1  = (Set<String>) list.get(i).get("mids");
        		@SuppressWarnings("unchecked")
				Set<String> set2  = (Set<String>) list.get(j).get("mids");
        		//System.out.print("set1["+i+"]:");
        		//System.out.println(set1);
        		//System.out.print("set2["+j+"]::");
        		//System.out.println(set2);
        		
        		Set<String> resultSet = new HashSet<String>();
        		resultSet.addAll(set1);

        		if(resultSet.retainAll(set2) && resultSet.size() > 0)
        		{
        			//System.out.print("resultSet:");
            		//System.out.println(resultSet);
            		
        			String uid1 = (String) list.get(i).get("uid");
        			String uid2 = (String) list.get(j).get("uid");
        			String ukey = uid1+"|"+uid2;
        			//System.out.println(ukey);
        			map.put(ukey, resultSet.size());
        		}
        	}
        }
        
		return map;
	}
	
	/**
     * 取得Top5用户对
     * 
     * @param map 用户对及数量
     * @param order 排序方式
	 */
	public static List<Entry<String, Integer>> getUserPairsTop5(HashMap<String, Integer> map, String order)
	{
		//将map.entrySet()转换成list
		List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				if(order.equals("desc")){
					return o2.getValue().compareTo(o1.getValue()); //降序排序
				} else {
					return o1.getValue().compareTo(o2.getValue()); //顺序排序
				}
				
			}
		});
		
		if(list.size() <= 5) {
			return list;
		} else {
		    return list.subList(0, 5);
		}
	}
	
	/**
     * 倒序排序Map
     * 
     * @param map map数据
	 */
	public static List<Entry<String, String>> orderMapWithDesc(Map<String, String> map)
	{
		List<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String, String>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
			public int compare(Entry<String, String> o1, Entry<String, String> o2) {
				return Integer.valueOf(o2.getValue()).compareTo(Integer.valueOf(o1.getValue())); //降序排序
			}
		});
		
		return list;
	}
}
