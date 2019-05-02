package com.servlet;

import com.dto.Order;
import com.service.OrderService;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

@WebServlet(name = "OrderAServlet")
public class OrderAServlet extends HttpServlet {
    public OrderService orderService = new OrderService();
    private HashMap<String, String> hashMap = new HashMap<String, String>();
    private JSONObject jsonObject = new JSONObject();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        request.setCharacterEncoding("UTF-8");

        int orderno = Integer.parseInt(request.getParameter("ordernumber"));
        String odate = request.getParameter("orderdate");

        String username = request.getParameter("username");
        int itemid = Integer.parseInt(request.getParameter("itemid"));
        String state = request.getParameter("status");
        String lostdate = request.getParameter("lostdate");

        PrintWriter out = response.getWriter();

        Date date = null;
        try {
            date = sdf.parse(lostdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        Order order = new Order(orderno, odate, username, itemid, state, sqlDate);
        boolean bool = orderService.insertOrderInfo(order);
        if (bool) {
            hashMap.put("addClaim", "success");
        } else {
            hashMap.put("addClaim", "failure");
        }
        jsonObject.putAll(hashMap);
        out.print(jsonObject.toString());
        out.flush();

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
