package com.mark.coreActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mark.employer.E_ListorderActivity;
import com.mark.fragment.FragmentHome;
import com.mark.fragment.FragmentThree;
import com.mark.fragment.FragmentTwo;
import com.mark.fragment.ViewPagerAdapter;
import com.mark.hibernia.LoginActivity;
import com.mark.hibernia.MainActivity;
import com.mark.hibernia.R;
import com.mark.integratedInfo.UserAllInfo;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView navigation;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private ArrayList<Fragment> frameList;
    FragmentThree fragmentThree = new FragmentThree();
    FragmentHome fragmentHome = new FragmentHome();
    SharedPreferences sharedPreferences;
    String ImportantName = "";
    OkHttpClient okHttpClient = new OkHttpClient();
    Gson gson = new Gson();
    ImageView drawer_header_icon;
    Button drawer_header_button;
    TextView drawer_header_name;
    private UserAllInfo userAllInfo;
    String PREURL = "http://47.94.214.88:8080/HS/GetCI";
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences = this.getSharedPreferences("persistentInfo", Context.MODE_PRIVATE);
        ImportantName = sharedPreferences.getString("currentUserName", "");
        sp = this.getSharedPreferences("USERALLINFO", Context.MODE_PRIVATE);
        preLoadingUserInfo(PREURL);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headView = navigationView.getHeaderView(0);
        drawer_header_icon = headView.findViewById(R.id.drawer_header_icon);
        drawer_header_button = headView.findViewById(R.id.drawer_header_button);
        drawer_header_name = headView.findViewById(R.id.drawer_header_name);
        drawer_header_name.setText(ImportantName);
        drawer_header_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(HomePage.this)
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
                                startActivity(new Intent(HomePage.this, LoginActivity.class));
                            }
                        })
                        .setNegativeButton(R.string.cancel, null).show();
            }
        });



        //viewPager的获取控件并设置监听器和adapter
        viewPager = findViewById(R.id.viewPager);
        frameList = new ArrayList<Fragment>();
        frameList.add(fragmentHome);//交换了一下
        frameList.add(fragmentThree);
        frameList.add(new FragmentTwo());
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), frameList);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                navigation.getMenu().getItem(i).setChecked(true);
                if (i==0){
                    setTitle("Claim");
                }else if (i==1){
                    setTitle("Item");
                }else if (i==2){
                    setTitle("Customer Service");
                }
//                if (i==0){
//                    fragmentThree.acquireData("http://47.94.214.88:8080/HS/SelectItem", "test1");
//                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        viewPager.setAdapter(viewPagerAdapter);

        //底部导航栏的空间获取和监听器
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {
            startActivity(new Intent(HomePage.this, CheckPersonal.class));
        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //    点击下方的导航栏所产生的效果
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //调用getItem（）//为防止隔页切换时,滑过中间页面的问题,去除页面切换缓慢滑动的动画效果
                    viewPager.setCurrentItem(0, false);
                    fragmentHome.downloadData("http://47.94.214.88:8080/HS/SelectOrder", ImportantName);
                    return true;
                case R.id.navigation_dashboard:
                    viewPager.setCurrentItem(1, false);
                    return true;
                case R.id.navigation_notifications:
                    viewPager.setCurrentItem(2, false);
                    return true;
            }
            return false;
        }
    };

    private void preLoadingUserInfo(String url){
        new AsyncTask<String, Void, String>(){

            @Override
            protected String doInBackground(String... strings) {
                String preUrl = strings[0];
                FormBody formBody = new FormBody.Builder()
                        .add("username", ImportantName)
                        .build();
                Request request = new Request.Builder().url(preUrl).post(formBody).build();
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
                    ArrayList<UserAllInfo> userAllInfoArrayList = gson.fromJson(s, new TypeToken<ArrayList<UserAllInfo>>(){}.getType());
                    userAllInfo = userAllInfoArrayList.get(0);
                    SharedPreferences.Editor editor=sp.edit();
                    editor.putString("CIGet",gson.toJson(userAllInfo));
                    editor.commit();
                    byte[] bytesTo = base64ToBytes(userAllInfo.getIcon());
                    Glide.with(HomePage.this)
                            .load(bytesTo)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.test)
                            .into(drawer_header_icon);
                }
            }
        }.execute(url);
    }

    public static byte[] base64ToBytes(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return bytes;
    }
}
