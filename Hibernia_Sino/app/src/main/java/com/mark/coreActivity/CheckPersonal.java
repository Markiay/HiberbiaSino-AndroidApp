package com.mark.coreActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mark.fragment.Perfect_Info;
import com.mark.hibernia.R;
import com.mark.integratedInfo.UserAllInfo;
import com.netease.nimlib.sdk.auth.LoginInfo;

public class CheckPersonal extends AppCompatActivity {

    private ImageView cp_ivback, cp_ivicon;
    private TextView cp_tvedit, cp_tvuser, cp_tvgender, cp_tvbd, cp_tvphone, cp_tvemail;
    private SharedPreferences sharedPreferences;
    private UserAllInfo userAllInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_personal);
        sharedPreferences = getSharedPreferences("USERALLINFO" , Context.MODE_PRIVATE);
        String userStr = sharedPreferences.getString("CIGet", "");
        if(!TextUtils.isEmpty(userStr)){
            userAllInfo = new Gson().fromJson(userStr, new TypeToken<UserAllInfo>(){}.getType());
        }
        initView();
        cp_ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        byte[] bytesTo = base64ToBytes(userAllInfo.getIcon());
        Glide.with(CheckPersonal.this)
                .load(bytesTo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.test)
                .into(cp_ivicon);
        cp_tvedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CheckPersonal.this, Perfect_Info.class);
                i.putExtra("Username",userAllInfo.getUsername());
                i.putExtra("Password","CheckPersonal");
                startActivity(i);
            }
        });
        cp_tvuser.setText(userAllInfo.getUsername());
        cp_tvgender.setText(userAllInfo.getGender());
        cp_tvbd.setText(userAllInfo.getBirthdate());
        cp_tvphone.setText(userAllInfo.getPhonenumber());
        cp_tvemail.setText(userAllInfo.getEmailaddress());
    }

    private void initView(){
        cp_ivback = findViewById(R.id.cp_ivback);
        cp_ivicon = findViewById(R.id.cp_ivicon);
        cp_tvedit = findViewById(R.id.cp_tvedit);
        cp_tvuser = findViewById(R.id.cp_tvuser);
        cp_tvgender = findViewById(R.id.cp_tvgender);
        cp_tvbd = findViewById(R.id.cp_tvbd);
        cp_tvphone = findViewById(R.id.cp_tvphone);
        cp_tvemail = findViewById(R.id.cp_tvemail);
    }

    public static byte[] base64ToBytes(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return bytes;
    }
}
