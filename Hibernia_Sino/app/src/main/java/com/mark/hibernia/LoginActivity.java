package com.mark.hibernia;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mark.coreActivity.HomePage;
import com.mark.employer.E_WelcomeActivity;
import com.mark.utils.CheckSumBuilder;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;

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

public class LoginActivity extends AppCompatActivity {

    private ImageView ivSwitch, ivUser, ivCode;
    private EditText etUsername, etPassword;
    private TextInputLayout til_login1, til_login2;
    private Button btnReg, btnEnter;
    private OkHttpClient okHttpClient;
    private String username, password;
    String jsonString = "";
    private SharedPreferences sp;
    private Gson gson=new Gson();
    LoadToast lt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sp=getSharedPreferences("userinfo",MODE_PRIVATE);
        initView();
        if (getIntent()!=null){
            Intent i = getIntent();
            etUsername.setText(i.getStringExtra("Username"));
            etPassword.setText(i.getStringExtra("Password"));
        }
        etUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    ivSwitch.setImageResource(R.drawable.ic_login_username);
                    ivUser.setImageResource(R.drawable.ic_action_user_purple);
                    ivCode.setImageResource(R.drawable.ic_action_lock_closed_gray);
                }else{
//                    ivUser.setImageResource(R.drawable.ic_action_user_gray);
                }
            }
        });

        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    ivSwitch.setImageResource(R.drawable.ic_login_password);
                    ivUser.setImageResource(R.drawable.ic_action_user_gray);
                    ivCode.setImageResource(R.drawable.ic_action_lock_closed_purple);
                }else{
//                    ivCode.setImageResource(R.drawable.ic_action_lock_closed_gray);
                }
            }
        });


//        if (!username.equals("") && !password.equals("")){
//            btnEnter.setEnabled(true);
//        }
        lt = new LoadToast(LoginActivity.this);
        lt.setText("Logging in...");
        lt.setTranslationY(300); // y offset in pixels
        lt.setTextColor(Color.BLACK).setBackgroundColor(Color.rgb(245, 245, 245)).setProgressColor(Color.rgb(128, 0, 128));
        lt.setBorderWidthDp(600);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_login_register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
                break;
            case R.id.btn_login_enter:
                lt.show();
                okHttpClient = new OkHttpClient();
                username = etUsername.getText().toString().trim();
                password = etPassword.getText().toString().trim();
                String url = "http://47.94.214.88:8080/HS/Login?username="+username+"&password="+password;
                CheckDataByPOST(url);
                break;
        }
    }

    private void initView(){
        ivSwitch = (ImageView) findViewById(R.id.switchOver) ;
        ivUser = (ImageView) findViewById(R.id.userIcon);
        ivCode = (ImageView) findViewById(R.id.codeIcon);
        etUsername = (EditText) findViewById(R.id.et_login_username);
        etPassword = (EditText) findViewById(R.id.et_login_password);
        btnReg = (Button) findViewById(R.id.btn_login_register);
        btnEnter = (Button) findViewById(R.id.btn_login_enter);
        til_login1 = findViewById(R.id.TextInputLayout_Login1);
        til_login2 = findViewById(R.id.TextInputLayout_Login2);
        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>15){
                    til_login1.setError("Word Limit Exceeded");
                }else {
                    til_login1.setError(null);
                }
                if (TextUtils.isEmpty(etUsername.getText()) || TextUtils.isEmpty(etPassword.getText())) {
                    btnEnter.setEnabled(Boolean.FALSE);
                } else {
                    btnEnter.setEnabled(Boolean.TRUE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>15){
                    til_login2.setError("Word Limit Exceeded");
                }else {
                    til_login2.setError(null);
                }
                if (TextUtils.isEmpty(etUsername.getText()) || TextUtils.isEmpty(etPassword.getText())) {
                    btnEnter.setEnabled(Boolean.FALSE);
                } else {
                    btnEnter.setEnabled(Boolean.TRUE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private String CheckDataByPOST(String url){
        new AsyncTask<String, Void, String>(){

            @Override
            protected String doInBackground(String... strings) {
                String urlString = strings[0];
                Request request = new Request.Builder().url(urlString).build();
                String identity = "";
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    String backData = response.body().string();//返回的字符串数据

                    JSONObject jsonObject = new JSONObject(backData);//将返回的字符串转为json
                    jsonString = jsonObject.getString("issuccess");
                    identity = jsonObject.getString("identity");
                    System.out.println(jsonString);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return identity;
            }

            @Override
            protected void onPostExecute(String s) {
                if (jsonString.equals("success")){
                    SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences("persistentInfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("currentUserName", username);
                    editor.putString("identity", s);
                    editor.commit();
                    login(s);
                }else{
                    lt.error();
                    Toast.makeText(LoginActivity.this,"Invalid login, please check user name and password", Toast.LENGTH_SHORT).show();
                }
                super.onPostExecute(s);
            }
        }.execute(url);
        return jsonString;
    }

    private void login(String userid) {
        //封装登录信息.
        LoginInfo info = new LoginInfo(username, password);
        //请求服务器的回调
        RequestCallback<LoginInfo> callback =
                new RequestCallback<LoginInfo>() {
                    @Override
                    public void onSuccess(LoginInfo param) {
                        Toast.makeText(LoginActivity.this,"Login Successfully", Toast.LENGTH_SHORT).show();
                        // 可以在此保存LoginInfo到本地，下次启动APP做自动登录用
                        SharedPreferences.Editor editor=sp.edit();
                        editor.putString("userLogin",gson.toJson(param));
                        editor.commit();
                        //跳转到消息页面
                        //NimUIKit.startP2PSession(MainActivity.this, "1234");
                        lt.success();
                        if (userid.equals("customer")) {
                            startActivity(new Intent(LoginActivity.this, HomePage.class));
                        } else if (userid.equals("stuff")) {
                            startActivity(new Intent(LoginActivity.this, E_WelcomeActivity.class));
                        }
                        finish();
                    }

                    @Override
                    public void onFailed(int code) {
//                        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                        if (userid.equals("customer")) {
                            Intent i = new Intent(LoginActivity.this, BlankPage.class);
                            i.putExtra("USERNAME", username);
                            i.putExtra("PASSWORD", password);
                            startActivity(i);
                            finish();
                        } else if (userid.equals("stuff")) {
//                        startActivity(new Intent(LoginActivity.this, E_WelcomeActivity.class));
                        }
                    }

                    @Override
                    public void onException(Throwable exception) {
                        Toast.makeText(LoginActivity.this, exception.toString(), Toast.LENGTH_SHORT).show();
                    }

                };
        //发送请求.
        NIMClient.getService(AuthService.class).login(info)
                .setCallback(callback);
    }

}