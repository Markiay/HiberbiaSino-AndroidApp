package com.mark.employer;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mark.hibernia.R;
import com.mark.integratedInfo.ItemInfo;
import com.mark.integratedInfo.OrderInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class E_ListorderActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listorder;
    private ArrayList<OrderInfo> orderInfoArrayList = new ArrayList<OrderInfo>();
    private E_ListorderActivityAdapter eListorderActivityAdapter = null;
    String url = "http://47.94.214.88:8080/HS/OrderStatus";
    String url2 = "http://47.94.214.88:8080/HS/SingleItem";
    String url3 = "http://47.94.214.88:8080/HS/UpdateStatus";
    String state = "";
    OkHttpClient okHttpClient = new OkHttpClient();
    Gson gson = new Gson();
    //AlertDialog
    private AlertDialog handlerDialog = null;
    TextView Edialog_itemid, Edialog_price, Edialog_itemname, Edialog_username, Edialog_lostdate, Edialog_itemdesc;
    Button Edialog_btndeny, Edialog_btnapprove;
    ImageView Edialog_icon;
    String claimNo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e__listorder);
        state = getIntent().getStringExtra("E_status");
        swipeRefreshLayout = findViewById(R.id.E_listOrderSwipe);// TODO: 2019/4/19
        swipeRefreshLayout.setColorSchemeResources(R.color.color_success, R.color.color_accent);//设置控件旋转颜色，最多四层
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.color_white);//设置刷新按钮背景颜色
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // TODO: 2019/4/19  获取数据
                E_LoadListorder(url);
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);//进度条不显示
                    }
                }, 2000);
            }
        });
        listorder = findViewById(R.id.E_listOrderLV);
        eListorderActivityAdapter = new E_ListorderActivityAdapter(E_ListorderActivity.this, orderInfoArrayList);
        listorder.setAdapter(eListorderActivityAdapter);

        E_LoadListorder(url);
        //click Event
        // TODO: 2019/4/19 设置processing可点同意或不同意
        listorder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View dialogView = LayoutInflater.from(E_ListorderActivity.this).inflate(R.layout.dialog_updatestate, null);
                Edialog_itemid = dialogView.findViewById(R.id.Edialog_itemid);
                Edialog_price = dialogView.findViewById(R.id.Edialog_price);
                Edialog_itemname = dialogView.findViewById(R.id.Edialog_itemname);
                Edialog_username = dialogView.findViewById(R.id.Edialog_username);
                Edialog_lostdate = dialogView.findViewById(R.id.Edialog_lostdate);
                Edialog_itemdesc = dialogView.findViewById(R.id.Edialog_itemdesc);
                Edialog_btndeny = dialogView.findViewById(R.id.Edialog_btndeny);
                Edialog_btnapprove = dialogView.findViewById(R.id.Edialog_btnapprove);
                if (!state.equals("processing")){
                    Edialog_btndeny.setVisibility(View.INVISIBLE);
                    Edialog_btnapprove.setVisibility(View.INVISIBLE);
                }
                Edialog_icon = dialogView.findViewById(R.id.Edialog_icon);
                Edialog_lostdate.setText(orderInfoArrayList.get(position).getLostdate());
                claimNo = orderInfoArrayList.get(position).getOrdernumber()+"";
                E_LoadSingleItem(url2, orderInfoArrayList.get(position).getItemid()+"");
                handlerDialog = new AlertDialog.Builder(E_ListorderActivity.this).setView(dialogView).create();
                handlerDialog.show();
                if (state.equals("processing")){
                    Edialog_btndeny.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            E_updateStatus(url3, "denying", claimNo);
                        }
                    });
                    Edialog_btnapprove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            E_updateStatus(url3, "approving", claimNo);
                        }
                    });
                }
            }
        });
    }

    public void E_LoadListorder(String DataURL){
        new AsyncTask<String, Void, String>(){

            @Override
            protected String doInBackground(String... strings) {
                String urlString = strings[0];
                FormBody formBody = new FormBody.Builder()
                        .add("status", state)
                        .build();
                Request request = new Request.Builder().url(urlString).post(formBody).build();
                String result2 = "";
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    result2 = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result2;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(s != null){
                    int currentItemNumber = eListorderActivityAdapter.getCount();
                    ArrayList<OrderInfo> list_item = gson.fromJson(s, new TypeToken<ArrayList<OrderInfo>>(){}.getType());
                    if (currentItemNumber==0){
                        for (int i = 0; i<list_item.size(); i++){
                            eListorderActivityAdapter.addOrderInfo(list_item.get(i));
//                            Log.e("=====", list_item.get(i).getOrderdate()+"!"+list_item.get(i).getStatus());
                        }
                    }else {
                        eListorderActivityAdapter.clearALL();
                        for (int i = 0; i<list_item.size(); i++){
                            eListorderActivityAdapter.addOrderInfo(list_item.get(i));
                        }
                    }
                }else {
                    Toast.makeText(E_ListorderActivity.this, "信息有误", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute(DataURL);
    }

    private void E_LoadSingleItem(String url, String ItemID){
        new AsyncTask<String, Void, String>(){

            @Override
            protected String doInBackground(String... strings) {
                String urlString = strings[0];
                FormBody formBody = new FormBody.Builder()
                        .add("itemid", ItemID)
                        .build();
                Request request = new Request.Builder().url(urlString).post(formBody).build();
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
                if(s != null){
                    ArrayList<ItemInfo> list_item = gson.fromJson(s, new TypeToken<ArrayList<ItemInfo>>(){}.getType());
                    ItemInfo singleItemInfo = list_item.get(0);
                    Edialog_itemid.setText(singleItemInfo.getItemid()+"");
                    Edialog_price.setText(singleItemInfo.getPrice()+"");
                    Edialog_itemname.setText(singleItemInfo.getItemname());
                    Edialog_username.setText(singleItemInfo.getUsername());
                    Edialog_itemdesc.setText(singleItemInfo.getDescription());
                    byte[] bytesTo = base64ToBytes(singleItemInfo.getPicture());
                    Glide.with(E_ListorderActivity.this)
                            .load(bytesTo)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.test)
                            .into(Edialog_icon);
                }
            }
        }.execute(url);
    }

    public static byte[] base64ToBytes(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return bytes;
    }

    private void E_updateStatus(String url, String status, String orderno){
        new AsyncTask<String, Void, String>(){

            @Override
            protected String doInBackground(String... strings) {
                String urlString = strings[0];
                FormBody formBody = new FormBody.Builder()
                        .add("status", status)
                        .add("ordernumber", orderno)
                        .build();
                Request request = new Request.Builder().url(urlString).post(formBody).build();
                String result = "";
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    String backData = response.body().string();

                    JSONObject jsonObject = new JSONObject(backData);//将返回的字符串转为json
                    result = jsonObject.getString("isupdate");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equals("success")){
                    handlerDialog.dismiss();
                    E_LoadListorder("http://47.94.214.88:8080/HS/OrderStatus");
                    Toast.makeText(E_ListorderActivity.this, "successfully", Toast.LENGTH_SHORT).show();
                }else if (s.equals("failure")){
                    Toast.makeText(E_ListorderActivity.this, "The information is wrong", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute(url);
    }
}
