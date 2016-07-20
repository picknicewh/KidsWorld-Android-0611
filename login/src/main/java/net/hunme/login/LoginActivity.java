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
import net.hunme.baselibrary.util.G;
import net.hunme.login.mode.CharacterSeleteVo;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, OkHttpListener {
    private EditText ed_username;
    private EditText ed_password;
    private Button b_login;
    private final String appLogin="/appLogin.do";
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
//             startActivity(new Intent(LoginActivity.this,MainActivity.class));
            isGoLogin();
//            finish();
        }
    }

    private void isGoLogin(){
        String username=ed_username.getText().toString().trim();
        String password=ed_password.getText().toString().trim();
        if(G.isEmteny(username)||G.isEmteny(password)){
            G.showToast(this,"账号密码不能为空");
            return;
        }
        Map<String,Object>map=new HashMap<>();
        map.put("accountId",username);
        map.put("password",password);
//        OkHttps.init().setClass(result).sendPost();
//        Type type= new TypeToken<Result>(){}.getType();
        Type type =new TypeToken<Result<CharacterSeleteVo>>(){}.getType();
        OkHttps.sendPost(type,"www.baidu.com",map,this);
    }

    @Override
    public void onSuccess(String uri, Object date) {

        Result<CharacterSeleteVo> result= (Result<CharacterSeleteVo>) date;
        G.showToast(this,result.getCode());
    }

    @Override
    public void onError(String uri, String error) {
        G.log(error);
    }
}
