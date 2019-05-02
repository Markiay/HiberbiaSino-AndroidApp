package com.servlet;

import com.service.EmployeeService;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@WebServlet(name = "UpdateStatusServlet")
public class UpdateStatusServlet extends HttpServlet {

    public EmployeeService employeeService = new EmployeeService();
    private HashMap<String, String> hashMap = new HashMap<String, String>();
    private JSONObject jsonObject = new JSONObject();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        request.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String status = request.getParameter("status");
        int ordernumber = Integer.parseInt(request.getParameter("ordernumber"));
        boolean bool = employeeService.upadateOrderStatus(status, ordernumber);
        if (bool){
            hashMap.put("isupdate", "success");
        }else{
            hashMap.put("isupdate", "failure");
        }
        jsonObject.putAll(hashMap);
        out.print(jsonObject.toString());
        out.flush();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
