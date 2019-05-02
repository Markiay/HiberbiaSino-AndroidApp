package com.mark.employer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mark.coreActivity.HomePage;
import com.mark.hibernia.LoginActivity;
import com.mark.hibernia.R;
import com.mark.iMessage.ContactActivity;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;

import java.util.Calendar;
import java.util.Date;

public class E_WelcomeActivity extends AppCompatActivity{

    private TextView title;
    private Button process, approve, reject, message;
    //获取系统时间
    private Calendar calendar = Calendar.getInstance();
    SharedPreferences sharedPreferences;
    String employeeName = "";
    private Button btn_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e__welcome);
        title = findViewById(R.id.E_welcomeTitle);
        process = findViewById(R.id.E_welcomeProcess);
        approve = findViewById(R.id.E_welcomeApprove);
        reject = findViewById(R.id.E_welcomeDeny);
        message = findViewById(R.id.E_welcomeMessage);
        btn_logout = findViewById(R.id.btn_logout);

        sharedPreferences = getSharedPreferences("persistentInfo", Context.MODE_PRIVATE);
        employeeName = sharedPreferences.getString("currentUserName", "");

        //提取他的时钟值，int型
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        if(hours<11){
            title.setText("Good morning! "+employeeName);
        }else if(hours<13){
            title.setText("Noon good! "+employeeName);
        }else if(hours<18){
            title.setText("Good afternoon! "+employeeName);
        }else if(hours<24){
            title.setText("Good evening! "+employeeName);
        }


        process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(E_WelcomeActivity.this, E_ListorderActivity.class);
                i.putExtra("E_status", "processing");
                startActivity(i);
            }
        });
        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(E_WelcomeActivity.this, E_ListorderActivity.class);
                i.putExtra("E_status", "approving");
                startActivity(i);
            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(E_WelcomeActivity.this, E_ListorderActivity.class);
                i.putExtra("E_status", "denying");
                startActivity(i);
            }
        });

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(E_WelcomeActivity.this, ContactActivity.class);
                startActivity(i);
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(E_WelcomeActivity.this)
                        .setIcon(R.drawable.ic_action_info)
                        .setTitle(R.string.Warning)
                        .setMessage(R.string.Warning_Message)
                        .setPositiveButton(R.string.commit, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                NIMClient.getService(AuthService.class).logout();
                                SharedPreferences.Editor editor0 = sharedPreferences.edit();
                                editor0.putString("currentUserName", "");
                                editor0.commit();
                                finish();
                                startActivity(new Intent(E_WelcomeActivity.this, LoginActivity.class));
                            }
                        })
                        .setNegativeButton(R.string.cancel, null).show();
            }
        });
    }
}
