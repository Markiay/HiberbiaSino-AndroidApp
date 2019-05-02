package com.mark.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mark.hibernia.R;
import com.mark.integratedInfo.OrderInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FragmentHome extends Fragment{
    private OkHttpClient okHttpClient = new OkHttpClient();
    private ViewPager mViewPager;
    private CardPagerAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;
    Gson gson = new Gson();
    private SwipeRefreshLayout swipeRefresh;
    SharedPreferences sharedPreferences;
    String ImportantName = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.claimViewPager);

        sharedPreferences = getContext().getSharedPreferences("persistentInfo", Context.MODE_PRIVATE);
        ImportantName = sharedPreferences.getString("currentUserName", "");

        mCardAdapter = new CardPagerAdapter(getContext());
//        mCardAdapter.addCardItem(new OrderInfo(000, "2019-04-05", "示例", 000, "processing", "2019-04-01"));
        String url = "http://47.94.214.88:8080/HS/SelectOrder";
        downloadData(url, ImportantName);




        return view;
    }

    public void downloadData(String url, String user){
        new AsyncTask<String, Void, String>(){

            @Override
            protected String doInBackground(String... strings) {
                String urlString = strings[0];
                FormBody formBody = new FormBody.Builder()
                        .add("username", user)
                        .build();
                Request request = new Request.Builder().url(urlString).post(formBody).build();
                String resultSet = "";
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    resultSet = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return resultSet;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s!=null){
                    int currentCount = mCardAdapter.getCount();
                    ArrayList<OrderInfo> list_order = gson.fromJson(s, new TypeToken<ArrayList<OrderInfo>>(){}.getType());
                    if(currentCount==0 && list_order.size()==0){
                        mCardAdapter.addCardItem(new OrderInfo(000, "Example", "示例", 000, "processing", "2019-04-01"));
                    }else if (currentCount==1 && list_order.size()==0){
                        mCardAdapter.RemoveCardItem();
                        mCardAdapter.addCardItem(new OrderInfo(999, "Example", "示例", 999, "processing", "2019-04-01"));
                    } else if (currentCount==0 && list_order.size()!=0){
                        for (int i = 0; i<list_order.size(); i++){
                            mCardAdapter.addCardItem(list_order.get(i));
                        }
                    }else{
                        mCardAdapter.RemoveCardItem();
                        Toast.makeText(getContext(), "success", Toast.LENGTH_SHORT).show();
                        for (int i = 0; i<list_order.size(); i++){
                            mCardAdapter.addCardItem(list_order.get(i));
                        }
                    }
                    mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);
                    mViewPager.setAdapter(mCardAdapter);

                    mViewPager.setPageTransformer(false, mCardShadowTransformer);
                    mViewPager.setOffscreenPageLimit(3);
                    mCardShadowTransformer.enableScaling(true);
                }else{
                    Toast.makeText(getContext(), "信息有误", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute(url);
    }
}
