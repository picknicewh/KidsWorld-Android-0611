package net.hunme.message.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.baselibrary.image.ImageCache;
import net.hunme.baselibrary.mode.Result;
import net.hunme.baselibrary.network.Apiurl;
import net.hunme.baselibrary.network.OkHttpListener;
import net.hunme.baselibrary.network.OkHttps;
import net.hunme.message.R;
import net.hunme.message.bean.RyUserInfor;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * 作者： wh
 * 时间： 2016/7/15
 * 名称：聊天--联系人详情页面
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class PersonDetailActivity  extends BaseActivity implements View.OnClickListener, OkHttpListener {
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
    /**
     * 用户名
     */
    private  String username;
    /**
     * 用户id
     */
    private String userid;
    /**
     * 头像地址
     */
    private String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persondatail);
        initview();
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
    }
    /**
     * 初始化界面数据
     */
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
        Intent intent = getIntent();
        username = intent.getStringExtra("title");
        userid = intent.getStringExtra("targetId");
        tv_pname.setText(username);
        getUserInfor(userid);
    }
    /**
     * 获取用户详情
     */
    private void  getUserInfor(String userid){
        Map<String,Object> param = new HashMap<>();
        param.put("tsId",userid);
        Type  type = new  TypeToken<Result<RyUserInfor>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.MESSAGE_GETDETAIL,param,this,2,"contract_person");

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
            if (RongIM.getInstance()!=null){
                RongIM.getInstance().startPrivateChat(this,userid,username);
                RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
                    @Override
                    public UserInfo getUserInfo(String userId) {
                        if(null!=image){
                            return  new UserInfo(userid,username, Uri.parse(image));
                        }
                       return null;
                    }
                }, true);
                if(null!=image){
                    RongIM.getInstance().refreshUserInfoCache(new UserInfo(userid, username, Uri.parse(image)));
                }
            }
        }
    }

    @Override
    public void onSuccess(String uri, Object date) {
        Result<RyUserInfor> UserInfor = (Result<RyUserInfor>) date;
        if (UserInfor!=null){
            if (UserInfor.isSuccess()){
                RyUserInfor ryUserInfor= UserInfor.getData();
                tv_school.setText(ryUserInfor.getSchoolName());
                tv_role.setText(ryUserInfor.getTsName());
                tv_phone.setText(ryUserInfor.getPhone());
                tv_class.setText(ryUserInfor.getClassName());
                image = ryUserInfor.getImg();
                ImageCache.imageLoader(ryUserInfor.getImg(),iv_phead);
            }
        }

    }
    @Override
    public void onError(String uri, String error) {
        Toast.makeText(PersonDetailActivity.this,error,Toast.LENGTH_SHORT).show();
    }
}
