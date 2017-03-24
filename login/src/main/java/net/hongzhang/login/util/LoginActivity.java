package net.hongzhang.login.util;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import net.hongzhang.baselibrary.BaseLibrary;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.baselibrary.widget.LoadingDialog;
import net.hongzhang.login.R;
import net.hongzhang.login.UserChooseActivity;
import net.hongzhang.login.mode.CharacterSeleteVo;
import net.hongzhang.login.presenter.LoginContact;
import net.hongzhang.login.presenter.LoginPresenter;

import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, LoginContact.View {
    private EditText ed_username;
    private EditText ed_password;
    private CheckBox checkBox;
    private TextView tv_agree;
    private Button b_login;
    private String username;
    private String password;
    private CharacterSeleteVo characterSeleteVo;
    private TextView tv_unpassword;
    private LoginPresenter presenter;
    public LoadingDialog dialog;
    private UserMessage userMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        BaseLibrary.addActivity(this);
        initView();
    }
    private void initView() {
        ed_username = (EditText) findViewById(R.id.ed_username);
        ed_password = (EditText) findViewById(R.id.ed_password);
        tv_unpassword = (TextView) findViewById(R.id.tv_unpassword);
        checkBox = (CheckBox) findViewById(R.id.checkbox);
        b_login = (Button) findViewById(R.id.b_login);
        tv_agree = (TextView) findViewById(R.id.tv_agree);
        b_login.setOnClickListener(this);
        tv_unpassword.setOnClickListener(this);
        tv_agree.setOnClickListener(this);
        initData();
    }
    private void initData(){
        presenter = new LoginPresenter(LoginActivity.this,this);
        userMessage = UserMessage.getInstance(this);
        characterSeleteVo =new CharacterSeleteVo();
    }
    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.b_login) {
            username = ed_username.getText().toString();
            password=tv_unpassword.getText().toString();
        } else if (i == R.id.tv_unpassword) {
            String phoneNumber = ed_username.getText().toString();
            presenter.goUpdateMessageActivity(phoneNumber);
        } else if (i == R.id.tv_agree) {
            String url = "file:///android_asset/userAgreement.html";
            presenter.goUserAgreementActivity(url,4);
        }
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

    @Override
    public void setCharacterVoList(List<CharacterSeleteVo> characterSeleteVos) {
        //将用户信息json串保存起来，提供用户多个身份选择
        userMessage.setUserMessagejsonCache(new Gson().toJson(characterSeleteVos));
        userMessage.setCount(characterSeleteVos.size());
        if (characterSeleteVos.size() > 1) {
            startActivity(new Intent(this, UserChooseActivity.class));
            finish();
        } else {
            characterSeleteVo= characterSeleteVos.get(0);
            presenter.selectUserSubmit(characterSeleteVo.getTsId());
            UserChooseActivity.flag = 1;
        }
        UserAction.saveLoginMessage(this, username, password);
    }

    @Override
    public void showLoadingDialog() {
        if (dialog == null)
            dialog = new LoadingDialog(this, R.style.LoadingDialogTheme);
        dialog.show();
        dialog.setCancelable(true);
        dialog.setLoadingText("数据加载中...");
    }
    @Override
    public void stopLoadingDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
