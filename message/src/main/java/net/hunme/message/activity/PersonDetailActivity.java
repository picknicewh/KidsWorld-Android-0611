package net.hunme.message.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.hunme.baselibrary.activity.BaseActivity;
import net.hunme.message.R;

/**
 * 作者： Administrator
 * 时间： 2016/7/15
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class PersonDetailActivity  extends BaseActivity implements View.OnClickListener{
    /**
     * 电话
     */
    private ImageView iv_pcall;
    /**
     * 短信
     */
    private ImageView iv_pmessage;
    /**
     * 号码
     */
    private TextView tv_phone;
    /**
     * 学校
     */
    private TextView tv_school;
    /**
     * 班级
     */
    private TextView tv_class;
    /**
     * 头像
     */
    private ImageView iv_phead;
    /**
     * 姓名
     */
    private TextView tv_pname;
    /**
     * 角色
     */
    private TextView tv_role;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persondatail);
        inittoobar();
        initview();
    }

    @Override
    protected void setToolBar() {

    }

    private void inittoobar(){
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void initview(){
        iv_pcall = $(R.id.iv_pcall);
        iv_phead = $(R.id.iv_phead);
        tv_pname= $(R.id.tv_pname);
        iv_pmessage = $(R.id.iv_pmessage);
        tv_phone = $(R.id.tv_phone);
        tv_role = $(R.id.tv_role);
        tv_class = $(R.id.tv_pclass);
        tv_school = $(R.id.tv_school);
        iv_pcall.setOnClickListener(this);
        iv_pmessage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String phone = tv_phone.getText().toString();
        if (v.getId()==R.id.iv_pcall){
            Uri phoneUri =  Uri.parse("tel:"+phone);
            Intent intent = new Intent(Intent.ACTION_DIAL,phoneUri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else if (v.getId()==R.id.iv_pmessage){
             Uri  smsUri =Uri.parse(phone);
             Intent intent = new Intent(Intent.ACTION_VIEW);
             intent.setType("vnd.android-dir/mms-sms");
            intent.setData(Uri.parse("smsto:"+smsUri));
             startActivity(intent);
        }
    }
}
