package com.servlet;

import com.dto.ChatMessage;
import com.service.MessageService;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@WebServlet(name = "MessageIServlet")
public class MessageIServlet extends HttpServlet {

    private MessageService messageService = new MessageService();
    private HashMap<String, String> hashMap = new HashMap<String, String>();
    private JSONObject jsonObject = new JSONObject();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        request.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        String text = request.getParameter("text");
        int ordernumber = Integer.parseInt(request.getParameter("ordernumber"));

        ChatMessage chatMessage = new ChatMessage(text, "", ordernumber, "Y");
        boolean bool = messageService.sendChat(chatMessage);
        if (bool) {
            hashMap.put("sendMessage", "success");
        } else {
            hashMap.put("sendMessage", "failure");
        }
        jsonObject.putAll(hashMap);
        out.print(jsonObject.toString());
        out.flush();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
