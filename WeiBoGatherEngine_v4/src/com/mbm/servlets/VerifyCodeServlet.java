package com.mbm.servlets;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mbm.util.VerifyCode;

public class VerifyCodeServlet extends HttpServlet {

    public VerifyCodeServlet() {
        super();
    }

    public void destroy() {
        super.destroy();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 设置浏览器不缓存本页
        response.setHeader("Cache-Control", "no-cache");

        // 生成验证码，写入用户session
        String verifyCode = VerifyCode.generateTextCode(VerifyCode.TYPE_UPPER_ONLY, 4, null);
        request.getSession().setAttribute("verifyCode", verifyCode);
        System.out.print("生成了："+verifyCode);

        // 输出验证码给客户端
        response.setContentType("image/jpeg");
        BufferedImage bim = VerifyCode.generateImageCode(verifyCode, 60, 20, 0, true, Color.WHITE, Color.BLACK, null);
        ImageIO.write(bim, "JPEG", response.getOutputStream());
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    public void init() throws ServletException {
    }

}

