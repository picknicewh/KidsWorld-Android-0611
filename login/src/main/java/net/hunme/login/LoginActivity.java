package net.hunme.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.hunme.baselibrary.BaseLibrary;
import net.hunme.baselibrary.activity.UpdateMessageActivity;
import net.hunme.baselibrary.mode.Result;
import net.hunme.baselibrary.network.OkHttpListener;
import net.hunme.baselibrary.network.OkHttps;
import net.hunme.baselibrary.util.EncryptUtil;
import net.hunme.baselibrary.util.FormValidation;
import net.hunme.baselibrary.util.G;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.login.mode.CharacterSeleteVo;
import net.hunme.login.util.UserAction;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, OkHttpListener {
    private EditText ed_username;
    private EditText ed_password;
    private Button b_login;
    private final String APPLOGIN="/app/login.do";
    private String username;
    private String password;
    private TextView tv_unpassword;
    private static final String SELECTUSER="/app/selectUser.do";
    private CharacterSeleteVo data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        initWindow();
        setContentView(R.layout.activity_login);
        BaseLibrary.addActivity(this);
        initView();
    }

    private void initView(){
        ed_username= (EditText) findViewById(R.id.ed_username);
        ed_password= (EditText) findViewById(R.id.ed_password);
        tv_unpassword= (TextView) findViewById(R.id.tv_unpassword);
        b_login= (Button) findViewById(R.id.b_login);
        b_login.setOnClickListener(this);
        tv_unpassword.setOnClickListener(this);
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
        }
    }

    /**
     * 访问服务器
     */
    private void isGoLogin(){
        username=ed_username.getText().toString().trim();
        password=ed_password.getText().toString().trim();
        if(G.isEmteny(username)||G.isEmteny(password)){
            G.showToast(this,"账号密码不能为空");
            return;
        }
//        if(!FormValidation.isMobileNO(username)){
//            G.showToast(this,"您输入的账号不规范");
//            return;
//        }
        b_login.setEnabled(false);
        Map<String,Object>map=new HashMap<>();
        map.put("accountId",username);
        map.put("password", EncryptUtil.getBase64(password+"hunme"+(int)(Math.random()*900)+100));
        Type type =new TypeToken<Result<List<CharacterSeleteVo>>>(){}.getType();
        OkHttps.sendPost(type,APPLOGIN,map,this);
    }

    //用户选择提交用户ID
    public static void selectUserSubmit(String tsid, OkHttpListener listener){
        Map<String,Object>map=new HashMap<>();
        map.put("tsId",tsid);
        Type type =new TypeToken<Result<String>>(){}.getType();
        OkHttps.sendPost(type,SELECTUSER,map,listener);
    }


    @Override
    public void onSuccess(String uri, Object date) {
        if(APPLOGIN.equals(uri)){
            b_login.setEnabled(true);
            Result<List<CharacterSeleteVo>> result= (Result<List<CharacterSeleteVo>>) date;
            List<CharacterSeleteVo> seleteList=result.getData();
            //将用户信息json串保存起来，提供用户多个身份选择
            UserMessage.getInstance(this).setUserMessagejsonCache(new Gson().toJson(seleteList));
            UserMessage.getInstance(this).setCount(result.getData().size());
           if(result.getData().size()>1){
                startActivity(new Intent(this,UserChooseActivity.class));
                finish();
           }else{
                data=seleteList.get(0);
                selectUserSubmit(data.getTsId(),this);
                UserChooseActivity.flag = 1;

            }
            UserAction.saveLoginMessage(this,username,password);
        }else if(SELECTUSER.equals(uri)){
            String sex;
            if(data.getSex()==1){
                sex="男";
            }else{
                sex="女";
            }
            UserAction.saveUserMessage(this,data.getName(),
                    data.getImg(),data.getClassName(),data.getSchoolName(),
                    data.getRyId(),data.getTsId(),data.getType(),sex,data.getSignature());
            //如果网络连接时，连接融云
            if (G.isNetworkConnected(this)) {
                BaseLibrary.connect(data.getRyId(), LoginActivity.this, data.getName(), data.getImg());
            }
            G.KisTyep.isChooseId=true;
            finish();
            G.log("用户选择身份成功----");
        }
    }

    @Override
    public void onError(String uri, String error) {
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
