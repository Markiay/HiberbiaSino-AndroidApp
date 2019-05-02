package com.mark.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mark.hibernia.R;
import com.mark.integratedInfo.ChatMessage;
import com.mark.integratedInfo.ItemInfo;
import com.mark.integratedInfo.OrderInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CardPagerAdapter extends PagerAdapter {

    public static int MAX_ELEVATION_FACTOR = 8;
    private List<CardView> mViews;
    private List<OrderInfo> mData;
    private float mBaseElevation;
    private Context context;
    OkHttpClient okHttpClient = new OkHttpClient();
    Gson gson = new Gson();
    private AlertDialog messageDialog;
    private String messGurl = "http://47.94.214.88:8080/HS/MessageG";
    private String messIurl = "http://47.94.214.88:8080/HS/MessageI";

    public CardPagerAdapter(Context context) {
        mViews = new ArrayList<>();
        mData = new ArrayList<>();
        this.context = context;
    }

    public void addCardItem(OrderInfo orderInfo) {
        mViews.add(null);
        mData.add(orderInfo);
        notifyDataSetChanged();
    }

    public void RemoveCardItem() {
        mData.clear();
        mViews.clear();
        notifyDataSetChanged();
    }

    public float getBaseElevation() {
        return mBaseElevation;
    }

    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.item_cardview, container, false);
        container.addView(view);
        bind(mData.get(position), view);
        CardView cardView = (CardView) view.findViewById(R.id.prettyCard);

        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }

        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, cardView);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

    private void bind(OrderInfo orderInfo, View view) {
        TextView claimOrderNo = (TextView) view.findViewById(R.id.claimOrderNo);
        TextView claimItemNo = (TextView) view.findViewById(R.id.claimItemNo);
        TextView claimLostDate = (TextView) view.findViewById(R.id.claimLostDate);
        TextView claimStatus = (TextView) view.findViewById(R.id.claimStatus);
        TextView claimDate = (TextView) view.findViewById(R.id.claimDate);
        Button claimButton = view.findViewById(R.id.claimButton);
        claimOrderNo.setText(orderInfo.getOrdernumber()+"");
        claimItemNo.setText(orderInfo.getItemid()+"");
        claimLostDate.setText(orderInfo.getLostdate());
        String color = orderInfo.getStatus();
        if (color.equals("processing")){
            claimStatus.setTextColor(Color.BLUE);
        }else if (color.equals("approving")){
            claimStatus.setTextColor(Color.GREEN);
        }else if (color.equals("denying")){
            claimStatus.setTextColor(Color.RED);
        }
        claimStatus.setText(orderInfo.getStatus());
        claimDate.setText(orderInfo.getOrderdate());
        if (orderInfo.getOrdernumber()==000 || orderInfo.getOrdernumber()==999){
            claimButton.setEnabled(false);
        }
        claimButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDialogView();
                MessGet(messGurl, orderInfo.getOrdernumber());
                messageDialog = new AlertDialog.Builder(context).setView(dialogView).setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (feedback.getText().toString().equals("")){

                        }else {
                            MessInsert(messIurl, orderInfo.getOrdernumber());
                        }
                    }
                }).setNegativeButton("cancel", null).create();
                messageDialog.show();
            }
        });
    }

    TextView time1, time2, time3, text1, text2, text3;
    EditText feedback;
    View dialogView;

    private void initDialogView(){
        dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_nonim, null);
        time1 = dialogView.findViewById(R.id.nonim_time1);
        time2 = dialogView.findViewById(R.id.nonim_time2);
        time3 = dialogView.findViewById(R.id.nonim_time3);
        text1 = dialogView.findViewById(R.id.nonim_text1);
        text2 = dialogView.findViewById(R.id.nonim_text2);
        text3 = dialogView.findViewById(R.id.nonim_text3);
        feedback = dialogView.findViewById(R.id.nonim_et);
    }

    private void MessGet(String url, int ordernumber){
        new AsyncTask<String, Void, String>(){

            @Override
            protected String doInBackground(String... strings) {
                String messageGETurl = strings[0];
                FormBody formBody = new FormBody.Builder()
                        .add("ordernumber", ordernumber+"")
                        .build();
                Request request = new Request.Builder().url(messageGETurl).post(formBody).build();
                String result = "";
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    result = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s!=null){
                    // TODO: 2019/4/24
                    ArrayList<ChatMessage> chatMessages = gson.fromJson(s, new TypeToken<ArrayList<ChatMessage>>(){}.getType());
                    int Size = chatMessages.size();
                    if (Size>=1){
                        time1.setText(chatMessages.get(Size-1).getMessagedate());
                        text1.setText(chatMessages.get(Size-1).getText());
                        if (chatMessages.get(Size-1).getIssend().equals("N")){
                            text1.setTextColor(Color.RED);
                            time1.setTextColor(Color.RED);
                        }
                    }
                    if (Size>=2){
                        time2.setText(chatMessages.get(Size-2).getMessagedate());
                        text2.setText(chatMessages.get(Size-2).getText());
                        if (chatMessages.get(Size-2).getIssend().equals("N")){
                            text2.setTextColor(Color.RED);
                            time2.setTextColor(Color.RED);
                        }
                    }
                    if (Size>=3){
                        time3.setText(chatMessages.get(Size-3).getMessagedate());
                        text3.setText(chatMessages.get(Size-3).getText());
                        if (chatMessages.get(Size-3).getIssend().equals("N")){
                            text3.setTextColor(Color.RED);
                            time3.setTextColor(Color.RED);
                        }
                    }
                }
            }
        }.execute(url);
    }

    private void MessInsert(String url, int ordernumber){
        new AsyncTask<String, Void, String>(){

            @Override
            protected String doInBackground(String... strings) {
                String strURL = strings[0];
                FormBody formBody = new FormBody.Builder()
                        .add("text", feedback.getText().toString())
                        .add("ordernumber", ordernumber+"")
                        .build();
                Request request = new Request.Builder().url(strURL).post(formBody).build();
                String jsonString = "";
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    String result = response.body().string();
                    JSONObject jsonObject = new JSONObject(result);
                    jsonString = jsonObject.getString("sendMessage");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonString;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equals("success")){
                    Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show();
                }else if (s.equals("failure")){
                    Toast.makeText(context, "服务器异常，请重新添加", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute(url);
    }

}
