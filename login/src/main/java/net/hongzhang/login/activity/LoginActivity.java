package net.hongzhang.login.activity;

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
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.util.EncryptUtil;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.baselibrary.widget.LoadingDialog;
import net.hongzhang.login.R;
import net.hongzhang.login.mode.CharacterSeleteVo;
import net.hongzhang.login.presenter.LoginContact;
import net.hongzhang.login.presenter.LoginPresenter;
import net.hongzhang.login.util.SignUtil;
import net.hongzhang.login.util.UserAction;

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

    private void initData() {
        presenter = new LoginPresenter(LoginActivity.this, this);
        userMessage = UserMessage.getInstance(this);
        characterSeleteVo = new CharacterSeleteVo();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.b_login) {
            username = ed_username.getText().toString();
            password = ed_password.getText().toString();
            presenter.getIsGologin(checkBox, username,
                    EncryptUtil.getBase64(password + "hunme" + (int) (Math.random() * 900) + 100), SignUtil.getSign(Apiurl.APPLOGIN));
        } else if (i == R.id.tv_unpassword) {
            String phoneNumber = ed_username.getText().toString();
            presenter.goUpdateMessageActivity(phoneNumber);
        } else if (i == R.id.tv_agree) {
            String url = "file:///android_asset/userAgreement.html";
            presenter.goUserAgreementActivity(url, 4);
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
            Intent intent = new Intent(this, UserChooseActivity.class);
            intent.putExtra("source", "ShareActivity");
            intent.putExtra("extra", getIntent().getStringExtra("extra"));
            startActivity(intent);
            finish();
        } else {
            characterSeleteVo = characterSeleteVos.get(0);
            presenter.selectUserSubmit(characterSeleteVo.getTsId());
        }
        UserAction.saveLoginMessage(this, username, password);
    }

    @Override
    public void setIsOneRole(boolean isChoose) {
        if (isChoose) {
            //如果source不为空，表示分享时，并没有登陆，从而进入登陆页面，登陆成功且角色选择成功后，再次进入分享页面，继续分享。
            String source = getIntent().getStringExtra("source");
            if (!G.isEmteny(source)) {
                if (source.equals("ShareActivity")) {
                    UserAction.goShareActivity(this, getIntent().getStringExtra("extra"));
                }
            }
            String sex;
            if (characterSeleteVo.getSex() == null || characterSeleteVo.getSex() == 1) sex = "男";
            else sex = "女";
            UserAction.saveUserMessage(this, characterSeleteVo.getName(), characterSeleteVo.getImg(), characterSeleteVo.getClassName(), characterSeleteVo.getSchoolName(),
                    characterSeleteVo.getRyId(), characterSeleteVo.getTsId(), characterSeleteVo.getType(), sex, characterSeleteVo.getSignature(), characterSeleteVo.getAccount_id());
            //如果网络连接时，连接融云
            if (G.isNetworkConnected(this)) {
                BaseLibrary.connect(characterSeleteVo.getRyId(), this, characterSeleteVo.getName(), characterSeleteVo.getImg());
            }
            G.KisTyep.isChooseId = true;
            UserAction.goMainActivity(this);
        }
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
