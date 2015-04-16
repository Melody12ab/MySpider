<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>系统操作</title>
	</head>
	<body>
	${message }
		<h1 style="text-align: center;">
			系统操作
		</h1>
		<br />
		<br />
		<div style="text-align: center;">
			<a href="system!start.action">开启抓取系统</a>
		</div>
		<br/>
		<div style="text-align: center;">
			<a href="system!pause.action">暂停抓取系统</a>
		</div>
		<br/>
		<div style="text-align: center;">
			<a href="system!addMyTask.action">添加抓取任务</a>
		</div>
		<br/><hr/><br/>
		<div style="text-align: center;">
			<a href="mongo!insert.action">MongoTest</a>
		</div>
		
	</body>
</html>