package net.hongzhang.message.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.image.ImageCache;
import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.DetaiCodeUtil;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.message.R;
import net.hongzhang.message.adapter.PhoneListAdapter;
import net.hongzhang.message.bean.RyUserInfor;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;


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
   // private ImageView iv_pcall;
    /**
     * 短信
     */
  //  private ImageView iv_pmessage;
    /**
     * 号码
     */
 //   private TextView tv_phone;
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
   // private TextView tv_pname;
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
    /**
     * 电话号码
     */
    private String phone;
    private RecyclerView recyclerView;
    private PhoneListAdapter adapter;
    private RelativeLayout rl_nonetwork;
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
       // iv_pcall = $(R.id.iv_pcall);
      //  tv_pname= $(R.id.tv_pname);
       // iv_pmessage = $(R.id.iv_pmessage);
        // tv_phone = $(R.id.tv_phone);
        iv_phead = $(R.id.iv_phead);
        recyclerView = $(R.id.rv_phone_list);
        tv_role = $(R.id.tv_role);
        tv_class = $(R.id.tv_pclass);
        tv_school = $(R.id.tv_school);
        rl_nonetwork=  $(R.id.rl_nonetwork);
       // iv_pcall.setOnClickListener(this);
      //  iv_pmessage.setOnClickListener(this);
        Intent intent = getIntent();
      //  username = intent.getStringExtra("title");
        userid = intent.getStringExtra("targetId");
       // tv_pname.setText(username);
        getUserInfor(userid);
        rl_nonetwork.setVisibility(G.isNetworkConnected(this)?View.GONE:View.VISIBLE);
    }
    /**
     * 获取用户详情
     */
    private void  getUserInfor(String userid){
        Map<String,Object> param = new HashMap<>();
        param.put("tsId",userid);
        Type  type = new  TypeToken<Result<RyUserInfor>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.MESSAGE_GETDETAIL,param,this);
    }
    @Override
    public void onClick(View v) {

        int viewId = v.getId();
      /*  if (viewId== iv_pcall){
            ConformDialog dialog  = new ConformDialog(this,phone);
            dialog.initView();
            *//*if (!G.isEmteny(phone)){
                Uri phoneUri =  Uri.parse("tel:"+phone);
                Intent intent = new Intent(Intent.ACTION_DIAL,phoneUri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }*//*
        }else if (viewId== iv_pmessage){
            if (!G.isEmteny(userid)&&!G.isEmteny(username)){
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
                finish();
            }
        }*/
    }

    @Override
    public void onSuccess(String uri, Object date) {
        Result<RyUserInfor> UserInfor = (Result<RyUserInfor>) date;
        if (UserInfor!=null){
                RyUserInfor ryUserInfor= UserInfor.getData();
                adapter = new PhoneListAdapter(this,ryUserInfor,userid);
                LinearLayoutManager manager  =new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(adapter);
                tv_school.setText(ryUserInfor.getSchoolName());
                tv_class.setText(ryUserInfor.getClassName());
                ImageCache.imageLoader(ryUserInfor.getImg(),iv_phead);
            //    username =ryUserInfor.getTsName();
               // tv_pname.setText(ryUserInfor.getTsName());
           //     image = ryUserInfor.getImg();
                //phone = ryUserInfor.getAccount_info().get(0).getPhone();
               /* StringBuffer buffer = new StringBuffer();
                for (int i = 0 ; i <ryUserInfor.getAccount_info().size();i++){
                    RyUserInfor.AccountInfo  accountInfo = ryUserInfor.getAccount_info().get(i);
                    buffer.append(accountInfo.getName()).append("  ").append(accountInfo.getPhone()).append("\n");
                }*/
             //   tv_phone.setText(buffer.toString());

        }
    }
    @Override
    public void onError(String uri, Result error) {
        DetaiCodeUtil.errorDetail(error,this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode== ConservationActivity.PERSONDETAIL){
            userid = data.getStringExtra("targetId");
        }
    }
}
