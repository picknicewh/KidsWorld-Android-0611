package net.hongzhang.message.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.DetaiCodeUtil;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.BroadcastConstant;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.MyConnectionStatusListener;
import net.hongzhang.baselibrary.util.PermissionsChecker;
import net.hongzhang.message.R;
import net.hongzhang.message.bean.RyUserInfo;
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
    private  TextView tv_name;
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
  //  private SharedPreferences spf;
   // private SharedPreferences.Editor editor;
    private GroupOperationReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conservation);
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
        //设置导航栏的颜色
        iv_back = (ImageView) findViewById(R.id.iv_cback);
        tv_name = (TextView) findViewById(R.id.tv_cname);
        iv_detail = (ImageView) findViewById(R.id.iv_detail);
        iv_call = (ImageView) findViewById(R.id.iv_call);
        iv_call.setOnClickListener(this);
        iv_detail.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        receiver = new GroupOperationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastConstant.EDITGROUPNAME);
        filter.addAction(BroadcastConstant.DELETEDEXITGROIP);
        registerReceiver(receiver,filter);
        setGroupInfo();

    }
    private void setGroupInfo(){
      //  spf=getSharedPreferences("name", Context.MODE_PRIVATE);
      //  editor=spf.edit();
      //  targetId =  spf.getString("targetGroupId","");
      //  name = spf.getString("groupName","");
    /*    if (targetId.equals("")&& name.equals("") || !targetId.equals(intent.getData().getQueryParameter("targetId"))&&
                !name.equals(intent.getData().getQueryParameter("title"))){
        }*/
      //  editor.putString("groupName",name);
      //  editor.putString("targetGroupId",targetId);
     //   editor.commit();
        Intent intent = getIntent();
        targetId = intent.getData().getQueryParameter("targetId");
        name = intent.getData().getQueryParameter("title");
        tv_name.setText(name);
        mconversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));
        showview(mconversationType);
    }

    @Override
    protected void onRestart() {
        G.log("------------onRestart----------");
      //  setGroupInfo();
        tv_name.setText(name);
        super.onRestart();
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
        Type type = new  TypeToken<Result<RyUserInfo>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.MESSAGE_GETTSINFO,param,this,2,"phone");
    }
    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.iv_cback){
            finish();
        }else if (v.getId()==R.id.iv_call){
            if (!G.isEmteny(phoneNumber)){
                Uri phoneUri =  Uri.parse("tel:"+phoneNumber);
                Intent intent = new Intent(Intent.ACTION_DIAL,phoneUri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
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
                finish();
               /* //保留一个activity，其他切换都界面，都给销毁
                if (spf.getInt("count",0)==1){
                //   finish();
                }
                editor.putInt("count",1);
                editor.commit();*/
            }
        }
    }
    @Override
    public void onSuccess(String uri, Object date) {
        Result<RyUserInfo> UserInfo= (Result<RyUserInfo>) date;
        if (UserInfo!=null){
            if (UserInfo.isSuccess()){
                RyUserInfo ryUserInfo= UserInfo.getData();
                phoneNumber = ryUserInfo.getPhone();
            }
        }
    }
    @Override
    public void onError(String uri, Result error) {
        DetaiCodeUtil.errorDetail(error,this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
    private class  GroupOperationReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BroadcastConstant.DELETEDEXITGROIP)){
                finish();
            }else if (action.equals(BroadcastConstant.EDITGROUPNAME)){
                String groupName =  intent.getStringExtra("groupName");
                tv_name.setText(groupName);
                G.log("---------------------"+groupName);
            }
        }
    }
}
