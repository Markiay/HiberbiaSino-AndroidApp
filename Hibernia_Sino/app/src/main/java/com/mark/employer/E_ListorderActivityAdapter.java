package com.mark.employer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mark.hibernia.R;
import com.mark.integratedInfo.OrderInfo;

import java.util.ArrayList;

public class E_ListorderActivityAdapter extends BaseAdapter {

    private Context context = null;
    private ArrayList<OrderInfo> arrayList;

    public E_ListorderActivityAdapter(Context context, ArrayList<OrderInfo> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public OrderInfo getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addOrderInfo(OrderInfo orderInfo){
        arrayList.add(orderInfo);
        this.notifyDataSetChanged();
    }

    public void clearALL(){
        arrayList.clear();
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout ll = null;
        if (convertView != null){
            ll = (LinearLayout) convertView;
        }else {
            ll = (LinearLayout) LayoutInflater.from(this.getContext()).inflate(R.layout.item_listorder, null);
        }

        OrderInfo orderInfo = getItem(position);
        
        TextView ordernumber = ll.findViewById(R.id.Eli_orderno);
        TextView orderdate = ll.findViewById(R.id.Eli_orderdate);
        TextView client = ll.findViewById(R.id.Eli_username);
        TextView itemid = ll.findViewById(R.id.Eli_itemid);
        TextView status = ll.findViewById(R.id.Eli_status);
        TextView lostdate = ll.findViewById(R.id.Eli_lostdate);

        ordernumber.setText(orderInfo.getOrdernumber()+"");
        orderdate.setText(orderInfo.getOrderdate());
        client.setText(orderInfo.getUsername());
        itemid.setText(orderInfo.getItemid()+"");
        status.setText(orderInfo.getStatus());
        lostdate.setText(orderInfo.getLostdate());

        return ll;
    }
}
