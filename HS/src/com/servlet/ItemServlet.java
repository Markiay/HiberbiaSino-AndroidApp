package com.servlet;

import com.dto.Item;
import com.service.ItemService;
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
import java.util.HashMap;

@WebServlet(name = "ItemServlet")
public class ItemServlet extends HttpServlet {

    public ItemService itemService = new ItemService();
    private HashMap<String, String> hashMap = new HashMap<String, String>();
    private JSONObject jsonObject = new JSONObject();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        request.setCharacterEncoding("UTF-8");

        int itemid = Integer.parseInt(request.getParameter("itemid"));
        String picture = request.getParameter("picture");
        float price = Float.parseFloat(request.getParameter("price"));
        String itemname = request.getParameter("itemname");
        String username = request.getParameter("username");
        String description = request.getParameter("description");
        PrintWriter out = response.getWriter();
        //
        byte[] decodedByte = Base64.decode(picture);
        Blob b = null;
        try {
            b = new SerialBlob(decodedByte);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //
        Item item = new Item(itemid, b, price, itemname, username, description);
        boolean bool = itemService.insertItemInfo(item);
        if (bool) {
            hashMap.put("addItem", "success");
        } else {
            hashMap.put("addItem", "failure");
        }
        jsonObject.putAll(hashMap);
        out.print(jsonObject.toString());
        out.flush();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

}
