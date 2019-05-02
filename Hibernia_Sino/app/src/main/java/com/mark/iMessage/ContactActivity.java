package com.mark.iMessage;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mark.adapter.ContactAdapter;
import com.mark.adapter.MessageAdapter;
import com.mark.hibernia.R;
import com.mark.integratedInfo.UserInfo;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created By HuangQing on 2018/7/23 11:00
 **/
public class ContactActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ContactAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<UserInfo> list = new ArrayList<UserInfo>();;
    String NameListURL = "http://47.94.214.88:8080/HS/NameList";
    OkHttpClient okHttpClient = new OkHttpClient();
    Gson gson = new Gson();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
//        list = getUserList();
        mRecyclerView = findViewById(R.id.recyclerview);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAdapter = new ContactAdapter(list);
        E_loadNameList(NameListURL);
        // 设置布局管理器
        mRecyclerView.setLayoutManager(mLayoutManager);
        // 设置adapter
        mRecyclerView.setAdapter(mAdapter);
        //使用默认的间隔线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mAdapter.setOnItemClickListener(new MessageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(ContactActivity.this, ChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", list.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    private ArrayList<UserInfo> getUserList() {
        ArrayList<UserInfo> list = new ArrayList<>();
        list.add(new UserInfo("mark"));
        list.add(new UserInfo("test123"));
        return list;
    }

    private void E_loadNameList(String url){
        new AsyncTask<String, Void, String>(){

            @Override
            protected String doInBackground(String... strings) {
                String urlString = strings[0];
                Request request = new Request.Builder().url(urlString).build();
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
                    ArrayList<String> list_name = gson.fromJson(s, new TypeToken<ArrayList<String>>(){}.getType());
                    for (int i = 0; i<list_name.size(); i++){
                        mAdapter.addNewItem(new UserInfo(list_name.get(i)));
                    }
                }
            }
        }.execute(url);
    }
}
