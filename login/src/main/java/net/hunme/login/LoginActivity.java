package net.hunme.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.hunme.baselibrary.BaseLibrary;
import net.hunme.baselibrary.mode.Result;
import net.hunme.baselibrary.network.OkHttpListener;
import net.hunme.baselibrary.network.OkHttps;
import net.hunme.baselibrary.util.EncryptUtil;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        BaseLibrary.addActivity(this);
        initView();
    }

    private void initView(){
        ed_username= (EditText) findViewById(R.id.ed_username);
        ed_password= (EditText) findViewById(R.id.ed_password);
        b_login= (Button) findViewById(R.id.b_login);
        b_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i ==R.id.b_login) {
            isGoLogin();
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

    @Override
    public void onSuccess(String uri, Object date) {
        if(APPLOGIN.equals(uri)){
            b_login.setEnabled(true);

            Result<List<CharacterSeleteVo>> result= (Result<List<CharacterSeleteVo>>) date;
            List<CharacterSeleteVo> seleteList=result.getData();
            //将用户信息json串保存起来，提供用户多个身份选择
            UserMessage.getInstance(this).setUserMessagejsonCache(new Gson().toJson(seleteList));
//                if(result.getData().size()>1){
            startActivity(new Intent(this,UserChooseActivity.class));
            UserAction.saveLoginMessage(this,username,password);
//                }else{
//                    CharacterSeleteVo data=seleteList.get(0);
//                    UserAction.saveUserMessage(this,username,data.getName(),
//                            data.getImg(),data.getClassName(),data.getSchoolName(),
//                            data.getRyId(),data.getTsId(),data.getType());
//                }
            finish();

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
