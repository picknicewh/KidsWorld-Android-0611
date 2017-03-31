package net.hongzhang.login.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.umeng.analytics.MobclickAgent;
import net.hongzhang.baselibrary.BaseLibrary;
import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.util.EncryptUtil;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.login.R;
import net.hongzhang.login.UserChooseAdapter;
import net.hongzhang.login.mode.CharacterSeleteVo;
import net.hongzhang.login.presenter.UserChooseContract;
import net.hongzhang.login.presenter.UserChoosePresenter;
import net.hongzhang.login.util.SignUtil;
import net.hongzhang.login.util.UserAction;
import java.util.ArrayList;
import java.util.List;

public class UserChooseActivity extends BaseActivity implements UserChooseContract.View, View.OnClickListener {
    private ListView lv_user_choose;
    private UserChooseAdapter adapter;
    private UserMessage um;
    private List<CharacterSeleteVo> characterSeleteVoList;
    public static int flag = 0;
    private boolean isGoBack; //判断按返回键是返回还是保存用户信息跳入主页面  如果是用户设置进入的话 直接返回 如果是登录进来的话 默认选择跳入主页面
    private UserChoosePresenter presenter;
    private CharacterSeleteVo characterSeleteVo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_choose);
        initView();
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setCententTitle("选择账号");
        setLiftOnClickListener(this);
    }
    private void initView() {
        um = UserMessage.getInstance(this);
        characterSeleteVo = new CharacterSeleteVo();
        characterSeleteVoList = new ArrayList<>();
        lv_user_choose = $(R.id.lv_user_choose);
        isGoBack = getIntent().getBooleanExtra("type", false);
        presenter = new UserChoosePresenter(UserChooseActivity.this, this);
        presenter.getIsGologin(um.getLoginName(), EncryptUtil.getBase64(um.getPassword()+"hunme"+(int)(Math.random()*900)+100), SignUtil.getSign(Apiurl.APPLOGIN));
    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (!isGoBack && G.isEmteny(um.getUserName())) {
                // 如果用户没有点击选择默认选择第一个身份
                characterSeleteVo = characterSeleteVoList.get(0);
                presenter.selectUserSubmit(characterSeleteVo.getTsId(), SignUtil.getSign(Apiurl.SELECTUSER));
                dialog.show();
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MobclickAgent.onEvent(this, "openChangeAccount");
    }

    @Override
    public void setCharacterVoList(final List<CharacterSeleteVo> characterSeleteVos) {
        characterSeleteVoList = characterSeleteVos;
        adapter = new UserChooseAdapter(this, characterSeleteVos);
        lv_user_choose.setAdapter(adapter);
        lv_user_choose.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                characterSeleteVo = characterSeleteVos.get(position);
                presenter.selectUserSubmit(characterSeleteVo.getTsId(), SignUtil.getSign(Apiurl.SELECTUSER));
                um.setSelectFlag(position);
            }
        });
        if(isGoBack){
            for (int i=0;i<characterSeleteVos.size();i++){
                if(um.getUserName().equals(characterSeleteVos.get(i).getName())){
                    adapter.setSelectPosition(um.getSelectFlag());
                }
            }
        }
    }
    @Override
    public void setIsChoose(boolean isChoose) {
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
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.iv_left) {
            if (!isGoBack && G.isEmteny(um.getUserName())) {
                // 如果用户没有点击选择默认选择第一个身份
                characterSeleteVo = characterSeleteVoList.get(0);
                presenter.selectUserSubmit(characterSeleteVo.getTsId(), SignUtil.getSign(Apiurl.SELECTUSER));
            } else {
                finish();
            }
        }
    }
}
