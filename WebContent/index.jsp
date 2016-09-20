<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>NoSQL作业结果查询</title>
<script type="text/javascript" src="js/jquery-1.11.1.min.js"></script>
</head>
<body>
<center>
<table width="1000px" style="background-color:aliceblue;">
  <tr height="20px"><td align="left" style="background-color:aquamarine;">经济情况查询：</td></tr>
  <tr>
    <td>
      <form action="#" name="form1">
      <table>
        <tr>
          <td>日期：
            <input type="text" id="data" name="date" size="20" value="2014-08-31"/>
            <input type="button" id="btn1" name="btn1" value="查询"/>
          </td>
        </tr>
        <tr>
          <td>
            <div id="economicSituation" style="width:1000px;height;100px;background-color:beige">
              无数据
            </div>
          </td>
        </tr>
      </table>
      </form>
    </td>
  </tr>
  <tr height="50px"><td>&nbsp;</td></tr>
  <tr height="20px"><td align="left" style="background-color:aquamarine;">微博数据查询：</td></tr>
  <tr>
    <td>
      <form action="#" name="form2">
      <table>
        <tr>
          <td>数据项：
            <select id="act" name="act">
              <option value="">请选择查询数据项...</option>
              <option value="FaTieUsersTop5">发帖最多的5个用户</option>
              <option value="ZhuanFaWeibosTop20">转发次数最多的20个微博</option>
              <option value="HotTopicsTop3">最热门的3个主题</option>
              <option value="HaveSameFriendsUserPairsTop5">拥有相同朋友最多的5对用户</option>
              <option value="RepostSameWeibosUserPairsTop5">转发相同的微博最多的5对用户</option>
            </select>
            <input type="button" id="btn2" name="btn2" value="查询"/>
          </td>
        </tr>
        <tr>
          <td>
            <div id="weiboData" style="width:1000px;height;100px;background-color:beige">
              无数据
            </div>
          </td>
        </tr>
      </table>
      </form>
    </td>
  </tr>
</table>
</center>
<script type="text/javascript">
$("#btn1").click(function(){
    var date = $("#data").val();

    if(date == ""){
        alert("请输入查询日期!");
    }else{
        //发送查询请求
    	$.getJSON("form.do?act=EconomicSituation&date="+date, function(req) {
    		console.log(req);
    		if(req.error == 1){
    			alert(req.msg);
    		} else {
    			var result = "中国经济CPI为" + req.data.cpi + "，<font color='red'>";
    			if(req.data.cpi > 5){
    				result = result + "严重通胀";
    			} else if(req.data.cpi > 3) {
    		     	result = result + "通胀";
    		    } else {
    		        result = result + "不通胀";
    		    }
    			result = result + "</font>，PMI为" + req.data.pmi + "，趋于<font color='red'>";
    			
    			if(req.data.pmi > 50) {
    				result = result + "上升";
    			} else {
    				result = result + "下降";
    		    }
    			result = result + "</font>。<br>";
    			
    			result = result + "美国失业率是"+ req.data.失业率 +"%，<font color='red'>";
    			if(req.data.失业率 > 5) {
    				result = result + "偏高";
    			} else if(req.data.失业率 > 4){
    				result = result + "略高";
    		    } else {
    		    	result = result + "正常";
    		    }
    			result = result + "</font>，环比";
    			
    	        if(req.data.失业环比变化 == 0){
    	        	result = result + "<font color='red'>无变化</font>。";
    	        }else{
    		        if(req.data.失业环比变化>0){
    		        	result = result + "<font color='red'>上涨"+ (Math.round(req.data.失业环比变化*100)/100)+"%</font>,";
    		        }else{
    		        	result = result + "<font color='red'>下降"+ (Math.round(Math.abs(req.data.失业环比变化)*100)/100) +"%</font>,";
    		        }
    		        if(Math.abs(req.data.失业环比变化) > 2){
    		        	result = result + "变化<font color='red'>较快</font>";
    		        }else{
    		        	result = result + "变化<font color='red'>缓慢</font>";
    		        }
    		        result = result +"。";
    	        }
    	    	$("#economicSituation").html(result);
    		}
    	});
    }
});

$("#btn2").click(function(){
    var act = $("#act").val();

    if(act == ""){
        alert("请选择查询数据项!");
    }else{
        //发送查询请求
    	$.getJSON("form.do?act="+act, function(req) {
    		console.log(req);
    		if(req.error == 1){
    			alert(req.msg);
    		} else {
    			var contentHtml = "<table>";
    			if(act == "FaTieUsersTop5"){
    				contentHtml = contentHtml + "<tr><td>用户ID</td><td>用户名</td><td>发帖数量</td></tr>";
    				$.each(req.data, function(i, item) {
    					contentHtml = contentHtml + "<tr><td>"+item.uid+"</td><td>"+item.name+"</td><td>"+item.num+"</td></tr>";
        	    	});
    		    } else if (act == "ZhuanFaWeibosTop20") {
    		    	contentHtml = contentHtml + "<tr><td>微博ID</td><td>微博内容</td><td>转发次数</td></tr></tr>";
    		    	$.each(req.data, function(i, item) {
    					contentHtml = contentHtml + "<tr><td>"+item.mid+"</td><td>"+item.text+"</td><td>"+item.num+"</td></tr>";
        	    	});
    		    } else if (act == "HotTopicsTop3") {
    		    	contentHtml = contentHtml + "<tr><td>话题</td><td>评论数</td></tr>";
    		    	$.each(req.data, function(i, item) {
    					contentHtml = contentHtml + "<tr><td>"+item.topic+"</td><td>"+item.num+"</td></tr>";
        	    	});
    		    } else if (act == "HaveSameFriendsUserPairsTop5") {
    		    	contentHtml = contentHtml + "<tr><td>用户1</td><td>用户2</td><td>拥有相同朋友数量</td></tr>";
    		    	$.each(req.data, function(i, item) {
    					contentHtml = contentHtml + "<tr><td>"+item.user1+"</td><td>"+item.user2+"</td><td>"+item.num+"</td></tr>";
        	    	});
    		    } else if (act == "RepostSameWeibosUserPairsTop5") {
    		    	contentHtml = contentHtml + "<tr><td>用户1</td><td>用户2</td><td>转发相同微博数量</td></tr>";
    		    	$.each(req.data, function(i, item) {
    					contentHtml = contentHtml + "<tr><td>"+item.user1+"</td><td>"+item.user2+"</td><td>"+item.num+"</td></tr>";
        	    	});
    		    }
    			contentHtml = contentHtml + "</table>";
    			$("#weiboData").html(contentHtml);
    		}
    	});
    }
});
</script>
</body>
</html>