package com.mark.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mark.hibernia.R;
import com.mark.integratedInfo.CountryPhone;

import java.util.ArrayList;

public class FlagAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<CountryPhone> countryList;

    public FlagAdapter(Context context, ArrayList<CountryPhone> countryList) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.countryList = countryList;
    }

    @Override
    public int getCount() {
        return countryList.size();
    }

    @Override
    public CountryPhone getItem(int position) {
        return countryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = View.inflate(context, R.layout.spinner_flag, null);
            new diyViewHolder(convertView);
        }

        diyViewHolder holder = (FlagAdapter.diyViewHolder) convertView.getTag();
        holder.ivFlag.setImageResource(getItem(position).getIconid());
        holder.tvFlag.setText(getItem(position).getCountry());
        holder.tvNum.setText(getItem(position).getPhoneNo());

        return convertView;
    }

    class diyViewHolder{
        ImageView ivFlag;
        TextView tvFlag, tvNum;

        public diyViewHolder(View convertView) {
            this.ivFlag = convertView.findViewById(R.id.ivFlag);
            this.tvFlag = convertView.findViewById(R.id.tvFlag);
            this.tvNum = convertView.findViewById(R.id.tvNum);
            convertView.setTag(this);
        }
    }
}
