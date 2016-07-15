package net.hunme.message.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.hunme.message.R;
import net.hunme.message.ronglistener.MySendMessageListener;

import io.rong.imkit.RongIM;

public class ConservationActivity extends FragmentActivity implements View.OnClickListener{
    private SharedPreferences sp;
    private String username;
    private String userId;
    private String portrait;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conservation);
        if (RongIM.getInstance() != null) {
            //设置自己发出的消息监听器。
            RongIM.getInstance().setSendMessageListener(new MySendMessageListener());
        }
        sp = getSharedPreferences("config", Activity.MODE_PRIVATE);

        String userid = RongIM.getInstance().getCurrentUserId();
        Log.i("TAGFGG",userid);
        findview();
    }
    private  void findview(){
        iv_back = (ImageView) findViewById(R.id.iv_cback);
        tv_name = (TextView) findViewById(R.id.tv_cname);
        iv_detail = (ImageView) findViewById(R.id.iv_detail);
        iv_call = (ImageView) findViewById(R.id.iv_call);
        iv_call.setOnClickListener(this);
        iv_detail.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.iv_cback){
            finish();
        }else if (v.getId()==R.id.iv_call){
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:13850734494"));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else if (v.getId()==R.id.iv_detail){
            Intent intent = new Intent();
            intent.setClass(this,PersonDetailActivity.class);
            startActivity(intent);
        }
    }
}
