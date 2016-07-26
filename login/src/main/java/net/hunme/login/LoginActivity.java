package net.hunme.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, OkHttpListener {
    private EditText ed_username;
    private EditText ed_password;
    private Button b_login;
    private final String appLogin="/appLogin.do";
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
        Map<String,Object>map=new HashMap<>();
        map.put("accountId",username);
        map.put("password", EncryptUtil.encodeMD5String(password));
        Type type =new TypeToken<Result<CharacterSeleteVo>>(){}.getType();
        OkHttps.sendPost(type,appLogin,map,this);
    }

    @Override
    public void onSuccess(String uri, Object date) {
        if(appLogin.equals(uri)){
            Result<CharacterSeleteVo> result= (Result<CharacterSeleteVo>) date;
            G.showToast(this,result.getCode());
            if(result.isSuccess()){
                CharacterSeleteVo data=result.getData();
                UserMessage um=UserMessage.getInstance(this);
                CharacterSeleteVo.characterSelete selete=data.getJsonList().get(0);
                um.setLoginName(username);
                um.setTsId(selete.getTsId());
                um.setHoldImgUrl(selete.getImg());
                um.setUserName(selete.getName());
                um.setClassName(selete.getClassName());
                um.setSchoolName(selete.getSchoolName());
                um.setType(selete.getType());
                finish();
            }else{
                G.showToast(this,"登陆失败，请再次登陆");
            }
        }
    }

    @Override
    public void onError(String uri, String error) {
        G.log(error);
        G.showToast(this,"登陆失败，请检查您的网络");
    }

}
