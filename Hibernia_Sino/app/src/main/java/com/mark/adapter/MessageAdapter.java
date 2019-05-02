package com.mark.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mark.hibernia.R;
import com.mark.integratedInfo.MessageEntity;
import com.mark.utils.ImageUtils;
import com.mark.utils.RotateTransformation;

import java.util.ArrayList;

/**
 * Created By HuangQing on 2018/7/13 11:42
 **/
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private ArrayList<MessageEntity> mData;
    private OnItemClickListener onItemClickListener;
    private LayoutInflater inflater;
    private Context context;

    public enum ITEM_TYPE {
        ITEM_TYPE_MINE,
        ITEM_TYPE_OTHER
    }

    public MessageAdapter(Context context,ArrayList<MessageEntity> data) {
        this.context=context;
        this.mData = data;
        inflater=LayoutInflater.from(context);
    }

    public void updateData(ArrayList<MessageEntity> data) {
        this.mData = data;
        notifyDataSetChanged();
    }


    //参数二为itemView的类型，viewType代表这个类型值
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v;
        if(viewType== ITEM_TYPE.ITEM_TYPE_MINE.ordinal()){
            v= inflater.inflate(R.layout.item_cv_mine, viewGroup, false);
        }else{
            v= inflater.inflate(R.layout.item_cv_other, viewGroup, false);
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        // 绑定数据
        MessageEntity entity=mData.get(position);
        if(entity.getMsgType()==1){
            holder.mTv.setVisibility(View.VISIBLE);
            holder.mTv.setText(entity.getMessage());
            holder.mIv.setVisibility(View.GONE);
        }else{
            holder.mIv.setVisibility(View.VISIBLE);
            holder.mTv.setVisibility(View.GONE);
            RequestOptions options = new RequestOptions()
                    .transforms(new RotateTransformation(ImageUtils.parseImageDegree(entity.getImagePath())));
            Glide.with(context).load(entity.getImagePath()).apply(options).into(holder.mIv);
        }
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
    public int getItemViewType(int position) {
        if(mData.get(position).isMine()){
            return ITEM_TYPE.ITEM_TYPE_MINE.ordinal();
        }
        return ITEM_TYPE.ITEM_TYPE_OTHER.ordinal();
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTv;
        ImageView mIv;

        public ViewHolder(View itemView) {
            super(itemView);
            mTv = itemView.findViewById(R.id.item_tv);
            mIv=itemView.findViewById(R.id.iv);
        }
    }

    public void addNewItem(MessageEntity entity) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.add(getItemCount(), entity);
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        if (mData == null || mData.isEmpty()) {
            return;
        }
        mData.remove(position);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(MessageAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

}
