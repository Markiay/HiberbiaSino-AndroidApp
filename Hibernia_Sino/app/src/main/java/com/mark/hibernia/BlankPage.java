package com.mark.hibernia;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mark.coreActivity.HomePage;
import com.mark.utils.CheckSumBuilder;

import net.steamcrafted.loadtoast.LoadToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BlankPage extends AppCompatActivity {

    private AlertDialog imDialog = null;
    OkHttpClient okHttpClient = new OkHttpClient();
    String statusCode = "";
    String username = "", password = "";
    LoadToast loadToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank_page);
        username = getIntent().getStringExtra("USERNAME");
        password = getIntent().getStringExtra("PASSWORD");

        //loadingdialog
        loadToast = new LoadToast(BlankPage.this);
        loadToast.setText("Sending Reply...");
        loadToast.setTranslationY(300); // y offset in pixels
        loadToast.setTextColor(Color.BLACK).setBackgroundColor(Color.WHITE).setProgressColor(Color.rgb(128, 0, 128));
        loadToast.setBorderWidthDp(400);
        //

        View dialogView = LayoutInflater.from(BlankPage.this).inflate(R.layout.dialog_imregister, null);
        EditText imUsername = dialogView.findViewById(R.id.im_register_username);
        EditText imPassword = dialogView.findViewById(R.id.im_register_password);
        imUsername.setText(username);
        imDialog = new AlertDialog.Builder(BlankPage.this).setView(dialogView).setPositiveButton(R.string.commit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String impasswordStr = imPassword.getText().toString();
                if (impasswordStr.equals(password)) {
                    String url = "https://api.netease.im/nimserver/user/create.action";
                    YunxinId_Reg(url);
                    loadToast.show();
                    // TODO: 2019/4/23 加载的动态图
                } else {
                    Toast.makeText(BlankPage.this, "not match for two input", Toast.LENGTH_SHORT).show();
                }
            }
        }).create();
        imDialog.setCancelable(false);
        imDialog.show();
    }

    private void YunxinId_Reg(String url){
        new AsyncTask<String, Void, String>(){

            @Override
            protected String doInBackground(String... strings) {
                String yunxinURL = strings[0];
                String appKey = "c692ae2a44df5de44fb90836d133cd18";
                String appSecret = "15409d260fb4";
                String nonce =  "12345";
                String curTime = String.valueOf((new Date()).getTime() / 1000L);
                String checkSum = CheckSumBuilder.getCheckSum(appSecret, nonce ,curTime);//参考 计算CheckSum的java代码
                FormBody formBody = new FormBody.Builder()
                        .add("accid",username)
                        .add("token",password)
                        .build();
                Request request = new Request.Builder()
                        .url(yunxinURL)
                        .addHeader("AppKey", appKey)
                        .addHeader("Nonce", nonce)
                        .addHeader("CurTime", curTime)
                        .addHeader("CheckSum", checkSum)
                        .addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                        .post(formBody)
                        .build();
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    String jsonStr =  response.body().string();
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    statusCode = jsonObject.getInt("code")+"";
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return statusCode;
            }

            @Override
            protected void onPostExecute(String s) {
                if (s.equals("200")){
                    Toast.makeText(BlankPage.this, "Successfully", Toast.LENGTH_SHORT).show();
                    // Call this if it was successful
                    loadToast.success();
                    startActivity(new Intent(BlankPage.this, HomePage.class));
                }else {
                    Toast.makeText(BlankPage.this, "Error Registration"+s, Toast.LENGTH_SHORT).show();
                    // Or this method if it failed
                    loadToast.error();
                }
                super.onPostExecute(s);
            }
        }.execute(url);
    }


}

//                okHttpClient.newCall(request).enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        String jsonStr =  response.body().string();
//                        try {
//                            JSONObject jsonObject = new JSONObject(jsonStr);
//                            statusCode = jsonObject.getInt("code")+"";
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });