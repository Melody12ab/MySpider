package com.mbm.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PubMsgServlet extends HttpServlet {

	private static final long serialVersionUID = -6555086842464114497L;

	public PubMsgServlet() {
        super();
    }

    public void destroy() {
        super.destroy(); // Just puts "destroy" string in log
        // Put your code here
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String msgTitle   = request.getParameter("msgTitle");
        String msgContent = request.getParameter("msgContent");
        String verifyCode = request.getParameter("verifyCode");

        String legalCode = null;
        if (request.getSession().getAttribute("verifyCode") != null)
            legalCode = (String) (request.getSession().getAttribute("verifyCode"));

        String next;

        if (verifyCode != null && verifyCode.equalsIgnoreCase(legalCode)) {
        	next="ok";

        } else
            next = "no";
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        out.print(next);
        out.flush();
        out.close();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    public void init() throws ServletException {
    }

}