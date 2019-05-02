package com.servlet;

import com.dto.User;
import com.service.CheckUserService;
import com.service.InsertInfoService;
import com.util.BCrypt;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@WebServlet(name = "Register")
public class Register extends HttpServlet {

    private InsertInfoService insertInfoService = new InsertInfoService();
    private HashMap<String, String> hashMap = new HashMap<String, String>();
    private JSONObject jsonObject = new JSONObject();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        request.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        //
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());

        User user = new User();
        user.setUsername(username);
        user.setPassword(hashed);

        boolean bool = insertInfoService.insertUserInfo(user);

        if (bool){
            hashMap.put("hassuccess", "success");
        }else{
            hashMap.put("hassuccess", "failure");
        }
        jsonObject.putAll(hashMap);
        out.print(jsonObject.toString());
        out.flush();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
