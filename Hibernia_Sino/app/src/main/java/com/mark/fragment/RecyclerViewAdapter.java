package com.mark.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mark.addition.ImageZoom;
import com.mark.hibernia.R;
import com.mark.integratedInfo.ItemInfo;
import com.mark.integratedInfo.OrderInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> implements View.OnClickListener {
    //初始化尽量不要放在方法内会很耗时
    private Context context;
    private ArrayList<ItemInfo> arrayList;
    private onRecyclerViewClickListener listener;
    private RecyclerView recyclerView;
    private AlertDialog claimDialog = null;
    private OkHttpClient okHttpClient = new OkHttpClient();
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public void setRecyclerViewClickListener(onRecyclerViewClickListener listener) {
        this.listener = listener;
    }

    public RecyclerViewAdapter(Context context, ArrayList<ItemInfo> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override       //初始化界面时使用
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {//i is the value of viewType
        View view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview, viewGroup, false);
        view.setOnClickListener(this);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.thing_number.setText(arrayList.get(i).getItemid()+"");//setText只能是String
//        Bitmap bitmap = base64ToBitmap(arrayList.get(i).getPicture());
//        myViewHolder.thing_image.setImageBitmap(bitmap);//使用Glide对象池
        byte[] bytesTo = base64ToBytes(arrayList.get(i).getPicture());
        Glide.with(context)
                .load(bytesTo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.test)
                .into(myViewHolder.thing_image);
        //
        myViewHolder.thing_price.setText(arrayList.get(i).getPrice()+"");
        myViewHolder.thing_name.setText(arrayList.get(i).getItemname());
        myViewHolder.thing_desc.setText(arrayList.get(i).getDescription());
        myViewHolder.thing_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, "can click", Toast.LENGTH_SHORT).show();
                //开始索赔
                View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_claim, null);
                String goodsNo = arrayList.get(i).getItemid()+"";
                String goodsName = arrayList.get(i).getItemname();
                String user = arrayList.get(i).getUsername();
                String goodsDesc = arrayList.get(i).getDescription();
                TextView clientName = dialogView.findViewById(R.id.namePicker);
                TextView clientId = dialogView.findViewById(R.id.idPicker);
                TextView clientGoods = dialogView.findViewById(R.id.goodsPicker);
                TextView clientDesc = dialogView.findViewById(R.id.descPicker);
                clientName.setText(user);
                clientId.setText(goodsNo);
                clientGoods.setText(goodsName);
                clientDesc.setText(goodsDesc);
                Button clientButton = dialogView.findViewById(R.id.TimePicker);
                clientButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DateDialog(clientButton);
                    }
                });
                claimDialog = new AlertDialog.Builder(context).setView(dialogView).setPositiveButton("commit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String lostDate = clientButton.getText().toString();
                        if (lostDate.equals("DD/MM/YYYY")){
                            Toast.makeText(context, "选择丢失日期", Toast.LENGTH_SHORT).show();
                        }else{
                            OrderInfo orderInfo = new OrderInfo(0,"od", user, Integer.parseInt(goodsNo), "processing", lostDate);
                            String url = "http://47.94.214.88:8080/HS/AddOrder";
                            uploadData(url, orderInfo);
                            System.out.println("========"+lostDate);

                        }
                    }
                }).setNegativeButton("cancel", null).create();
                claimDialog.show();
            }
        });

        myViewHolder.thing_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ImageZoom.class);
                i.putExtra("imageURL", bytesTo);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.recyclerView = null;
    }

    @Override
    public void onClick(View v) {
        if(recyclerView != null && listener != null){
            int position = recyclerView.getChildAdapterPosition(v);
            listener.onRecyclerItemClick(recyclerView, v, position, arrayList.get(position));
        }
    }

    public void remove(int position){
        arrayList.remove(position);
//        notifyDataSetChanged();
        notifyItemRemoved(position);//有动画效果
    }

    public void add(int position, ItemInfo data){
//        arrayList.add(position, data);
        arrayList.add(data);
//        notifyItemInserted(position);
        notifyDataSetChanged();
    }
    //修改当前点击事件
    public void change(int position, ItemInfo data){
        arrayList.remove(position);
        arrayList.add(position, data);
        notifyItemChanged(position);
    }

    public void removeAll(){
        arrayList.clear();
        notifyDataSetChanged();
    }

//    public static Bitmap base64ToBitmap(String base64Data) {
//        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
//        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//    }

    public static byte[] base64ToBytes(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return bytes;
    }

//    //开启点击对话框
//    public void claimDialog() {
//        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_claim, null);
//        new AlertDialog.Builder(context).setView(dialogView)
//                .setPositiveButton("commit", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                String goodsNo = arrayList.get(i).getItemid() + "";
//                String goodsName = arrayList.get(i).getItemname();
//                String user = arrayList.get(i).getUsername();
//                TextView clientName = dialogView.findViewById(R.id.namePicker);
//                TextView clientId = dialogView.findViewById(R.id.idPicker);
//                TextView clientGoods = dialogView.findViewById(R.id.goodsPicker);
//                clientName.setText(user);
//                clientId.setText(goodsNo);
//                clientGoods.setText(goodsName);
//                Button clientButton = dialogView.findViewById(R.id.TimePicker);
//            }
//        }).setNegativeButton("cancel", null).show();
//    }
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;

    private void DateDialog(Button btnDate) {
        calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //monthOfYear 得到的月份会减1所以我们要加1
                String time = String.valueOf(year) + "-" + String.valueOf(monthOfYear + 1) + "-" + Integer.toString(dayOfMonth);
                btnDate.setText(time);
//                Log.d("测试", time);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
        //自动弹出键盘问题解决
        datePickerDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    //以下的类为内部的viewHolder类
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView thing_number, thing_name, thing_price, thing_desc;
        private ImageView thing_image;
        private Button thing_btn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            thing_image = itemView.findViewById(R.id.EachItemIcon);
            thing_number = itemView.findViewById(R.id.ThingNumber);
            thing_name = itemView.findViewById(R.id.ThingName);
            thing_price = itemView.findViewById(R.id.ThingPrice);
            thing_desc = itemView.findViewById(R.id.ThingDescription);
            thing_btn = itemView.findViewById(R.id.ThingButton);
        }
    }
    //以下是recyclerView点击事件的监听接口
    public interface onRecyclerViewClickListener{
        void onRecyclerItemClick(RecyclerView parent, View view, int position, ItemInfo data);
        //RecyclerView是通过attach获取，view是点击方法，String可根据数据类型进行修改
    }

    private void uploadData(String url, OrderInfo orderInfo){
        new AsyncTask<String, Void, String>(){

            @Override
            protected String doInBackground(String... strings) {
                String strURL = strings[0];
                FormBody formBody = new FormBody.Builder()
                        .add("ordernumber", orderInfo.getOrdernumber()+"")
                        .add("orderdate", orderInfo.getOrderdate())
                        .add("username", orderInfo.getUsername())
                        .add("itemid", orderInfo.getItemid()+"")
                        .add("status", orderInfo.getStatus())
                        .add("lostdate", orderInfo.getLostdate())
                        .build();
                Request request = new Request.Builder().url(strURL).post(formBody).build();
                String jsonString = "";
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    String result = response.body().string();
                    JSONObject jsonObject = new JSONObject(result);
                    jsonString = jsonObject.getString("addClaim");
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
                    Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show();
                }else if (s.equals("failure")){
                    Toast.makeText(context, "服务器异常，请重新添加", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute(url);
    }


}
