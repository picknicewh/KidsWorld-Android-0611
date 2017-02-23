package net.hongzhang.baselibrary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.R;
import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.EncryptUtil;
import net.hongzhang.baselibrary.util.FormValidation;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.UserMessage;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class UpdateMessageActivity extends BaseActivity implements View.OnClickListener, OkHttpListener {
    private EditText et_heckNumber;
    private EditText et_password;
    private Button b_finish;
 //   private TextView tv_time;
    private TextView tv_cp_number;
    /**
     * 获取验证码
     */
    private Button btn_checkcode;
    private UserMessage um;
    private TextView tv_type;
    private final String VALIDATECODE="/appUser/validateCode.do";
    private final String UPDATEPASSWORD="/appUser/updataPassword.do";
    private final String UPDATEPHONE="/appUser/updataPhone.do";
    private String type;
    private MyCount myCount;
    private boolean isSubmitDate;//是否需要提交数据
    private String Sign;//验证码
    private String value;//新密码或者新手机号
    private String phoneNum;//手机号
    private LinearLayout ll_code; //获取验证码页面
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
        btn_checkcode = $(R.id.btn_checkcode);
        tv_cp_number=$(R.id.tv_cp_number);
        tv_type=$(R.id.tv_type);
        ll_code=$(R.id.ll_code);
        b_finish.setOnClickListener(this);
        btn_checkcode.setOnClickListener(this);
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
            et_password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            b_finish.setText("完成");
            b_finish.setEnabled(false);
            phoneNum=getIntent().getStringExtra("phoneNumber");
            tv_cp_number.setText("+86 "+phoneNum.substring(0,3)+"****"+phoneNum.substring(phoneNum.length()-4,phoneNum.length()));
        }else{
            type="1";
            setCententTitle("修改手机号");
            et_password.setHint("请输入你的新号码");
            b_finish.setText("确定");
            tv_type.setVisibility(View.GONE);
            tv_cp_number.setVisibility(View.GONE);
            et_heckNumber.setVisibility(View.GONE);
            btn_checkcode.setVisibility(View.GONE);
            ll_code.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btn_checkcode){
            getValidateCode(type,phoneNum);
            btn_checkcode.setEnabled(false);
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
                    if(!FormValidation.isPassword(value)){
                        G.showToast(this,"输入的密码由数字和字母组成,长度在6-16位");
                        return;
                    }
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
     * 更新密码
     * @param sign 密钥 有服务端提供来源请求的验证码接口
     * @param code 验证码
     * @param value 更新的值
     */
    private void updatePassword(String sign, String code, String value){
        Map<String,Object>map=new HashMap<>();
        map.put("password", EncryptUtil.getBase64(value+"hunme"+(int)(Math.random()*900)+100));
        map.put("code",code);
        map.put("sign",sign);
        Type mtype=new TypeToken<Result<String>>(){}.getType();
        OkHttps.sendPost(mtype,UPDATEPASSWORD,map,this);
    }

    /**
     * 修改电话号码
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
            b_finish.setEnabled(true);
            //验证码
            Sign=result.getSign();
            if(type.equals("1")&&!isSubmitDate){
                isSubmitDate=true;
                et_heckNumber.setVisibility(View.VISIBLE);
                et_password.setVisibility(View.GONE);
                ll_code.setVisibility(View.VISIBLE);
            }else if(type.equals("2")){
                tv_type.setText("我们已经发送短信验证码到你的手机");
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
        if(VALIDATECODE.equals(uri)){
            myCount.onFinish();
            myCount.cancel();
        }
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
            btn_checkcode.setEnabled(true);
            btn_checkcode.setText("获取验证码");
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btn_checkcode.setText("重新获取(" + millisUntilFinished / 1000 + ")秒");
        }
    }

}
