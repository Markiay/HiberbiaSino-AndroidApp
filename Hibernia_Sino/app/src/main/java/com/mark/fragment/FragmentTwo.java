package com.mark.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mark.adapter.ContactAdapter;
import com.mark.adapter.MessageAdapter;
import com.mark.hibernia.R;
import com.mark.iMessage.ChatActivity;
import com.mark.integratedInfo.UserInfo;

import java.util.ArrayList;

public class FragmentTwo extends Fragment {

    private RecyclerView mRecyclerView;
    private ContactAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<UserInfo> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_contact, container, false);
        list = getUserList();
        TextView tv = view.findViewById(R.id.tv);
        tv.setVisibility(View.GONE);
        mRecyclerView = view.findViewById(R.id.recyclerview);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mAdapter = new ContactAdapter(list);
        // 设置布局管理器
        mRecyclerView.setLayoutManager(mLayoutManager);
        // 设置adapter
        mRecyclerView.setAdapter(mAdapter);
        //使用默认的间隔线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mAdapter.setOnItemClickListener(new MessageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getContext(), ChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", list.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });



        return view;
    }

    private ArrayList<UserInfo> getUserList() {
        ArrayList<UserInfo> list = new ArrayList<>();
        list.add(new UserInfo("hiberniasino"));
//        list.add(new UserInfo("test123"));
        return list;
    }

}
