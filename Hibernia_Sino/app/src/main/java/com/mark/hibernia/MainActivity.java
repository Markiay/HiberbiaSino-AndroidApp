package com.mark.hibernia;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.mark.coreActivity.HomePage;
import com.mark.employer.E_WelcomeActivity;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_bill);
        sharedPreferences = this.getSharedPreferences("persistentInfo", Context.MODE_PRIVATE);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                String ImportantName = sharedPreferences.getString("currentUserName", "");
                String identity = sharedPreferences.getString("identity", "");
                if (!ImportantName.equals("")){
                    if (identity.equals("customer")){
                        Intent intent = new Intent(MainActivity.this, HomePage.class);
                        startActivity(intent);
                        finish();
                    }else if (identity.equals("stuff")){
                        Intent intent = new Intent(MainActivity.this, E_WelcomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }else {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

        }, 1500);
    }
}
