package com.tianwen;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class FormHandle
 */
@WebServlet("/FormHandle")
public class FormHandle extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public FormHandle() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
        res.setContentType("text/json; charset=UTF-8");

	    PrintWriter out = res.getWriter();
	    
	    String act = req.getParameter("act");
	    if(act.equals("EconomicSituation")){
	    	
	    	String date = req.getParameter("date");
	    	if(date == null){
	    		JsonObject jsonOut = Json.createObjectBuilder()
		        		.add("error", "1")
		        	    .add("msg", "参数错误")
		        	    .build();
		    	out.print(jsonOut.toString());
	    	} else {
		    	String host = Config.MONGODB_HOST;
				int port = Config.MONGODB_PORT;
				String dbName = Config.MONGODB_DBNAME;
				
				HashMap<String, Float> ret = EconomicSituation.getEconomicSituation(host, port, dbName, date);
				Float cpi = ret.get("CPI");
		        Float pmi = ret.get("PMI");
		        Float 失业率 = ret.get("失业率");
		        
		        JsonObject jsonOut = Json.createObjectBuilder()
		        		.add("error", "0")
		        	    .add("msg", "")
		        	    .add("data", Json.createObjectBuilder()
				        		.add("cpi", String.valueOf(cpi))
				        	    .add("pmi", String.valueOf(pmi))
				        	    .add("失业率", String.valueOf(失业率)))
		        	    .build();
		    	out.print(jsonOut.toString());
	    	}
	    } else if(act.equals("FaTieUsersTop5")){
	    	RedisHandler rh = RedisHandler.getInstance();
			rh.connect();
			JsonArrayBuilder jsonData = Json.createArrayBuilder();
			
			Map<String, String> users = rh.getFaTieUsersTop5();
			List<Entry<String, String>> newUsers = DataHandler.orderMapWithDesc(users);
	        for (Entry<String, String> mapping : newUsers) {
				String uid = mapping.getKey();
				String name = rh.getUser(uid);
				String ftnum = mapping.getValue();
				
				JsonObjectBuilder item = Json.createObjectBuilder();
		        item.add("uid", uid);
		        item.add("name", name);
		        item.add("num", ftnum);
		        jsonData.add(item);
			}
	        
	        JsonObject jsonOut = Json.createObjectBuilder()
	        		.add("error", "0")
	        	    .add("msg", "")
	        	    .add("data",jsonData)
	        	    .build();
	        
			out.print(jsonOut.toString());
	    	rh.close();
	    } else if(act.equals("ZhuanFaWeibosTop20")){
	    	RedisHandler rh = RedisHandler.getInstance();
			rh.connect();
			JsonArrayBuilder jsonData = Json.createArrayBuilder();
			
			Map<String, String> weibos = rh.getZhuanFaWeibosTop20();
		    List<Entry<String, String>> newWeibos = DataHandler.orderMapWithDesc(weibos);
		    for (Entry<String, String> mapping : newWeibos) {
				String mid = mapping.getKey();
				String text = rh.getWeibo(mid);
		        String rpnum = mapping.getValue();
		        
		        JsonObjectBuilder item = Json.createObjectBuilder();
		        item.add("mid", mid);
		        item.add("text", text);
		        item.add("num", rpnum);
		        jsonData.add(item);
			}
		    
		    JsonObject jsonOut = Json.createObjectBuilder()
	        		.add("error", "0")
	        	    .add("msg", "")
	        	    .add("data",jsonData)
	        	    .build();
			out.print(jsonOut.toString());
	    	rh.close();
	    } else if(act.equals("HotTopicsTop3")){
	    	RedisHandler rh = RedisHandler.getInstance();
			rh.connect();
			JsonArrayBuilder jsonData = Json.createArrayBuilder();
			
			Map<String, String> topics = rh.getHotTopicsTop3();
		    List<Entry<String, String>> newTopics = DataHandler.orderMapWithDesc(topics);
		    for (Entry<String, String> mapping : newTopics) {
				String topic = mapping.getKey();
		        String cnum = mapping.getValue();
		        
		        JsonObjectBuilder item = Json.createObjectBuilder();
		        item.add("topic", topic);
		        item.add("num", cnum);
		        jsonData.add(item);
			}
		    
		    JsonObject jsonOut = Json.createObjectBuilder()
	        		.add("error", "0")
	        	    .add("msg", "")
	        	    .add("data",jsonData)
	        	    .build();
			out.print(jsonOut.toString());
	    	rh.close();
	    } else if(act.equals("HaveSameFriendsUserPairsTop5")){
	    	RedisHandler rh = RedisHandler.getInstance();
			rh.connect();
			JsonArrayBuilder jsonData = Json.createArrayBuilder();
			
			Map<String, String> userPairs = rh.getHaveSameFriendsUserPairsTop5();
		    List<Entry<String, String>> newUserPairs = DataHandler.orderMapWithDesc(userPairs);
		    for (Entry<String, String> mapping : newUserPairs) {
		        String userPair = mapping.getKey();
		        String[] userIds = userPair.split("\\|");
		        String user1 = rh.getUser(userIds[0]);
		        String user2 = rh.getUser(userIds[1]);
		        String fnum = mapping.getValue();
		        
		        JsonObjectBuilder item = Json.createObjectBuilder();
		        item.add("user1", user1);
		        item.add("user2", user2);
		        item.add("num", fnum);
		        jsonData.add(item);
			}
			
		    JsonObject jsonOut = Json.createObjectBuilder()
	        		.add("error", "0")
	        	    .add("msg", "")
	        	    .add("data",jsonData)
	        	    .build();
			out.print(jsonOut.toString());
	    	rh.close();
	    } else if(act.equals("RepostSameWeibosUserPairsTop5")){
	    	RedisHandler rh = RedisHandler.getInstance();
			rh.connect();
			JsonArrayBuilder jsonData = Json.createArrayBuilder();
			
	    	Map<String, String> userPairs = rh.getRepostSameWeibosUserPairsTop5();
		    List<Entry<String, String>> newUserPairs = DataHandler.orderMapWithDesc(userPairs);
		    for (Entry<String, String> mapping : newUserPairs) {
		        String userPair = mapping.getKey();
		        String[] userIds = userPair.split("\\|");
		        String user1 = rh.getUser(userIds[0]);
		        String user2 = rh.getUser(userIds[1]);
		        String num = mapping.getValue();
		        
		        JsonObjectBuilder item = Json.createObjectBuilder();
		        item.add("user1", user1);
		        item.add("user2", user2);
		        item.add("num", num);
		        jsonData.add(item);
			}
	    	
		    JsonObject jsonOut = Json.createObjectBuilder()
	        		.add("error", "0")
	        	    .add("msg", "")
	        	    .add("data",jsonData)
	        	    .build();
			out.print(jsonOut.toString());
	    	rh.close();
	    } else {
	    	JsonObject jsonOut = Json.createObjectBuilder()
	        		.add("error", "1")
	        	    .add("msg", "参数错误")
	        	    .build();
	    	out.print(jsonOut.toString());
	    }
	    
	    out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	/*public void service(HttpServletRequest req, HttpServletResponse res) throws IOException{
	    res.setContentType("text/html");
	    
	    PrintWriter out = res.getWriter();
	    
	    Enumeration flds = req.getParameterNames();
	    
	    if(!flds.hasMoreElements()){
	        
	        out.print("<html>");
	        out.print("<form method=\"POST\"" +"action=\"FormHandle\">");
	        for(int i = 0; i < 10; i++)
	            out.print("<b>Field" + i + "</b> " +
	                    "<input type=\"text\""+" size=\"20\" name=\"Field"
	                    + i + "\" value=\"Value" + i + "\"><br>");
	        out.print("<INPUT TYPE=submit name=submit Value=\"Submit\"></form></html>");
	    
	    }else{
	        
	        out.print("<h1>Your form contained:</h1>");
	        
	        while(flds.hasMoreElements()){
	            String field= (String)flds.nextElement();
	            String value= req.getParameter(field);
	            out.print(field + " = " + value+ "<br>");
	        }
	    }
	    
	    out.close();
	    
	}*/

}
