package net.hongzhang.discovery.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.DetaiCodeUtil;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.network.ServerConfigManager;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.discovery.R;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ConsultActivity extends BaseActivity implements OkHttpListener {
    private WebView wb_consult;
    /**
     * 没网络时显示
     */
    private  String baseurl = ServerConfigManager.WEB_IP + "/consult_detail/index.html";
    private String resourceId;
    private UserMessage userMessage;
    private ProgressBar progress_bar;
    private String  url;
    private RelativeLayout rl_nonetwork;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_detail);
        loadView();
    }

    private void loadView() {
        wb_consult = $(R.id.wb_consult);
        progress_bar = $(R.id.progress_bar);
        rl_nonetwork = $(R.id.rl_nonetwork);
        userMessage = UserMessage.getInstance(this);
        resourceId = getIntent().getStringExtra("resourceId");
        if (G.isNetworkConnected(this)){
            savePlayTheRecord(userMessage.getTsId(),resourceId,0,1);
            savePlayTheRecord(userMessage.getTsId(),resourceId,0,2);
            url =baseurl+ "?tsId=" + userMessage.getTsId() + "&resourceid="
                    + resourceId+ "&accountId=" + userMessage.getAccount_id() ;
            wb_consult.loadUrl(url);
            G.log("------------------------" + url);
            setWebView();
            rl_nonetwork.setVisibility(View.GONE);
        }else {
            rl_nonetwork.setVisibility(View.VISIBLE);
        }

    }
    /**
     * 保存播放记录列表
     *
     * @param tsId          角色id
     * @param resourceId    资源id
     * @param broadcastPace 当前播放音乐的秒数
     * @param type          播放音乐开始/结束 type = 1 播放开始 type=2播放结束
     */
    public void savePlayTheRecord(String tsId, String resourceId, int broadcastPace, int type) {
        Map<String, Object> map = new HashMap<>();
        map.put("tsId", tsId);
        map.put("resourceid", resourceId);
        if (type == 2) {
            map.put("broadcastPace", broadcastPace);
        }
        map.put("type", type);
        Type mType = new TypeToken<Result<String>>() {
        }.getType();
        OkHttps.sendPost(mType, Apiurl.SAVEPLAYRECORDING, map, this);
    }
    private void setWebView() {
        wb_consult.getSettings().setJavaScriptEnabled(true);
        wb_consult.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        wb_consult.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progress_bar.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
    }
    @Override
    protected void setToolBar() {
        setLiftOnClickClose();
        setCententTitle("资讯详情");
    }
    @Override
    public void onSuccess(String uri, Object date) {
        if (uri.equals(Apiurl.SAVEPLAYRECORDING)) {
            Result<String> data = (Result<String>) date;
            String result = data.getData();
            Log.i("SSS", result);
        }
    }
    @Override
    public void onError(String uri, Result error) {
        DetaiCodeUtil.errorDetail(error,this);
    }
}
