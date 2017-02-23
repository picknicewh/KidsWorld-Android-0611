package net.hongzhang.login;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import net.hongzhang.baselibrary.BaseLibrary;
import net.hongzhang.baselibrary.activity.UpdateMessageActivity;
import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.EncryptUtil;
import net.hongzhang.baselibrary.util.FormValidation;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.baselibrary.widget.LoadingDialog;
import net.hongzhang.baselibrary.widget.PromptPopWindow;
import net.hongzhang.login.mode.CharacterSeleteVo;
import net.hongzhang.login.util.UserAction;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, OkHttpListener {
    private EditText ed_username;
    private EditText ed_password;
    private CheckBox checkBox;
    private TextView tv_agree;
    private Button b_login;
    private String username;
    private String password;
    private TextView tv_unpassword;
    private CharacterSeleteVo data;
    private LoadingDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar(Color.parseColor("#fafafa"));
        setContentView(R.layout.activity_login);
        BaseLibrary.addActivity(this);

        initView();
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setStatusBar(int color){
        //设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(color);
        }
    }
    private void initView(){
        ed_username= (EditText) findViewById(R.id.ed_username);
        ed_password= (EditText) findViewById(R.id.ed_password);
        tv_unpassword= (TextView) findViewById(R.id.tv_unpassword);
        checkBox = (CheckBox)findViewById(R.id.checkbox);
        b_login= (Button) findViewById(R.id.b_login);
        tv_agree= (TextView) findViewById(R.id.tv_agree);
        b_login.setOnClickListener(this);
        tv_unpassword.setOnClickListener(this);
        tv_agree.setOnClickListener(this);
        dialog=new LoadingDialog(this,R.style.LoadingDialogTheme);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i ==R.id.b_login) {
            isGoLogin();
        }else if(i==R.id.tv_unpassword){
            String phoneNumber=ed_username.getText().toString();
            if (!FormValidation.isMobileNO(phoneNumber)){
                G.showToast(this,"请先输入正确的手机号码");
                return;
            }
            Intent intent = new Intent();
            intent.putExtra("type", "pw");
            intent.putExtra("phoneNumber",phoneNumber);
            intent.setClass(this, UpdateMessageActivity.class);
            startActivity(intent);
        }else if (i==R.id.tv_agree){
            Intent intent  = new Intent();
            ComponentName componetName = new ComponentName("net.hongzhang.bbhow","net.hongzhang.user.activity.TextContentActivity");
            intent.setComponent(componetName);
            intent.putExtra("source",2);
            startActivity(intent);
        }
    }
    /**
     * 访问服务器
     */
    private void isGoLogin(){
        username=ed_username.getText().toString().trim();
        password=ed_password.getText().toString().trim();
        if (!checkBox.isChecked()){
            G.initDisplaySize(this);
            PromptPopWindow promptPopWindow = new PromptPopWindow(this,"请同意服务条款");
            promptPopWindow.showAtLocation(checkBox, Gravity.NO_GRAVITY,  (G.size.W-promptPopWindow.getWidth())/2, (int) (G.size.H*0.2));
            return;
        }
        if(G.isEmteny(username)||G.isEmteny(password)){
            G.showToast(this,"账号密码不能为空");
            return;
        }
        if(!FormValidation.isMobileNO(username)){
            G.showToast(this,"您输入的账号不规范");
            return;
        }
        b_login.setEnabled(false);
        Map<String,Object>map=new HashMap<>();
        map.put("accountId",username);
        map.put("password", EncryptUtil.getBase64(password+"hunme"+(int)(Math.random()*900)+100));
        Type type =new TypeToken<Result<List<CharacterSeleteVo>>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.APPLOGIN,map,this);
        dialog.show();
    }

    //用户选择提交用户ID
    public static void selectUserSubmit(String tsid, OkHttpListener listener){
        Map<String,Object>map=new HashMap<>();
        map.put("tsId",tsid);
        Type type =new TypeToken<Result<String>>(){}.getType();
        OkHttps.sendPost(type,Apiurl.SELECTUSER,map,listener);
    }

    @Override
    public void onSuccess(String uri, Object date) {
        if(Apiurl.APPLOGIN.equals(uri)){

            MobclickAgent.onProfileSignIn(EncryptUtil.getBase64(username));
            b_login.setEnabled(true);
            Result<List<CharacterSeleteVo>> result= (Result<List<CharacterSeleteVo>>) date;
            List<CharacterSeleteVo> seleteList=result.getData();
            //将用户信息json串保存起来，提供用户多个身份选择
            UserMessage.getInstance(this).setUserMessagejsonCache(new Gson().toJson(seleteList));
            UserMessage.getInstance(this).setCount(result.getData().size());
           if(result.getData().size()>1){
                dialog.dismiss();
                startActivity(new Intent(this,UserChooseActivity.class));
                finish();
           }else{
                data=seleteList.get(0);
                selectUserSubmit(data.getTsId(),this);
                UserChooseActivity.flag = 1;
            }
            UserAction.saveLoginMessage(this,username,password);

        }else if(Apiurl.SELECTUSER.equals(uri)){
            String sex;
            if (data.getSex()!=null){
                if(data.getSex()==1){
                    sex="男";
                }else{
                    sex="女";
                }
            }else {
                sex="男";
            }
            UserAction.saveUserMessage(this,data.getName(),
                    data.getImg(),data.getClassName(),data.getSchoolName(),
                    data.getRyId(),data.getTsId(),data.getType(),sex,data.getSignature(),data.getAccount_id());
            //如果网络连接时，连接融云
            if (G.isNetworkConnected(this)) {
                BaseLibrary.connect(data.getRyId(), LoginActivity.this, data.getName(), data.getImg());
            }
            G.KisTyep.isChooseId=true;
            dialog.dismiss();
            finish();
            UserAction.goMainActivity(this);
        }

    }

    @Override
    public void onError(String uri, String error) {
        if( dialog!=null){
            dialog.dismiss();
        }
        b_login.setEnabled(true);
        G.showToast(this,error);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        //监听返回按钮
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            BaseLibrary.exit();
            finish();
            return false;
        }
        return super.dispatchKeyEvent(event);
    }

}
