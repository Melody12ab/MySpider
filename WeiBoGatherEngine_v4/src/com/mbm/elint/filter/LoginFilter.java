package com.mbm.elint.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginFilter implements javax.servlet.Filter {
	private static Logger logger = LoggerFactory.getLogger(LoginFilter.class);

	public void destroy() {
		logger.info("过滤器已经销毁");
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		// 判断user级别网页的浏览权限
		if (httpRequest.getSession().getAttribute("user") == null) {
			httpRequest.setAttribute("message",
					"<script>alert('您还没有登陆,没有该问该目录权限')</script>");
			httpRequest.getRequestDispatcher("/jump.jsp").forward(request,
					response);
			return;
		}
		httpRequest.setCharacterEncoding("UTF-8");
		httpResponse.setCharacterEncoding("UTF-8");
		chain.doFilter(httpRequest, httpResponse);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		logger.info("编码过滤器初始化完成");
//		SpiderControler spiderControler = new SpiderControler();
//		try {
//			spiderControler.startSpider();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

}
