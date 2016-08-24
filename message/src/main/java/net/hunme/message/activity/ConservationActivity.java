package net.hunme.message.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.hunme.baselibrary.activity.PermissionsActivity;
import net.hunme.baselibrary.util.G;
import net.hunme.baselibrary.util.PermissionsChecker;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.message.R;
import net.hunme.user.util.PermissionUtils;

import java.util.Locale;

import io.rong.imlib.model.Conversation;

/**
 * 作者： wh
 * 时间： 2016/7/15
 * 名称：通讯--聊天页面
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class ConservationActivity extends FragmentActivity implements View.OnClickListener{
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
    private String name;
    /**
     * 聊天用户id
     */
    private String image;
    /**
     *当前的会话类型
     */
    private Conversation.ConversationType mconversationType;
    private final String[] PERMISSIONS = new String[]{
            Manifest.permission.RECORD_AUDIO, //麦克风权限
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conservation);
     //   addExtendProvider();
        initView();
        if(new PermissionsChecker(this).lacksPermissions(PERMISSIONS)){
            PermissionsActivity.startActivityForResult(this, PermissionUtils.REQUEST_CODE, PERMISSIONS);
            return;
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
        Intent intent = getIntent();
        targetId = intent.getData().getQueryParameter("targetId");
        name = intent.getData().getQueryParameter("title");
        tv_name.setText(name);
        mconversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));
        showview(mconversationType);
    }
    private void  showview(Conversation.ConversationType mconversationType){
         if (mconversationType.equals(Conversation.ConversationType.GROUP)){
             iv_call.setVisibility(View.GONE);
             iv_detail.setVisibility(View.GONE);
         }else if (mconversationType.equals(Conversation.ConversationType.PRIVATE)){
             iv_call.setVisibility(View.VISIBLE);
             iv_detail.setVisibility(View.VISIBLE);
         }
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.iv_cback){
            finish();
        }else if (v.getId()==R.id.iv_call){
            Uri phoneUri =  Uri.parse("tel:"+Uri.parse(UserMessage.getInstance(this).getLoginName()));
            Intent intent = new Intent(Intent.ACTION_DIAL,phoneUri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else if (v.getId()==R.id.iv_detail){
            Intent intent = new Intent();
            intent.setClass(this,PersonDetailActivity.class);
            intent.putExtra("title",name);
            intent.putExtra("targetId",targetId);
            startActivity(intent);
        }
    }
 /*   private void  addExtendProvider(){
        //扩展功能自定义
        InputProvider.ExtendProvider[] provider = {
                new ImageInputProvider(RongContext.getInstance()),//图片
                new CameraInputProvider(RongContext.getInstance()),//相机
                new LocationInputProvider(RongContext.getInstance()),//地理位置
        };
        RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.PRIVATE, provider);
    }*/

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PermissionUtils.REQUEST_CODE:
                //检测到没有授取权限 关闭页面
                if(resultCode == PermissionsActivity.PERMISSIONS_DENIED){
                    G.showToast(this,"权限没有授取，可能会影响您的语音发送，请到权限中心授权");
                }
                break;
        }
    }
}
