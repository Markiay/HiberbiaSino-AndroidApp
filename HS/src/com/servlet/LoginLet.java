package com.servlet;

import com.dto.User;
import com.service.CheckUserService;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@WebServlet(name = "LoginLet")
public class LoginLet extends HttpServlet {

    private CheckUserService checkUserService = new CheckUserService();
    private HashMap<String, String> hashMap = new HashMap<String, String>();
    private JSONObject jsonObject = new JSONObject();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        request.setCharacterEncoding("UTF-8");
        //
        PrintWriter out = response.getWriter();
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        String bool = checkUserService.checkLogin(user);
        String[] bool0 = bool.split(" ");
        if (bool0[0].equals("true")){
            hashMap.put("issuccess", "success");
        }else{
            hashMap.put("issuccess", "failure");
        }
        hashMap.put("identity", bool0[1]);
        jsonObject.putAll(hashMap);
        out.print(jsonObject.toString());
        out.flush();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
