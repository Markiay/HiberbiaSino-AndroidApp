package com.mark.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mark.hibernia.R;
import com.mark.integratedInfo.ItemInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;
import static android.support.v4.content.PermissionChecker.checkSelfPermission;

public class FragmentThree extends Fragment {

    private RecyclerView recyclerView;
    private FloatingActionButton btnAdd;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Dialog bottomDialog = null;
    private AlertDialog loadDialog = null;
    RecyclerViewAdapter recyclerViewAdapter;
    //
    private static final int REQUEST_TAKE_PHOTO = 0;// 拍照
    private static final int REQUEST_CROP = 1;// 裁剪
    private static final int SCAN_OPEN_PHONE = 2;// 相册
    private static final int REQUEST_PERMISSION = 100;
    private Uri photoURI; // 拍照时返回的uri
    private Uri cropURI;// 图片裁剪时返回的uri
    private boolean hasPermission = false;
    String currentPhotoPath;
    private static final String TAG = "MainActivity";
    public ImageView imageView;
    public OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)//设置连接超时时间);
            .readTimeout(200, TimeUnit.SECONDS)//设置读取超时时间
            .build();
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    ArrayList<ItemInfo> arrayList = new ArrayList<>();
//    public String currentUser = "test1";
    Gson gson = new Gson();
    SharedPreferences sharedPreferences;
    String ImportantName = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sharedPreferences = getContext().getSharedPreferences("persistentInfo", Context.MODE_PRIVATE);
        ImportantName = sharedPreferences.getString("currentUserName", "");
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerViewAdapter = new RecyclerViewAdapter(getContext(), arrayList);
        recyclerView.setAdapter(recyclerViewAdapter);
        String dataUrl = "http://47.94.214.88:8080/HS/SelectItem";
        acquireData(dataUrl, ImportantName);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);//刷新控件
        swipeRefreshLayout.setColorSchemeResources(R.color.color_success, R.color.color_accent);//设置控件旋转颜色，最多四层
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.color_white);//设置刷新按钮背景颜色
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                acquireData(dataUrl, ImportantName);
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);//进度条不显示
                    }
                }, 2000);
            }
        });

//        for (int i = 0; i < 100; i++){
//            arrayList.add(String.format(Locale.CHINA, "第%03d条数据",i));
//        }
        //LayoutManager
//        new LinearLayoutManager()

        //add dividing lines添加分割线
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.onDraw(c, parent, state);
//                c.drawColor(Color.BLACK);//背景色
            }

            @Override
            public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.onDrawOver(c, parent, state);
            }

            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
//                outRect.set(0,5,0,5);
            }
        });




//        recyclerViewAdapter = new RecyclerViewAdapter(getContext(), arrayList);
//        recyclerView.setAdapter(recyclerViewAdapter);

        recyclerViewAdapter.setRecyclerViewClickListener(new RecyclerViewAdapter.onRecyclerViewClickListener() {
            @Override
            public void onRecyclerItemClick(RecyclerView parent, View view, int position, ItemInfo data) {
                Toast.makeText(getContext(), data.getItemid()+"", Toast.LENGTH_SHORT).show();
//                recyclerViewAdapter.remove(position);
//                recyclerViewAdapter.add(position, "新增数据");
            }
        });

        btnAdd = view.findViewById(R.id.fab);
        //snackBar滑动效果可用来做到底的展示
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog(getContext());
            }
        });





        return view;
    }//onCreateView
    public ImageButton confirm;

    private void Dialog(Context context){
        bottomDialog = new Dialog(context, R.style.BottomDialog);
        View dialogview = LayoutInflater.from(context).inflate(R.layout.dialog_add, null);
        bottomDialog.setContentView(dialogview);
        ViewGroup.LayoutParams layoutParams = dialogview.getLayoutParams();
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels;
        layoutParams.height=context.getResources().getDisplayMetrics().heightPixels;
        dialogview.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.show();
        TextView cancel = dialogview.findViewById(R.id.dialog_cancel);
        imageView = dialogview.findViewById(R.id.dialog_photo);
        Button capture = dialogview.findViewById(R.id.dialog_capture);
        Button gallery = dialogview.findViewById(R.id.dialog_gallery);
        confirm = dialogview.findViewById(R.id.dialog_imageButton);
        TextInputLayout til_dialog1 = dialogview.findViewById(R.id.textInputLayout4);
        EditText etitemName = dialogview.findViewById(R.id.dialog_itemname);

        TextInputLayout til_dialog2 = dialogview.findViewById(R.id.textInputLayout3);
        EditText etitemPrice = dialogview.findViewById(R.id.dialog_itemprice);

        TextInputLayout til_dialog3 = dialogview.findViewById(R.id.textInputLayout7);
        EditText etdescription = dialogview.findViewById(R.id.dialog_description);
        etitemName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(etitemName.getText()) || TextUtils.isEmpty(etitemPrice.getText()) || TextUtils.isEmpty(etdescription.getText())) {
                    confirm.setEnabled(Boolean.FALSE);
                } else {
                    confirm.setEnabled(Boolean.TRUE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etitemPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(etitemName.getText()) || TextUtils.isEmpty(etitemPrice.getText()) || TextUtils.isEmpty(etdescription.getText())) {
                    confirm.setEnabled(Boolean.FALSE);
                } else {
                    confirm.setEnabled(Boolean.TRUE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etdescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(etitemName.getText()) || TextUtils.isEmpty(etitemPrice.getText()) || TextUtils.isEmpty(etdescription.getText())) {
                    confirm.setEnabled(Boolean.FALSE);
                } else {
                    confirm.setEnabled(Boolean.TRUE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
//        Button capture = dialogview.findViewById(R.id.dialog_capture);
//        Button gallery = dialogview.findViewById(R.id.dialog_gallery);
//        ImageButton confirm = dialogview.findViewById(R.id.dialog_imageButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.dismiss();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPhotoPath==null){
                    Toast.makeText(getContext(), "Please select photo",  Toast.LENGTH_SHORT).show();
                }else {
                    loadDialog = new AlertDialog.Builder(getContext()).setView(LayoutInflater.from(getContext()).inflate(R.layout.dialog_processbar, null)).create();
                    loadDialog.setCancelable(false);
                    loadDialog.show();
                    FileInputStream fis = null;
                    try {
                        fis = new FileInputStream(currentPhotoPath);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap  = BitmapFactory.decodeStream(fis);
                    String b64 = bitmapToBase64(bitmap);
                    ItemInfo itemInfo = new ItemInfo(0, b64, Float.parseFloat(etitemPrice.getText().toString()), etitemName.getText().toString(),ImportantName, etdescription.getText().toString());
                    String url = "http://47.94.214.88:8080/HS/AddItem";
                    uploadDateToServerByPOST(url, itemInfo);
                }
            }
        });
        checkPermissons();
        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasPermission) {
                    takePhone();
                }
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasPermission) {
                    openGallery();
                }
            }
        });
    }//

    private void checkPermissons(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // 检查是否有存储和拍照权限
            if (checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
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
                Toast.makeText(getContext(), "权限授予失败！", Toast.LENGTH_SHORT).show();
                hasPermission = false;
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, SCAN_OPEN_PHONE);
    }

    // 拍照// 将file转换成uri
    // 注意7.0及以上与之前获取的uri不一样了，返回的是provider路径
    private void takePhone() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = createImageFile();
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(getContext(), "com.mark.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile(){
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        String path = Environment.getExternalStorageDirectory()+"/HS";
        File storageDir = new File(path);
        if (!storageDir.exists()){storageDir.mkdirs();}
        File image = new File(storageDir, imageFileName+".jpg");
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getContext().sendBroadcast(mediaScanIntent);
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
        if (fromCapture){
            cropURI = uri;
        }else{
            File cropPhoto = createImageFile();
            if (cropPhoto != null){cropURI = FileProvider.getUriForFile(getContext(), "com.mark.fileprovider", cropPhoto);}
        }
        List<ResolveInfo> resInfoList = getContext().getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            getContext().grantUriPermission(packageName, cropURI, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropURI);
        Toast.makeText(getContext(), "剪裁图片", Toast.LENGTH_SHORT).show();
        galleryAddPic();
        startActivityForResult(intent, REQUEST_CROP);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 拍照并进行裁剪
                case REQUEST_TAKE_PHOTO:
                    Log.e(TAG, "onActivityResult: imgUri:REQUEST_TAKE_PHOTO:" + photoURI.toString()+":"+currentPhotoPath);
                    cropPhoto(photoURI, true);
                    break;
                // 裁剪后设置图片
                case REQUEST_CROP:
                    imageView.setImageURI(cropURI);
                    Log.e(TAG, "onActivityResult: imgUri:REQUEST_CROP:" + cropURI.toString()+":"+currentPhotoPath);
                    break;
                // 打开图库获取图片并进行裁剪
                case SCAN_OPEN_PHONE:
                    cropPhoto(data.getData(), false);
                    Log.e(TAG, "onActivityResult: SCAN_OPEN_PHONE:" + data.getData().toString());
                    break;
            }
        }
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

    public void uploadDateToServerByPOST(String url, ItemInfo item_info){
        new AsyncTask<String, Void, String>(){

            @Override
            protected String doInBackground(String... strings) {
                String urlString = strings[0];
                FormBody formBody = new FormBody.Builder()
                        .add("itemid", item_info.getItemid()+"")
                        .add("picture", item_info.getPicture())
                        .add("price", item_info.getPrice()+"")
                        .add("itemname", item_info.getItemname())
                        .add("username", item_info.getUsername())
                        .add("description", item_info.getDescription())
                        .build();
                Request request = new Request.Builder().url(urlString).post(formBody).build();
                String jsonString = "";
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    String result = response.body().string();
                    JSONObject jsonObject = new JSONObject(result);
                    jsonString = jsonObject.getString("addItem");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonString;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equals("success")){
//                    recyclerViewAdapter.add(0, item_info);
                    recyclerView.getLayoutManager().scrollToPosition(0);
                    loadDialog.dismiss();
                    bottomDialog.dismiss();
                    acquireData("http://47.94.214.88:8080/HS/SelectItem", ImportantName);
                    Toast.makeText(getContext(), "Successfully", Toast.LENGTH_SHORT).show();
                }else if (s.equals("failure")){
                    Toast.makeText(getContext(), "The information is wrong", Toast.LENGTH_SHORT).show();
                    loadDialog.dismiss();
                }
            }
        }.execute(url);
    }

    public void acquireData(String DataURL, String currentUser){
        new AsyncTask<String, Void, String>(){

            @Override
            protected String doInBackground(String... strings) {
                String urlString = strings[0];
                FormBody formBody = new FormBody.Builder()
                        .add("username", currentUser)
                        .build();
                Request request = new Request.Builder().url(urlString).post(formBody).build();
                String result2 = "";
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    result2 = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result2;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(s != null){
//                    System.out.println(s);
                    int currentItemNumber = recyclerViewAdapter.getItemCount();
                    ArrayList<ItemInfo> list_item = gson.fromJson(s, new TypeToken<ArrayList<ItemInfo>>(){}.getType());
                    if (currentItemNumber==0){
                        for (int i = 0; i<list_item.size(); i++){
                            recyclerViewAdapter.add(0, list_item.get(i));
                        }
                    }else {
                        recyclerViewAdapter.removeAll();
                        for (int i = 0; i<list_item.size(); i++){
                            recyclerViewAdapter.add(0, list_item.get(i));
                        }
                    }
                    recyclerView.getLayoutManager().scrollToPosition(0);
                }else {
                    Toast.makeText(getContext(), "信息有误", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute(DataURL);
    }


}
//                    recyclerViewAdapter.add(0, itemInfo);
//                    recyclerView.getLayoutManager().scrollToPosition(0);
//                    bottomDialog.dismiss();
//                    Toast.makeText(getContext(), "信息有误", Toast.LENGTH_SHORT).show();
//                ItemInfo itemInfo = new ItemInfo(14534254, Float.parseFloat(et2.getText().toString()), et.getText().toString(),"test1");
//                ItemInfo itemInfo = null;//new ItemInfo(14534254, et2.getText().toString(), et.getText().toString(),"test1");
//                recyclerViewAdapter.add(0, itemInfo);
//                recyclerView.getLayoutManager().scrollToPosition(0);
//View dialogview = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add, null);
//                new AlertDialog.Builder(getContext()).setTitle("New Moudle")
//                        .setView(dialogview).setPositiveButton("confirm", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        EditText et = dialogview.findViewById(R.id.editText);
//                        String etsSring = et.getText().toString();
//                        recyclerViewAdapter.add(0, etsSring);
//                        recyclerView.getLayoutManager().scrollToPosition(0);
//                    }
//                }).setNegativeButton("Cancal", null).show();