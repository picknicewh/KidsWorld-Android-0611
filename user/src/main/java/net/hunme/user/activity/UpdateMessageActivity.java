package net.hunme.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.baselibrary.mode.Result;
import net.hunme.baselibrary.network.OkHttpListener;
import net.hunme.baselibrary.network.OkHttps;
import net.hunme.baselibrary.util.FormValidation;
import net.hunme.baselibrary.util.G;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.user.R;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class UpdateMessageActivity extends BaseActivity implements View.OnClickListener, OkHttpListener {
    private EditText et_heckNumber;
    private EditText et_password;
    private Button b_finish;
    private TextView tv_time;
    private TextView tv_cp_number;
    private UserMessage um;
    private TextView tv_type;
    private final String VALIDATECODE="/appUser/validateCode.do";
    private final String UPDATEPASSWORD="/appUser/updataPassword.do";
    private final String UPDATEPHONE="/appUser/updataPhone.do";
    private String type;
    private MyCount myCount;
    private boolean isSubmitDate;
    private String Sign;
    private String value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_message);
        initView();
        initData();
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
    }

    private void initView(){
        et_heckNumber=$(R.id.et_heckNumber);
        et_password=$(R.id.et_password);
        b_finish=$(R.id.b_finish);
        tv_time=$(R.id.tv_time);
        tv_cp_number=$(R.id.tv_cp_number);
        tv_type=$(R.id.tv_type);
        b_finish.setOnClickListener(this);
        tv_time.setOnClickListener(this);
//        b_finish.setEnabled(false);
    }

    private void initData(){
        um=UserMessage.getInstance(this);
        myCount=new MyCount(60000,1000);
        Intent intent  =getIntent();
        type=intent.getStringExtra("type");
        isSubmitDate=false;
        if("pw".equals(type)){
            type="2";
            setCententTitle("修改密码");
            et_password.setHint("请输入你的密码");
            b_finish.setText("完成");
        }else{
            type="1";
            setCententTitle("修改手机号");
            et_password.setHint("请输入你的新号码");
            b_finish.setText("确定");
            tv_type.setVisibility(View.GONE);
            tv_cp_number.setVisibility(View.GONE);
            et_heckNumber.setVisibility(View.GONE);
            tv_time.setVisibility(View.GONE);
        }
        tv_cp_number.setText("+86 "+um.getLoginName());
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.tv_time){
            getValidateCode(type,"15158835049");
            tv_time.setEnabled(false);
            tv_type.setText("我们已经发送短信验证码到你的手机");
            myCount.start();
        }else if(view.getId()==R.id.b_finish){
            if(type.equals("1")&&!isSubmitDate){
                String phoneNumber=et_password.getText().toString().trim();
                if(!FormValidation.isMobileNO(phoneNumber)){
                    G.showToast(UpdateMessageActivity.this,"手机号码不符合规范");
                    return;
                }
                getValidateCode(type,phoneNumber);
            }else{
                String code=et_heckNumber.getText().toString().trim();
                 value=et_password.getText().toString().trim();
                if(G.isEmteny(code)||G.isEmteny(value)){
                    G.showToast(this,"提交的数据不能为空");
                    return;
                }
                if(type.equals("1")){
                    updatePhone(Sign,code,value);
                }else{
                    updatePassword(Sign,code,value);
                }
            }
        }
    }

    /**
     * 获取验证码
     * @param type
     */
    private void getValidateCode(String type,String phoneNumber){
        Map<String,Object>map=new HashMap<>();
        map.put("phone",phoneNumber);
        map.put("type",type);
        Type mtype=new TypeToken<Result<String>>(){}.getType();
        OkHttps.sendPost(mtype,VALIDATECODE,map,this);
    }

    /**
     * 更新信息
     * @param sign 密钥 有服务端提供来源请求的验证码接口
     * @param code 验证码
     * @param value 更新的值
     */
    private void updatePassword(String sign, String code, String value){
        Map<String,Object>map=new HashMap<>();
        map.put("password",value);
        map.put("code",code);
        map.put("sign",sign);
        Type mtype=new TypeToken<Result<String>>(){}.getType();
        OkHttps.sendPost(mtype,UPDATEPASSWORD,map,this);
    }

    /**
     *
     * @param sign 密钥 有服务端提供来源请求的验证码接口
     * @param code 验证码
     * @param newPhone 新号码
     */
    private void updatePhone(String sign,String code,String newPhone){
        Map<String,Object>map=new HashMap<>();
        map.put("newPhone",newPhone);
        map.put("code",code);
        map.put("sign",sign);
        map.put("phone",um.getLoginName());
        Type mtype=new TypeToken<Result<String>>(){}.getType();
        OkHttps.sendPost(mtype,UPDATEPHONE,map,this);
    }

    @Override
    public void onSuccess(String uri, Object date) {
        Result<String>result= (Result<String>) date;
        if(VALIDATECODE.equals(uri)){
            tv_time.setEnabled(true);
            //验证码
            Sign=result.getSign();
            if(type.equals("1")&&!isSubmitDate){
                isSubmitDate=true;
                et_heckNumber.setVisibility(View.VISIBLE);
                et_password.setVisibility(View.GONE);
            }
            b_finish.setEnabled(true);
            G.showToast(this,"验证码已发送你的手机请注意查收！");
        }else if(UPDATEPHONE.equals(uri)){
            //修改手机号码
            G.showToast(this,"手机号码修改成功");
            um.setLoginName(value);
            finish();
        }else if(UPDATEPASSWORD.equals(uri)){
            //修改密码
            G.showToast(this,"密码修改成功");
            um.setPassword(value);
            finish();
        }
    }

    @Override
    public void onError(String uri, String error) {
        G.showToast(this,error);
    }

    /**
     * 一个倒计时的内部类
     */
    private final class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            tv_time.setEnabled(true);
            tv_time.setText("获取验证码");
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tv_time.setText("重新获取(" + millisUntilFinished / 1000 + ")秒");
        }
    }

}
