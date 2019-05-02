package com.mark.hibernia;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.mark.coreActivity.HomePage;
import com.mark.fragment.Perfect_Info;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText et_Username, et_Password, et_Password_Again, et_Verification;
    private ImageView iv_Back, iv_Verification;
    private Button btnRegister;
    public OkHttpClient okHttpClient;
    private String realCode, username, password, passwordAgain, anthCode;
    private TextInputLayout til_reg1, til_reg2, til_reg3, til_reg4;
//    public boolean isUser = false, isPass = false, isPassAgain = false, isanthCode = false;
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();

        iv_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.this.finish();
            }
        });
        //将验证码用图片的形式显示出来
        iv_Verification.setImageBitmap(Verification.getInstance().createBitmap());
        realCode = Verification.getInstance().getCode().toLowerCase();
        iv_Verification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_Verification.setImageBitmap(Verification.getInstance().createBitmap());
                realCode = Verification.getInstance().getCode().toLowerCase();
            }
        });

        //获取用户名，密码并验证是否符合规则

        //验证成功后，将信息打包成Json并发送至服务器，用AsyncTask来发送，并在post中作出反应
        //okhttp: 发起网络请求；将请求结果显示出来
        //runOnUiThread();里面添加runnable来实现UI线程
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int result = InfoVerify();
                switch(result){
                    case 1:
                        okHttpClient = new OkHttpClient();
                        String url = "http://47.94.214.88:8080/HS/Register";
//                        String url = "http://47.94.214.88:8080/HS/Register?username="+username+"&password="+password;
                        TransferDateByPOST(url);

                        break;
                    case 2:
                        Toast.makeText(RegisterActivity.this,"The passwords entered do not match", Toast.LENGTH_SHORT).show();
                        et_Password.setText("");
                        et_Password_Again.setText("");
                        break;
                    case 3:
                        Toast.makeText(RegisterActivity.this,"Verification code error", Toast.LENGTH_SHORT).show();
                        et_Verification.setText("");
                        iv_Verification.setImageBitmap(Verification.getInstance().createBitmap());
                        realCode = Verification.getInstance().getCode().toLowerCase();
                        break;
                    case 4:
                        Toast.makeText(RegisterActivity.this,"Username empty", Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        Toast.makeText(RegisterActivity.this,"Password empty", Toast.LENGTH_SHORT).show();
                        break;
                    case 6:
                        Toast.makeText(RegisterActivity.this,"Please enter your password again", Toast.LENGTH_SHORT).show();
                        break;
                    case 7:
                        Toast.makeText(RegisterActivity.this,"Please enter the verification code", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });

    }

    private int InfoVerify(){
        username = et_Username.getText().toString().trim();
        password = et_Password.getText().toString().trim();
        passwordAgain = et_Password_Again.getText().toString().trim();
        anthCode = et_Verification.getText().toString().toLowerCase().trim();
        if (username.equals("")) return 4;//用户名为空
        else if (password.equals("")) return 5;//密码为空
        else if (passwordAgain.equals("")) return 6;//请再次输入密码为空
        else if (anthCode.equals("")) return 7;//请输入验证码
        else{
            if (password.equals(passwordAgain)){
                //缺少用户名和密码的正则验证
                if (realCode.equals(anthCode)){
                    return 1;
                }else {
                    return 3;//验证码有误
                }
            }else{
                return 2;//两次输入的密码不一样
            }
        }
    }

    public void TransferDateByPOST(String url){
        new AsyncTask<String, Void, String>(){

            @Override
            protected String doInBackground(String... strings) {
                String urlString = strings[0];
                String  resultSet = "";
                String jsonString = "";
                //创建json文件上传至服务器
//               String updateToServer = "{'username':"+username+",'password':"+password+"}";
//                RequestBody requestBody = RequestBody.create(JSON, updateToServer);
//                Request request = new Request.Builder().url(urlString).build();GET
                FormBody formBody = new FormBody.Builder()
                        .add("username",username)
                        .add("password",password)
                        .build();//生成url查询后缀
                Request request = new Request.Builder().url(urlString).post(formBody).build();
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    resultSet = response.body().string();
                    JSONObject jsonObject = new JSONObject(resultSet);
                    jsonString = jsonObject.getString("hassuccess");
                    System.out.println(jsonString);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonString;
            }

            @Override
            protected void onPostExecute(String s) {
                if (s.equals("success")){
                    Toast.makeText(RegisterActivity.this,"Registered successfully", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(RegisterActivity.this, Perfect_Info.class);
                    i.putExtra("Username",username);
                    i.putExtra("Password",password);
                    startActivity(i);
                    RegisterActivity.this.finish();
                }else{
                    Toast.makeText(RegisterActivity.this,"The username already exists", Toast.LENGTH_SHORT).show();
                }
                super.onPostExecute(s);
            }
        }.execute(url);
    }

    private void initView(){
        et_Username = findViewById(R.id.et_register_username);
        et_Password = findViewById(R.id.et_register_password);
        et_Password_Again = findViewById(R.id.et_register_password_again);
        et_Verification = findViewById(R.id.et_register_verification);
        iv_Back = findViewById(R.id.iv_Back);
        iv_Verification = findViewById(R.id.iv_Verification);
        btnRegister = findViewById(R.id.btn_registerActivity_register);
        til_reg1 = findViewById(R.id.TextInputLayout_Reg1);
        til_reg2 = findViewById(R.id.TextInputLayout_Reg2);
        til_reg3 = findViewById(R.id.TextInputLayout_Reg3);
        til_reg4 = findViewById(R.id.TextInputLayout_Reg4);
        et_Username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(et_Username.getText())){

                }else{
                    til_reg1.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_Password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (TextUtils.isEmpty(et_Username.getText())){
                    til_reg1.setError("The username cannot be empty");
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()<6){
                    til_reg2.setError("Password no less than 6 characters");
                }else if (s.length()>16){
                    til_reg2.setError("Word limit exceeded");
                }else {
                    til_reg2.setError(null);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!(et_Password.getText().toString().trim().equals(et_Password_Again.getText().toString().trim()))){
                    til_reg3.setError("The password is not the same");
                }else {
                    til_reg3.setError(null);
                }
            }
        });

        et_Password_Again.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (TextUtils.isEmpty(et_Username.getText())){
                    til_reg1.setError("The username cannot be empty");
                }
                if (TextUtils.isEmpty(et_Password.getText())){
                    til_reg2.setError("The password cannot be empty");
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!(et_Password.getText().toString().trim().equals(et_Password_Again.getText().toString().trim()))){
                    til_reg3.setError("The password is not the same");
                }else {
                    til_reg3.setError(null);
                }
            }
        });

        et_Verification.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (TextUtils.isEmpty(et_Username.getText())){
                    til_reg1.setError("The user name cannot be empty");
                }
                if (TextUtils.isEmpty(et_Password.getText())){
                    til_reg2.setError("The password cannot be empty");
                }
                if (TextUtils.isEmpty(et_Password_Again.getText())){
                    til_reg3.setError("Please enter your password again");
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!(realCode.equals(et_Verification.getText().toString().trim().toLowerCase()))){
                    til_reg4.setError("The verification code is inconsistent");
                }else {
                    til_reg4.setError(null);
                }
            }
        });


    }
}
