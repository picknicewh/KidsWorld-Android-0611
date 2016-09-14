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
import net.hunme.baselibrary.util.MyConnectionStatusListener;
import net.hunme.baselibrary.util.PermissionsChecker;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.message.R;
import net.hunme.user.util.PermissionUtils;

import java.util.Locale;

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
     *当前的会话类型
     */
    private Conversation.ConversationType mconversationType;
    private final String[] PERMISSIONS = new String[]{
            Manifest.permission.RECORD_AUDIO, //麦克风权限
    };
    private boolean isGroup = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conservation);
        initView();
        if(new PermissionsChecker(this).lacksPermissions(PERMISSIONS)){
            PermissionsActivity.startActivityForResult(this, PermissionUtils.REQUEST_CODE, PERMISSIONS);
        }
        // 账号抢登监听
        if (RongIM.getInstance()!=null){
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
        Intent intent = getIntent();
        if (intent.getData()!=null){
            targetId = intent.getData().getQueryParameter("targetId");
            name = intent.getData().getQueryParameter("title");
            tv_name.setText(name);
            mconversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));
            showview(mconversationType);
        }
    }

    private void  showview(Conversation.ConversationType mconversationType){
         if (mconversationType.equals(Conversation.ConversationType.GROUP)){
             iv_call.setVisibility(View.GONE);
             iv_detail.setVisibility(View.VISIBLE);
             iv_detail.setImageResource(R.mipmap.ic_member);
             isGroup = true;
         }else if (mconversationType.equals(Conversation.ConversationType.PRIVATE)){
             iv_call.setVisibility(View.VISIBLE);
             iv_detail.setVisibility(View.VISIBLE);
             iv_detail.setImageResource(R.mipmap.ic_person);
             isGroup = false;
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
            finish();
        }else if (v.getId()==R.id.iv_detail){
            Intent intent = new Intent();
            if (isGroup){
                intent.setClass(this,GroupDetailActivity.class);
                intent.putExtra("title",name);
                intent.putExtra("targetGroupId",targetId);
                startActivityForResult(intent,GroupDetailActivity.EDIT_NMAE);
                finish();
                G.log("我跳转了----");
            }else {
                intent.setClass(this,PersonDetailActivity.class);
                intent.putExtra("title",name);
                intent.putExtra("targetId",targetId);
                startActivity(intent);
            }

        }
    }
}
