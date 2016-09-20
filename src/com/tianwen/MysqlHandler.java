package com.tianwen;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MysqlHandler{

	private static Connection conn = null;
	
	private MysqlHandler() {}
	
	private static class LazyHolder {    
        private static final MysqlHandler INSTANCE = new MysqlHandler();    
    }

    public static final MysqlHandler getInstance() {    
       return LazyHolder.INSTANCE;    
    }
	
	public void connect() {
		String url = "jdbc:mysql://"+Config.MYSQL_HOST+":"+Config.MYSQL_PORT+"/"+Config.MYSQL_DBNAME+"?"
                   + "user="+Config.MYSQL_USER+"&password="+Config.MYSQL_PASSWD+"&useUnicode=true&characterEncoding=UTF8";

	    try {
	        Class.forName("com.mysql.jdbc.Driver");
	        conn = DriverManager.getConnection(url);
	    } catch (SQLException e) {
	        System.out.println("MySQL连接失败");
	        e.printStackTrace();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public void close(){
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
    /**
     * 取得发帖最多的5个用户
	 */
	public List<HashMap<String, Object>> getFaTieUsersTop5() {

		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
       
        try {
            Statement stmt = conn.createStatement();
            String sql = "select uid,count(*) ftnum from weibo group by uid order by ftnum desc limit 5";
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
            	HashMap<String, Object> map = new HashMap<String, Object>();  
                map.put("uid", rs.getString("uid")); 
                map.put("ftnum", rs.getInt("ftnum")); 
                list.add(map);
            }
            stmt.close();
        } catch (SQLException e) {
            System.out.println("MySQL操作错误");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
		return list;
	}
	
	/**
     * 取得转发次数最多的20个帖子
	 */
	public List<HashMap<String, Object>> getZhuanFaWeibosTop20() {

		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
       
        try {
            Statement stmt = conn.createStatement();
            String sql = "select mid,sum(repostsnum) as zfnum from weibo group by mid order by zfnum desc limit 20";
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
            	HashMap<String, Object> map = new HashMap<String, Object>();  
                map.put("mid", rs.getString("mid")); 
                map.put("zfnum", rs.getInt("zfnum")); 
                list.add(map);
            }
            stmt.close();
        } catch (SQLException e) {
            System.out.println("MySQL操作错误");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
		return list;
	}
	
	/**
     * 取得最热门的3个话题
	 */
	public List<HashMap<String, Object>> getHotTopicsTop3() {

		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
       
        try {
            Statement stmt = conn.createStatement();
            String sql = "select topic,sum(commentsnum) as cnum from weibo group by topic order by cnum desc limit 3";
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
            	HashMap<String, Object> map = new HashMap<String, Object>();  
                map.put("topic", rs.getString("topic")); 
                map.put("cnum", rs.getInt("cnum")); 
                list.add(map);
            }
            stmt.close();
        } catch (SQLException e) {
            System.out.println("MySQL操作错误");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
		return list;
	}
	
	/**
     * 取得用户朋友数据
	 */
	public List<HashMap<String, Object>> getUserFriends() {

		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		
        try {
            Statement stmt = conn.createStatement();
            String sql = "SET session group_concat_max_len = 10240;";
            stmt.execute(sql);
            
            sql = "select suid,group_concat(tuid) as friends,count(1) fnum from userrelation group by suid having fnum>50 order by fnum desc";
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                HashMap<String, Object> map = new HashMap<String, Object>();  
                map.put("uid", rs.getString("suid"));
                Set<String> friends = new HashSet<String>();  
                Collections.addAll(friends, rs.getString("friends").split(","));  
                map.put("friends", friends); 
                
                list.add(map);
            }
            stmt.close();
        } catch (SQLException e) {
            System.out.println("MySQL操作错误");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
		return list;
	}
	
	/**
     * 取得用户转发贴数据
	 */
	public List<HashMap<String, Object>> getUserZFWeibos() {

		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		
        try {
            Statement stmt = conn.createStatement();
            String sql = "SET session group_concat_max_len = 20480;";
            stmt.execute(sql);
            
            sql = "select a.uid,group_concat(b.tmid) as mids,count(1) as zfnum from weibo a inner join weiborelation b on a.mid=b.smid group by a.uid having zfnum > 4 order by zfnum desc";
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                HashMap<String, Object> map = new HashMap<String, Object>();  
                map.put("uid", rs.getString("uid"));
                Set<String> mids = new HashSet<String>();  
                Collections.addAll(mids, rs.getString("mids").split(","));  
                map.put("mids", mids); 
                
                list.add(map);
            }
            stmt.close();
        } catch (SQLException e) {
            System.out.println("MySQL操作错误");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
		return list;
	}
	
	/**
     * 取得用户数量
	 */
	public int getUsersCount() {
		int users = 0;
       
        try {
            Statement stmt = conn.createStatement();
            String sql = "select count(1) as users from user";
            ResultSet rs = stmt.executeQuery(sql);
            
            if (rs.next()) {
            	users = rs.getInt("users");
            }
            stmt.close();
        } catch (SQLException e) {
            System.out.println("MySQL操作错误");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
		return users;
	}
	
	/**
     * 分页取得用户信息
     * @param start    开始位置
     * @param pagesize 取得分页大小
	 */
	public List<HashMap<String, Object>> getUsersInfoByPage(int start, int pagesize) {
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
       
        try {
            Statement stmt = conn.createStatement();
            String sql = "select uid,name from user limit "+ start +","+ pagesize;
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
            	HashMap<String, Object> map = new HashMap<String, Object>();  
                map.put("uid", rs.getString("uid")); 
                map.put("name", rs.getString("name")); 
                list.add(map);
            }
            stmt.close();
        } catch (SQLException e) {
            System.out.println("MySQL操作错误");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
		return list;
	}
	
	/**
     * 取得微博信息数量
	 */
	public int getWeibosCount() {
		int weibos = 0;
       
        try {
            Statement stmt = conn.createStatement();
            String sql = "select count(1) as weibos from weibo";
            ResultSet rs = stmt.executeQuery(sql);
            
            if (rs.next()) {
            	weibos = rs.getInt("weibos");
            }
            stmt.close();
        } catch (SQLException e) {
            System.out.println("MySQL操作错误");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
		return weibos;
	}
	
	/**
     * 分页取得微博信息
     * @param start    开始位置
     * @param pagesize 取得分页大小
	 */
	public List<HashMap<String, Object>> getWeibosInfoByPage(int start, int pagesize) {
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
       
        try {
            Statement stmt = conn.createStatement();
            String sql = "select mid,text from weibo limit "+ start +","+ pagesize;
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
            	HashMap<String, Object> map = new HashMap<String, Object>();  
                map.put("mid", rs.getString("mid")); 
                map.put("text", rs.getString("text")); 
                list.add(map);
            }
            stmt.close();
        } catch (SQLException e) {
            System.out.println("MySQL操作错误");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
		return list;
	}
}
