package com.mark.fragment;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mark.coreActivity.CheckPersonal;
import com.mark.hibernia.LoginActivity;
import com.mark.hibernia.R;
import com.mark.hibernia.RegisterActivity;
import com.mark.integratedInfo.CountryPhone;
import com.mark.integratedInfo.UserAllInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Perfect_Info extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener{
    private TextView comSkip;
    private ImageView ivIcon;
    private RadioGroup rgGender;
    private Spinner spinner;
    private TextInputLayout textInputLayout1, textInputLayout2;
    private EditText etPhone, etEmail;
    private Button selectIcon, selectDate, submit;
    private String username = "", password = "";
    private String gender="", phonenumber="", emailaddress="", birthdate="0000-00-00", icon="";
    public String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    public String REGEX_MOBILE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
    private static final int REQUEST_CROP = 1;// 裁剪
    private static final int SCAN_OPEN_PHONE = 2;// 相册
    private static final int REQUEST_PERMISSION = 100;
    private Uri cropURI;// 图片裁剪时返回的uri
    private boolean hasPermission = false;
    String currentPhotoPath;
    private static final String TAG = "MainActivity";
    private OkHttpClient okHttpClient = new OkHttpClient();
    UserAllInfo userAllInfoCP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfect__info);
        initView();
        checkPermissons();
        if (getIntent()!=null){
            Intent i = getIntent();
            username = i.getStringExtra("Username");
            password = i.getStringExtra("Password");
        }
        if (password.equals("CheckPersonal")){
            comSkip.setText("Cancel");
            SharedPreferences sharedPreferences = getSharedPreferences("USERALLINFO" , Context.MODE_PRIVATE);
            String userStr = sharedPreferences.getString("CIGet", "");
            if(!TextUtils.isEmpty(userStr)){
                userAllInfoCP = new Gson().fromJson(userStr, new TypeToken<UserAllInfo>(){}.getType());
                byte[] bytesTo = base64ToBytes(userAllInfoCP.getIcon());
                Glide.with(Perfect_Info.this)
                        .load(bytesTo)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.test)
                        .into(ivIcon);
                if (userAllInfoCP.getGender().equals("Male")){
                    rgGender.check(R.id.complete_rBtn1);
                }else if (userAllInfoCP.getGender().equals("Female")){
                    rgGender.check(R.id.complete_rBtn2);
                }
                etPhone.setText(userAllInfoCP.getPhonenumber());
                etEmail.setText(userAllInfoCP.getEmailaddress());
                selectDate.setText(userAllInfoCP.getBirthdate());
            }
        }

    }

    public static byte[] base64ToBytes(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return bytes;
    }

    private void initView(){
        comSkip = findViewById(R.id.complete_skip);
        ivIcon = findViewById(R.id.complete_icon);
        rgGender = findViewById(R.id.complete_rGroup);
        spinner = findViewById(R.id.complete_spinner);
        textInputLayout1 = findViewById(R.id.complete_til1);
        textInputLayout2 = findViewById(R.id.complete_til2);
        etPhone = findViewById(R.id.complete_et1);
        etEmail = findViewById(R.id.complete_et2);
        selectIcon = findViewById(R.id.complete_buttonIcon);
        selectDate = findViewById(R.id.complete_buttonDate);
        submit = findViewById(R.id.complete_buttonSubmit);
        comSkip.setOnClickListener(this);
        selectIcon.setOnClickListener(this);
        selectDate.setOnClickListener(this);
        submit.setOnClickListener(this);
        rgGender.setOnCheckedChangeListener(this);
        spinnerDetail();
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>11){
                    textInputLayout1.setError("Exceed length limit");
                }else {
                    textInputLayout1.setError(null);
                }
                if (Pattern.matches(REGEX_MOBILE, s)){
                    textInputLayout1.setError(null);
                }else{
                    textInputLayout1.setError("Invalid format");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Pattern.matches(REGEX_EMAIL, s)){
                    textInputLayout2.setError(null);
                    submit.setEnabled(true);
                }else{
                    textInputLayout2.setError("Mailbox format error");
                    submit.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.complete_skip:
                if (password.equals("CheckPersonal")){
                    finish();
                }else {
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.putExtra("Username",username);
                    intent.putExtra("Password",password);
                    startActivity(intent);
                }
                break;
            case R.id.complete_buttonIcon:
                if (hasPermission) {
                    openGallery();
                }
                break;
            case R.id.complete_buttonDate:
                DateDialog(selectDate);
                break;
            case R.id.complete_buttonSubmit:
                phonenumber = etPhone.getText().toString();
                emailaddress = etEmail.getText().toString();
                if (!selectDate.getText().toString().equals("DD/MM/YYYY")){
                    birthdate = selectDate.getText().toString();
                }
                if (currentPhotoPath == null){
                    if (password.equals("CheckPersonal")){
                        icon = userAllInfoCP.getIcon();
                        UserAllInfo userAllInfo = new UserAllInfo(username, gender, phonenumber, emailaddress, birthdate, icon);
                        String Url = "http://47.94.214.88:8080/HS/UpdateCI";
                        updateInformation(Url, userAllInfo);
                    }else {
                        Toast.makeText(Perfect_Info.this, "Please select photo",  Toast.LENGTH_SHORT).show();
                    }
                }else{
                    FileInputStream fis = null;
                    try {
                        fis = new FileInputStream(currentPhotoPath);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap  = BitmapFactory.decodeStream(fis);
                    icon = bitmapToBase64(bitmap);

                    UserAllInfo userAllInfo = new UserAllInfo(username, gender, phonenumber, emailaddress, birthdate, icon);
                    String Url = "http://47.94.214.88:8080/HS/UpdateCI";
                    updateInformation(Url, userAllInfo);
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.complete_rBtn1:
                gender = "M";
                break;
            case R.id.complete_rBtn2:
                gender = "F";
                break;
        }
    }

    private void spinnerDetail(){
        ArrayList<CountryPhone> countryPhoneArrayList = new ArrayList<>();
        CountryPhone countryPhone = new CountryPhone(R.drawable.flagchina, "China", "+86");
        countryPhoneArrayList.add(countryPhone);
        countryPhone = new CountryPhone(R.drawable.flagireland, "Ireland", "+353");
        countryPhoneArrayList.add(countryPhone);

        final FlagAdapter adapter = new FlagAdapter(Perfect_Info.this, countryPhoneArrayList);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private DatePickerDialog datePickerDialog;
    private Calendar calendar;

    private void DateDialog(final Button btnDate) {
        calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -21);
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //monthOfYear 得到的月份会减1所以我们要加1
                String time = String.valueOf(year) + "-" + String.valueOf(monthOfYear + 1) + "-" + Integer.toString(dayOfMonth);
                btnDate.setText(time);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
        //自动弹出键盘问题解决
        datePickerDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void checkPermissons(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // 检查是否有存储和拍照权限
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                hasPermission = true;
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_PERMISSION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                hasPermission = true;
            } else {
                Toast.makeText(this, "权限授予失败！", Toast.LENGTH_SHORT).show();
                hasPermission = false;
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, SCAN_OPEN_PHONE);
    }

    // 图片裁剪
    private void cropPhoto(Uri uri, boolean fromCapture) {
        Intent intent = new Intent("com.android.camera.action.CROP"); //打开系统自带的裁剪图片的intent
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("scale", true);
        intent.putExtra("aspectX", 1); // 设置裁剪区域的宽高比例
        intent.putExtra("aspectY", 1); // 设置裁剪区域的宽高比例
        intent.putExtra("outputX", 200);// 设置裁剪区域的宽度和高度
        intent.putExtra("outputY", 200);// 设置裁剪区域的宽度和高度
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());// 图片输出格式
        intent.putExtra("return-data", false);// 若为false则表示不返回数据
        File cropPhoto = createImageFile();
        if (cropPhoto != null){cropURI = FileProvider.getUriForFile(this, "com.mark.fileprovider", cropPhoto);}
        List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            grantUriPermission(packageName, cropURI, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropURI);
        Toast.makeText(this, "剪裁图片", Toast.LENGTH_SHORT).show();
        galleryAddPic();
        startActivityForResult(intent, REQUEST_CROP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 裁剪后设置图片
                case REQUEST_CROP:
                    ivIcon.setImageURI(cropURI);
                    Log.e(TAG, "onActivityResult: imgUri:REQUEST_CROP:" + cropURI.toString()+":"+currentPhotoPath);
                    break;
                // 打开图库获取图片并进行裁剪
                case SCAN_OPEN_PHONE:
                    Log.e(TAG, "onActivityResult: SCAN_OPEN_PHONE:" + data.getData().toString());
                    cropPhoto(data.getData(), false);
                    break;
            }
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private File createImageFile(){
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        String path = Environment.getExternalStorageDirectory()+"/Icon";
        File storageDir = new File(path);
        if (!storageDir.exists()){storageDir.mkdirs();}
        File image = new File(storageDir, imageFileName+".jpg");
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private void updateInformation(String url, UserAllInfo userAllInfo){
        new AsyncTask<String, Void, String>(){

            @Override
            protected String doInBackground(String... strings) {
                String updateUrl = strings[0];
                FormBody formBody = new FormBody.Builder()
                        .add("username",userAllInfo.getUsername())
                        .add("gender",userAllInfo.getGender())
                        .add("phonenumber",userAllInfo.getPhonenumber())
                        .add("emailaddress",userAllInfo.getEmailaddress())
                        .add("birthdate",userAllInfo.getBirthdate())
                        .add("icon",userAllInfo.getIcon())
                        .build();//生成url查询后缀
                Request request = new Request.Builder().url(updateUrl).post(formBody).build();
                String jsonString = "";
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    String resultSet = response.body().string();
                    JSONObject jsonObject = new JSONObject(resultSet);
                    jsonString = jsonObject.getString("submitInfo");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonString;
            }

            @Override
            protected void onPostExecute(String s) {
                Log.e("====", s);
                if (s.equals("success")){
                    Toast.makeText(Perfect_Info.this,"Registered successfully", Toast.LENGTH_SHORT).show();
                    if (password.equals("CheckPersonal")){
                        Perfect_Info.this.finish();
                    }else {
                        Intent i = new Intent(Perfect_Info.this, LoginActivity.class);
                        i.putExtra("Username",username);
                        i.putExtra("Password",password);
                        startActivity(i);
                        Perfect_Info.this.finish();
                    }
                }else{
                    Toast.makeText(Perfect_Info.this,"The username already exists", Toast.LENGTH_SHORT).show();
                }
                super.onPostExecute(s);
            }
        }.execute(url);
    }
}
