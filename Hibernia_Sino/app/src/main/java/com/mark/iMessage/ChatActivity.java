package com.mark.iMessage;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mark.adapter.MessageAdapter;
import com.mark.hibernia.MainActivity;
import com.mark.hibernia.R;
import com.mark.integratedInfo.MessageEntity;
import com.mark.integratedInfo.UserInfo;
import com.mark.utils.PathUtils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.ImageAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created By HuangQing on 2018/7/21 18:52
 **/
public class ChatActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG=ChatActivity.class.getSimpleName();
    private static final int REQUEST_CODE=1;
    private EditText et3;
    private Button btn_send;
    private TextView tv,tv_title;
    private ImageView iv;
    private String account;
    private Observer<List<IMMessage>> incomingMessageObserver;
    private RecyclerView mRecyclerView;
    private MessageAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private UserInfo userInfo;
    private ArrayList<MessageEntity> list=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initData();
        initView();
        initMessageObserver();
        requestPermission();
    }

    private void initData(){
        userInfo=(UserInfo)getIntent().getExtras().getSerializable("user");
        account=userInfo.getAccount();
    }

    private void initView(){
        et3 = findViewById(R.id.et3);
        btn_send=findViewById(R.id.btn_send);
        tv=findViewById(R.id.tv);
        tv_title=findViewById(R.id.tv_title);
        iv=findViewById(R.id.iv);
        mRecyclerView=findViewById(R.id.recyclerview);

        btn_send.setOnClickListener(this);
        tv.setOnClickListener(this);
        iv.setOnClickListener(this);

//        tv_title.setText(userInfo.getNickname());
        tv_title.setText(userInfo.getAccount());

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAdapter = new MessageAdapter(this,list);
        // 设置布局管理器
        mRecyclerView.setLayoutManager(mLayoutManager);
        // 设置adapter
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initMessageObserver(){
        // 处理新收到的消息，为了上传处理方便，SDK 保证参数 messages 全部来自同一个聊天对象。
        //消息接收观察者
        incomingMessageObserver = new Observer<List<IMMessage>>() {
            @Override
            public void onEvent(List<IMMessage> messages) {
                // 处理新收到的消息，为了上传处理方便，SDK 保证参数 messages 全部来自同一个聊天对象。
                IMMessage imMessage = messages.get(0);

                if(imMessage.getMsgType().equals(MsgTypeEnum.text)){
                    String messageStr=imMessage.getContent();
                    mAdapter.addNewItem(new MessageEntity(messageStr,null,1,false));
                }else if(imMessage.getMsgType().equals(MsgTypeEnum.image)){
                    ImageAttachment msgAttachment=(ImageAttachment)imMessage.getAttachment();

                    String uri=msgAttachment.getThumbUrl();
                    mAdapter.addNewItem(new MessageEntity(null,uri,2,false));
                }

                account = imMessage.getFromAccount();
            }
        };
        //注册消息接收观察者,
        //true,代表注册.false,代表注销
        NIMClient.getService(MsgServiceObserve.class)
                .observeReceiveMessage(incomingMessageObserver, true);
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        },1);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_send:
                //发送消息
                // 以单聊类型为例
                SessionTypeEnum sessionType = SessionTypeEnum.P2P;
                String text = et3.getText().toString();
                // 创建一个文本消息
                IMMessage textMessage = MessageBuilder.createTextMessage(account, sessionType, text);
                // 发送给对方
                NIMClient.getService(MsgService.class).sendMessage(textMessage, false);
                //tv2.setText(text);
                mAdapter.addNewItem(new MessageEntity(text,null,1,true));
                mRecyclerView.scrollToPosition(list.size());
                et3.setText("");
                break;
            case R.id.iv:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(intent,1);
                break;
            case R.id.tv:
                //退出登录
//                NIMClient.getService(AuthService.class).logout();
//                finish();
//                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1 && data!=null){
            //获取真实路径，防止在某些机型，如小米中，获取的路径为空
            Uri uri=Uri.parse(PathUtils.getRealUri(ChatActivity.this,data.getData()));
            //转化为file文件
            File imageFile=new File(uri.toString());
            //构造图片消息对象
            IMMessage message = MessageBuilder.createImageMessage(account,SessionTypeEnum.P2P, imageFile, imageFile.getName());
            //发送图片消息
            NIMClient.getService(MsgService.class).sendMessage(message, false);
            mAdapter.addNewItem(new MessageEntity(null,data.getData().toString(),2,true));
            mRecyclerView.scrollToPosition(list.size());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销消息接收观察者.
        NIMClient.getService(MsgServiceObserve.class)
                .observeReceiveMessage(incomingMessageObserver, false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1&&grantResults.length>1){

        }
    }

}

