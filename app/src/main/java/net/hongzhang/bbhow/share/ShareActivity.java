package net.hongzhang.bbhow.share;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;

import com.google.gson.Gson;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.bbhow.R;
import net.hongzhang.login.LoginActivity;
import net.hongzhang.status.mode.DynamicVo;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ShareActivity extends BaseActivity implements ShareContract.View {
    @Bind(R.id.rv_class_list)
    RecyclerView rvClassList;
    private UserMessage userMessage;
    private ShareClassListAdapter adapter;
    private ShareVo shareVo;
    private SharePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userMessage = UserMessage.getInstance(this);
        if (G.isEmteny(userMessage.getTsId())) {
            goLoginActivity();
        }
        setContentView(R.layout.activity_share);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void setToolBar() {
        setCententTitle("选择空间");
        setLiftOnClickClose();
    }
    private void goLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("source", "ShareActivity");
        intent.putExtra("extra", getIntent().getStringExtra(Intent.EXTRA_TEXT));
        startActivity(intent);
        finish();
    }
    /**
     * 获取分享的内容
     */
    private void getShareContent() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_SEND.equals(action) && "text/json".equals(type)) {
            String json = intent.getStringExtra(Intent.EXTRA_TEXT);
           // String json = "{\"type\":1,\"title\":\"\",\"text\":\"趣乐园真好玩\",\"paths\":[\"/storage/emulated/0/Android/data/cn.net.hongzhang.quleyuan_android/files/SreenCut/IMG_2017_05_16_07_11_57_3432.jpg\"]}";
            G.log("xxxxxxxxx"+json);
            shareVo = new Gson().fromJson(json, ShareVo.class);
        }
    }

    /**
     * 初始化信息
     */
    private void initData() {
        shareVo = new ShareVo();
        getShareContent();
        presenter = new SharePresenter(ShareActivity.this, this);
        presenter.getClassList(userMessage.getTsId());
    }

    @Override
    public void setClassList(final List<DynamicVo> dynamicVoList) {
        adapter = new ShareClassListAdapter(this, dynamicVoList);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvClassList.setLayoutManager(manager);
        adapter.setOnItemClickListener(new ShareClassListAdapter.onItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                SharePopWindow popWindow = new SharePopWindow(ShareActivity.this, String.valueOf(shareVo.getType()),
                        shareVo.getPaths(), dynamicVoList.get(position).getGroupId(), shareVo.getText());
                popWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
            }
        });
        rvClassList.setAdapter(adapter);
    }
}
