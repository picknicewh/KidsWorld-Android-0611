package net.hongzhang.message.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.image.ImageCache;
import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.DetaiCodeUtil;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.message.R;
import net.hongzhang.message.adapter.PhoneListAdapter;
import net.hongzhang.message.bean.RyUserInfo;
import net.hongzhang.message.widget.ConformDialog;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

import static net.hongzhang.message.R.id.iv_message;


/**
 * 作者： wh
 * 时间： 2016/7/15
 * 名称：聊天--联系人详情页面
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class PersonDetailActivity extends BaseActivity implements View.OnClickListener, OkHttpListener {
    /**
     * 电话
     */
    private ImageView iv_call;
    /**
     * 短信
     */
    private ImageView iv_pmessage;
    /**
     * 号码
     */
    private TextView tv_phone;
    /**
     * 学校
     */
    private TextView tv_school;
    /**
     * 班级
     */
    private TextView tv_class;
    /**
     * 头像
     */
    private ImageView iv_head;
    /**
     * 姓名
     */
    private TextView tv_name;
    private TextView tv_ts_name;
    /**
     * 角色
     */
    private TextView tv_role;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户id
     */
    private String userid;
    /**
     * 头像地址
     */
    private String image;
    /**
     * 电话号码
     */
    private String phone;
    private RecyclerView recyclerView;
    private PhoneListAdapter adapter;
    private RelativeLayout rl_nonetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persondatail);
        initview();
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
    }

    /**
     * 初始化界面数据
     */
    private void initview() {
        iv_call = $(R.id.iv_call);
        tv_ts_name = $(R.id.tv_ts_name);
        iv_pmessage = $(R.id.iv_message);
        tv_phone = $(R.id.tv_phone);
        iv_head = $(R.id.iv_head);
        tv_name = $(R.id.tv_name);
        recyclerView = $(R.id.rv_phone_list);
        tv_role = $(R.id.tv_role);
        tv_class = $(R.id.tv_pclass);
        tv_school = $(R.id.tv_school);
        rl_nonetwork = $(R.id.rl_nonetwork);
        iv_call.setOnClickListener(this);
        iv_pmessage.setOnClickListener(this);
        Intent intent = getIntent();
        //  username = intent.getStringExtra("title");
        userid = intent.getStringExtra("targetId");
        getUserInfor(userid);
        rl_nonetwork.setVisibility(G.isNetworkConnected(this) ? View.GONE : View.VISIBLE);
    }

    /**
     * 获取用户详情
     */
    private void getUserInfor(String userid) {
        Map<String, Object> param = new HashMap<>();
        param.put("tsId", userid);
        Type type = new TypeToken<Result<RyUserInfo>>() {
        }.getType();
        OkHttps.sendPost(type, Apiurl.MESSAGE_GETTSINFO, param, this);
    }

    @Override
    public void onClick(View v) {

        int viewId = v.getId();
        if (viewId == R.id.iv_call) {
            ConformDialog dialog = new ConformDialog(this, phone);
            dialog.initView();
            if (!G.isEmteny(phone)) {
                Uri phoneUri = Uri.parse("tel:" + phone);
                Intent intent = new Intent(Intent.ACTION_DIAL, phoneUri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        } else if (viewId == iv_message) {
            if (!G.isEmteny(userid) && !G.isEmteny(username)) {
                if (RongIM.getInstance() != null) {
                    RongIM.getInstance().startPrivateChat(this, userid, username);
                    RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
                        @Override
                        public UserInfo getUserInfo(String userId) {
                            if (null != image) {
                                return new UserInfo(userid, username, Uri.parse(image));
                            }
                            return null;
                        }
                    }, true);
                    if (null != image) {
                        RongIM.getInstance().refreshUserInfoCache(new UserInfo(userid, username, Uri.parse(image)));
                    }
                }
                finish();
            }
        }
    }

    @Override
    public void onSuccess(String uri, Object date) {
        Result<RyUserInfo> UserInfor = (Result<RyUserInfo>) date;
        if (UserInfor != null) {
            RyUserInfo ryUserInfo = UserInfor.getData();
            username = ryUserInfo.getTsName();
            image = ryUserInfo.getImgUrl();
            tv_school.setText(ryUserInfo.getSchoolName());
            tv_class.setText(ryUserInfo.getClassName());
            ImageCache.imageLoader(ryUserInfo.getImgUrl(), iv_head);
            tv_phone.setText(ryUserInfo.getPhone());
            tv_name.setText(ryUserInfo.getName());
            tv_ts_name.setText(ryUserInfo.getTsName());
            // username =ryUserInfor.getTsName();
            // tv_pname.setText(ryUserInfor.getTsName());
            // image = ryUserInfor.getImg();
            //phone = ryUserInfor.getAccount_info().get(0).getPhone();
            // StringBuffer buffer = new StringBuffer();
            //for (int i = 0 ; i <ryUserInfor.getAccount_info().size();i++){
            // RyUserInfor.AccountInfo  accountInfo = ryUserInfor.getAccount_info().get(i);
            //  buffer.append(accountInfo.getName()).append("  ").append(accountInfo.getPhone()).append("\n");
            // }
            //   tv_phone.setText(buffer.toString());

        }
    }

    @Override
    public void onError(String uri, Result error) {
        DetaiCodeUtil.errorDetail(error, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ConservationActivity.PERSONDETAIL) {
            userid = data.getStringExtra("targetId");
        }
    }
}
