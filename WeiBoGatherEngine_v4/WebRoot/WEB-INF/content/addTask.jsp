<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>添加任务</title>
		
		<script type="text/javascript">
		  function doFocus(){
			  document.getElementById("keyword").focus();
		  }

		  function doSelect(){
			  var selValue=document.getElementById("mySel").value
			  if(selValue=="4"){
				  document.getElementById("keywordName").innerHTML="关键词";
				  return;
			  }
			  if(selValue=="5"){
				  document.getElementById("keywordName").innerHTML="微言内容docUrl";
				  return ;
			  }
			  //else {
				  document.getElementById("keywordName").innerHTML="微博URL";
			 // }
		  }
		  
		</script>
	</head>
	<body onload="doFocus()" style="text-align: center;">
	  ${message }
		<h1>
			添加任务
		</h1>
		<br />
		<div>
			<form id="mainForm" action="system!addTask.action?flag=1" method="POST" enctype ="multipart/form-data">
			  <!-- form的最外层div -->
			   <div>
			   <!-- 第一层div -->
			   <div>
					<span>类型</span>&nbsp;&nbsp;&nbsp;<span><select name="taskPojo.type" onchange="doSelect()" id="mySel">
					         <option value="1">帐户信息</option>
					         <option value="2">微博内容</option>
					         <option value="3">关注抓取</option>
					         <option value="4">关键词</option>
					         <option value="5">微言内容docUrl</option>
					          <option value="6">话题标题</option>
						</select>
					</span>
				</div>
				<br/><br/>
				<!-- 第二层div -->
			   <div>
					<span style="width: 20%;" id="keywordName">微博URL</span>&nbsp;&nbsp;&nbsp;
					<span>
					    <textarea name="taskPojo.url" id="keyword" rows="10" cols="50"></textarea>
					</span>
				</div>
				   <br/><br/>
				  <div>
					<span style="width: 20%;">URL文件路径</span>&nbsp;&nbsp;&nbsp;
					<span>
					    <input type="file" name="txtFile" id="txtFile"  ></input>
					</span>
				</div>
				
		<br/><br/>
		<!-- 第三层div -->
			   <div>
					<span>
					   <input type="button" value="提&nbsp;交&nbsp;任&nbsp;务" onclick="document.getElementById('mainForm').submit();">
					</span>
				</div>
			   </div>
			</form>
		</div>
	</body>
</html>