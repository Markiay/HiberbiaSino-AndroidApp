package com.servlet;

import com.dto.customerALL;
import com.service.ClientInfoService;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

@WebServlet(name = "CIUpdateServlet")
public class CIUpdateServlet extends HttpServlet {

    ClientInfoService clientInfoService = new ClientInfoService();
    private HashMap<String, String> hashMap = new HashMap<String, String>();
    private JSONObject jsonObject = new JSONObject();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        request.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String username = request.getParameter("username");
        String gender = request.getParameter("gender");
        String phonenumber = request.getParameter("phonenumber");
        String emailaddress = request.getParameter("emailaddress");
        String bdString = request.getParameter("birthdate");
        String iconString = request.getParameter("icon");

        Date birthdate = null;
        try {
            birthdate = sdf.parse(bdString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        java.sql.Date sqlDate = new java.sql.Date(birthdate.getTime());
        byte[] decodedByte = Base64.decode(iconString);
        Blob icon = null;
        try {
            icon = new SerialBlob(decodedByte);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        customerALL customerALL = new customerALL(username,gender, phonenumber, emailaddress, sqlDate, icon);
        boolean bool = clientInfoService.updateCI(customerALL);
        if (bool) {
            hashMap.put("submitInfo", "success");
        } else {
            hashMap.put("submitInfo", "failure");
        }
        jsonObject.putAll(hashMap);
        out.print(jsonObject.toString());
        out.flush();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
