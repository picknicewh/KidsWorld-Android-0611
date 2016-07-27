package net.hunme.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.hunme.baselibrary.mode.Result;
import net.hunme.baselibrary.network.OkHttpListener;
import net.hunme.baselibrary.network.OkHttps;
import net.hunme.baselibrary.util.EncryptUtil;
import net.hunme.baselibrary.util.G;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.login.mode.CharacterSeleteVo;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, OkHttpListener {
    private EditText ed_username;
    private EditText ed_password;
    private Button b_login;
    private final String APPLOGIN="/app/login.do";
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
        String password=ed_password.getText().toString().trim();
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
            if(result.isSuccess()){
//                if(result.getData().size()>1){
              startActivity(new Intent(this,UserChooseActivity.class));
//                }else{
//                    CharacterSeleteVo data=seleteList.get(0);
//                    UserAction.saveUserMessage(this,username,data.getName(),
//                            data.getImg(),data.getClassName(),data.getSchoolName(),
//                            data.getRyId(),data.getTsId(),data.getType());
//                }
                finish();
            }else{
                G.showToast(this,"登录失败，你输入的账号或者密码不符");
            }
        }
    }

    @Override
    public void onError(String uri, String error) {
        G.log(error);
        b_login.setEnabled(true);
        G.showToast(this,"登录失败，请检查您的网络");
    }

//    public boolean dispatchKeyEvent(KeyEvent event) {
//        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
////            UserAction.exit();
//            Intent startMain = new Intent(Intent.ACTION_MAIN);
//            startMain.addCategory(Intent.CATEGORY_HOME);
//            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(startMain);
//            System.exit(0);
//            return false;
//        }
//        return super.dispatchKeyEvent(event);
//    }
    /**
     * 建立与融云服务器的连接
     *
     * @param token
     */
    private void connect(String token) {
        if (getApplicationInfo().packageName.equals(LoginApplication.getCurProcessName(this))) {

            RongIM.connect(token, new RongIMClient.ConnectCallback() {
                @Override
                public void onTokenIncorrect() {}
                @Override
                public void onSuccess(String userid) {
                    if (RongIM.getInstance() != null) {
                        Log.i("wang","userid:"+userid);
                        RongIM.getInstance().setCurrentUserInfo(new UserInfo(userid, username, Uri.parse("portrait")));
                        RongIM.getInstance().setMessageAttachedUserInfo(true);
                    }
                }
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {}
            });
        }
    }
}
