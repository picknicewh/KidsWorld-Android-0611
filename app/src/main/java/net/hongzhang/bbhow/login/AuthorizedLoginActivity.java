package net.hongzhang.bbhow.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.image.ImageCache;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.baselibrary.widget.CircleImageView;
import net.hongzhang.bbhow.R;
import net.hongzhang.login.LoginActivity;
import net.hongzhang.login.activity.UserChooseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static net.hongzhang.bbhow.R.id.tv_phone_num;

/**
 * 作者： zll
 * 时间： 2017-5-27
 * 名称： 首页导航页
 * 版本说明：代码规范整改
 * 附加注释：滑动切换首页 最后一页出现前往Button
 * 主要接口：暂无
 */
public class AuthorizedLoginActivity extends BaseActivity implements AuthorizedLoginContract.View, View.OnClickListener {

    @Bind(R.id.iv_app_icon)
    ImageView ivAppIcon;
    @Bind(R.id.tv_app_name)
    TextView tvAppName;
    @Bind(R.id.civ_image)
    CircleImageView civImage;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(tv_phone_num)
    TextView tvPhoneNum;
    private UserMessage userMessage;
    private AuthorizedLoginPresenter presenter;
    private String keyNumber;
    private String packageName;
    private String activityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorized_login);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setCententTitle("贝贝虎登录");
        setLiftOnClickClose();
        if (UserMessage.getInstance(this).getCount() > 1) {
            setSubTitle("切换账号");
        }
        setSubTitleOnClickListener(this);
    }

    private void initData() {
        userMessage = UserMessage.getInstance(this);
        Intent intent = getIntent();
        keyNumber = intent.getStringExtra("keyNumber");
        packageName = intent.getStringExtra("packageName");
        activityName = intent.getStringExtra("activityName");
        if (userMessage == null) {
            goLoginActivity();
        } else {
            tvName.setText(userMessage.getUserName());
            tvPhoneNum.setText("(" + userMessage.getLoginName() + ")");
            ImageCache.imageLoader(userMessage.getHoldImgUrl(),civImage);
          //  ivAppIcon.setImageDrawable(PackageUtils.getAppIcon(this, packageName));
         //   tvAppName.setText(PackageUtils.getAppName(this, packageName));
            tvAppName.setText("趣乐园");
        }
        presenter = new AuthorizedLoginPresenter(AuthorizedLoginActivity.this, this);

    }

    private void goLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.tv_login)
    public void onViewClicked() {
        try {
            Intent intent = new Intent();
            JSONObject j = new JSONObject();
            j.put("userid", userMessage.getLoginName());
            j.put("token", "xdijwkpnmskooqssn");
            intent.putExtra("data", j.toString());
            setResult(RESULT_OK, intent);
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
      //  presenter.authorizedLogin(userMessage.getTsId(), keyNumber);
    }

    @Override
    public void setToken(String token) {

    }

    @Override
    public void onClick(View view) {
        int viewID = view.getId();
        if (viewID == R.id.tv_subtitle) {
            Intent intent = new Intent(AuthorizedLoginActivity.this, UserChooseActivity.class);
            intent.putExtra("type", true);
            startActivity(intent);
        }
    }
}
