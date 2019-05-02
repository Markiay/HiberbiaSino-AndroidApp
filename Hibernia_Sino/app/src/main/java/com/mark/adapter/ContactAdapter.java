package com.mark.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mark.hibernia.R;
import com.mark.integratedInfo.UserInfo;

import java.util.ArrayList;

/**
 * Created By HuangQing on 2018/7/23 11:03
 **/
public class ContactAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private ArrayList<UserInfo> mData;

    private MessageAdapter.OnItemClickListener onItemClickListener;

    public ContactAdapter(ArrayList<UserInfo> data) {
        this.mData = data;
    }

    public void updateData(ArrayList<UserInfo> data) {
        this.mData = data;
        notifyDataSetChanged();
    }


    //参数二为itemView的类型，viewType代表这个类型值
    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_rv_contact, viewGroup, false);
        // 实例化viewholder
        MessageAdapter.ViewHolder viewHolder = new MessageAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageAdapter.ViewHolder holder, int position) {
        // 绑定数据
//        holder.mTv.setText(mData.get(position).getNickname());
        holder.mTv.setText(mData.get(position).getAccount());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (onItemClickListener != null) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView, pos);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemClickListener != null) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemLongClick(holder.itemView, pos);
                }
                //表示此事件已经消费，不会触发单击事件
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTv;

        public ViewHolder(View itemView) {
            super(itemView);
            mTv = itemView.findViewById(R.id.item_tv);
        }
    }

    public void addNewItem(UserInfo userInfo) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.add(getItemCount(), userInfo);
        notifyItemInserted(getItemCount());
    }

    public void deleteItem(int position) {
        if (mData == null || mData.isEmpty()) {
            return;
        }
        mData.remove(position);
        //notifyItemRemoved(0);
    }

    public void setOnItemClickListener(MessageAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }
}
