<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>系统操作</title>
		<style type="text/css">
</style>
<script type="text/javascript">
  function changeBackground(id){
	  document.getElementById("span1").style.background="white";
	  document.getElementById("span2").style.background="white";
	  document.getElementById("span3").style.background="white";
	  
	  document.getElementById(id).style.background="orange";
  }
</script>
	</head>
	<body>
	     <div style="text-align: center;"> 
	        <span id="span1" onclick="changeBackground('span1')" style="background-color: orange;">菜单1</span>&nbsp;&nbsp;&nbsp;
	        <span id="span2" onclick="changeBackground('span2')">菜单2</span>&nbsp;&nbsp;&nbsp;
	        <span id="span3" onclick="changeBackground('span3')">菜单3</span>
	     </div>
	</body>
</html>