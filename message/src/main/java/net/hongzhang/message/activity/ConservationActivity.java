package net.hongzhang.message.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.MyConnectionStatusListener;
import net.hongzhang.baselibrary.util.PermissionsChecker;
import net.hongzhang.message.R;
import net.hongzhang.message.bean.RyUserInfor;
import net.hongzhang.message.ronglistener.MyConversationBehaviorListener;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

/**
 * 作者： wh
 * 时间： 2016/7/15
 * 名称：通讯--聊天页面
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class ConservationActivity extends FragmentActivity implements View.OnClickListener, OkHttpListener {
    public static final int PERSONDETAIL=2;
    /**
     * 名字view
     */
    private TextView tv_name;
    /**
     * 返回
     */
    private ImageView iv_back;
    /**
     * 打电话
     */
    private ImageView iv_call;
    /**
     * 用户详情
     */
    private ImageView iv_detail;
    /**
     * 聊天用户id
     */
    private String targetId;
    /**
     * 用户昵称
     */
    public static String name;
    /**
     *当前的会话类型
     */
    private Conversation.ConversationType mconversationType;
    /**
     * 电话号码
     */
    private String phoneNumber;
    private final String[] PERMISSIONS = new String[]{
            Manifest.permission.RECORD_AUDIO, //麦克风权限
    };
    private boolean isGroup = false;
    private SharedPreferences spf;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conservation);
       // G.setTranslucent(this);
        initView();
         PermissionsChecker.getInstance(this).getPerMissions(PERMISSIONS);
        if (RongIM.getInstance()!=null){
            //消息长按事件处理
            RongIM.setConversationBehaviorListener(new MyConversationBehaviorListener(this));
            // 账号抢登监听
            RongIM.setConnectionStatusListener(new MyConnectionStatusListener(this));
        }
    }
    /**
     * 初始化数据
     */
    private  void initView(){
        iv_back = (ImageView) findViewById(R.id.iv_cback);
        tv_name = (TextView) findViewById(R.id.tv_cname);
        iv_detail = (ImageView) findViewById(R.id.iv_detail);
        iv_call = (ImageView) findViewById(R.id.iv_call);
        iv_call.setOnClickListener(this);
        iv_detail.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        setGroupInfo();
    }
    private void setGroupInfo(){
        spf=getSharedPreferences("name", Context.MODE_PRIVATE);
        editor=spf.edit();
        targetId =  spf.getString("targetGroupId","");
        name = spf.getString("groupName","");
        Intent intent = getIntent();
        if (targetId.equals("")&& name.equals("") || !targetId.equals(intent.getData().getQueryParameter("targetId"))&&
                !name.equals(intent.getData().getQueryParameter("title"))){
                targetId = intent.getData().getQueryParameter("targetId");
                name = intent.getData().getQueryParameter("title");
        }
        editor.putString("groupName",name);
        editor.putString("targetGroupId",targetId);
        editor.commit();
        tv_name.setText(name);

        mconversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));
        showview(mconversationType);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setGroupInfo();
    }

    private void  showview(Conversation.ConversationType mconversationType){
         if (mconversationType.equals(Conversation.ConversationType.GROUP)){
             iv_call.setVisibility(View.GONE);
             iv_detail.setVisibility(View.VISIBLE);
             iv_detail.setImageResource(R.mipmap.ic_member);
             isGroup = true;
         }else if (mconversationType.equals(Conversation.ConversationType.PRIVATE)){
             getUserInfor(targetId);
             iv_call.setVisibility(View.VISIBLE);
             iv_detail.setVisibility(View.VISIBLE);
             iv_detail.setImageResource(R.mipmap.ic_person);
             isGroup = false;
         }
    }
    /**
     * 获取用户详情
     */
    private void  getUserInfor(String userid){
        Map<String,Object> param = new HashMap<>();
        param.put("tsId",userid);
        Type type = new  TypeToken<Result<RyUserInfor>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.MESSAGE_GETDETAIL,param,this,2,"phone");
    }
    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.iv_cback){
            finish();
        }else if (v.getId()==R.id.iv_call){
            Uri phoneUri =  Uri.parse("tel:"+phoneNumber);
            Intent intent = new Intent(Intent.ACTION_DIAL,phoneUri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else if (v.getId()==R.id.iv_detail){
            Intent intent = new Intent();
            if (isGroup){
                intent.setClass(this,GroupDetailActivity.class);
                intent.putExtra("title",name);
                intent.putExtra("targetGroupId",targetId);
                startActivityForResult(intent,GroupDetailActivity.EDIT_NMAE);
            }else {
                intent.setClass(this,PersonDetailActivity.class);
                intent.putExtra("title",name);
                intent.putExtra("targetId",targetId);
                startActivity(intent);
                //保留一个activity，其他切换都界面，都给销毁
                if (spf.getInt("count",0)==1){
                   finish();
                }
                editor.putInt("count",1);
                editor.commit();
            }
        }
    }

    @Override
    public void onSuccess(String uri, Object date) {
        Result<RyUserInfor> UserInfor = (Result<RyUserInfor>) date;
        if (UserInfor!=null){
            if (UserInfor.isSuccess()){
                RyUserInfor ryUserInfor= UserInfor.getData();
                phoneNumber = ryUserInfor.getAccount_info().get(0).getPhone();
            }
        }
    }

    @Override
    public void onError(String uri, String error) {
        Toast.makeText(this,error,Toast.LENGTH_SHORT).show();
    }
}
