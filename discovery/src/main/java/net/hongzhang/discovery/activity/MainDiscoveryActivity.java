package net.hongzhang.discovery.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.hongzhang.baselibrary.image.ImageCache;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.baselibrary.widget.LoadingDialog;
import net.hongzhang.baselibrary.widget.NoScrollGirdView;
import net.hongzhang.baselibrary.widget.NoScrollListView;
import net.hongzhang.discovery.R;
import net.hongzhang.discovery.adapter.MainConsultAdapter;
import net.hongzhang.discovery.adapter.MainResourceAdapter;
import net.hongzhang.discovery.modle.CompilationVo;
import net.hongzhang.discovery.util.MainRecommendContract;
import net.hongzhang.discovery.util.MainRecommendPresenter;
import net.hongzhang.discovery.widget.BannerView;
import net.hongzhang.user.mode.BannerVo;

import java.util.ArrayList;
import java.util.List;

public class MainDiscoveryActivity extends Activity implements View.OnClickListener, MainRecommendContract.View {
    /**
     * 左边图片
     */
    private ImageView iv_left;
    /**
     * 中间文字
     */
    private TextView tv_title;
    /**
     * 右边图片
     */
    private ImageView iv_right;
    /**
     * 首页搜索
     */
    private Button bt_search;
    /**
     * 广告栏
     */
    private BannerView bannerView;

    /**
     * 幼儿听听专辑页
     */
    private LinearLayout ll_music;
    /**
     * 幼儿课堂专辑页
     */
    private LinearLayout ll_class;
    /**
     * 幼儿资讯专辑页
     */
    private LinearLayout ll_consult;
    /**
     * 推荐听听列表
     */
    private NoScrollGirdView gv_music;
    /**
     * 推荐课堂列表
     */
    private NoScrollGirdView gv_class;
    /**
     * 推荐资讯列表
     */
    private NoScrollListView lv_consult;

    /**
     * 推荐听听更多
     */
    private TextView tv_more_music;
    /**
     * 推荐课堂更多
     */
    private TextView tv_more_clsss;
    /**
     * 推荐资讯更多
     */
    private TextView tv_more_consult;
    /**
     * 数据处理类
     */
    private MainRecommendPresenter presenter;
    /**
     * 推荐类型   type 1=音频，0=视频，2=资讯
     */
    private int type;
    public LoadingDialog dialog;
    /**
     * 用户id
     */
    private String tsid;
    /**
     * 音乐推荐专辑id
     */
    private String music_recommend_id;
    /**
     * 视频推荐专辑id
     */
    private String video_recommend_id;
    /**
     * 资讯推荐专辑id
     */
    private String consult_recommend_id;
    /**
     *
     * 用户信息
     */
    private UserMessage userMessage;
    /**
     * 装载需要展示导航图
     */
    private List<ImageView> guideList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_discovery);
        initView();
    }

    private void initView() {
        iv_left = (ImageView) findViewById(R.id.iv_dleft);
        tv_title = (TextView) findViewById(R.id.tv_dtitle);
        iv_right = (ImageView) findViewById(R.id.iv_dright);
        bannerView = (BannerView)findViewById(R.id.bv_home);
        bt_search = (Button) findViewById(R.id.et_search);
        ll_music = (LinearLayout) findViewById(R.id.ll_music);
        ll_class = (LinearLayout) findViewById(R.id.ll_class);
        ll_consult = (LinearLayout) findViewById(R.id.ll_consult);
        gv_music = (NoScrollGirdView) findViewById(R.id.gv_music);
        gv_class = (NoScrollGirdView) findViewById(R.id.gv_class);
        lv_consult = (NoScrollListView) findViewById(R.id.lv_consult);
        tv_more_music = (TextView) findViewById(R.id.tv_more_music);
        tv_more_clsss = (TextView) findViewById(R.id.tv_more_class);
        tv_more_consult = (TextView) findViewById(R.id.tv_more_consult);
        iv_left.setOnClickListener(this);
        bt_search.setOnClickListener(this);
        iv_right.setOnClickListener(this);
        ll_music.setOnClickListener(this);
        ll_class.setOnClickListener(this);
        ll_consult.setOnClickListener(this);
        tv_more_music.setOnClickListener(this);
        tv_more_clsss.setOnClickListener(this);
        tv_more_consult.setOnClickListener(this);
        initData();
    }
    private void initData() {
        G.setTranslucent(this);
        guideList = new ArrayList<>();
        presenter = new MainRecommendPresenter(MainDiscoveryActivity.this, this);
        userMessage =  UserMessage.getInstance(this);
        tsid = userMessage.getTsId();
        ImageCache.imageLoader(userMessage.getHoldImgUrl(),iv_left);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.iv_dleft) {
          presenter.startUserActivity();
        } else if (viewId == R.id.iv_dright) {
          presenter.startHistoryActivity();
        } else if (viewId == R.id.et_search) {
          presenter.startSearchActivity();
        }  else if (viewId == R.id.ll_music || viewId == R.id.tv_more_music) {
            presenter.startMusicListActivity(music_recommend_id);
        } else if (viewId == R.id.ll_class || viewId == R.id.tv_more_class) {
            presenter.startVideoListActivity(video_recommend_id);
        } else if (viewId == R.id.ll_consult || viewId == R.id.tv_more_consult) {
            presenter.startConsultListActivity(consult_recommend_id);
        }
    }

    @Override
    public void setRecommendVoMusicList(final List<CompilationVo> compilationVos) {
        if (compilationVos!=null &&compilationVos.size()>0){
            music_recommend_id = String.valueOf(compilationVos.get(0).getAlbumId());
            MainResourceAdapter adapter = new MainResourceAdapter(this, compilationVos);
            gv_music.setAdapter(adapter);
            gv_music.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    presenter.startMusicActivity(String.valueOf(compilationVos.get(i).getAlbumId()), null);
                }
            });
        }
    }

    @Override
    public void setRecommendVoClassList(final List<CompilationVo> compilationVos) {
        if (compilationVos!=null &&compilationVos.size()>0){
            video_recommend_id = String.valueOf(compilationVos.get(0).getAlbumId());
            MainResourceAdapter adapter = new MainResourceAdapter(this, compilationVos);
            gv_class.setAdapter(adapter);
            gv_class.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    presenter.startVideoActivity(String.valueOf(compilationVos.get(i).getAlbumId()));
                }
            });
        }

    }

    @Override
    public void setRecommendVoConsultList(final List<CompilationVo> compilationVos) {
        if (compilationVos!=null &&compilationVos.size()>0){
            consult_recommend_id = String.valueOf(compilationVos.get(0).getAlbumId());
            MainConsultAdapter adapter = new MainConsultAdapter(this, compilationVos);
            lv_consult.setAdapter(adapter);
            lv_consult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    presenter.startConsultActivity(Integer.parseInt(compilationVos.get(i).getAlbumId()));
                }
            });
        }

    }
    @Override
    public void setBannerList(List<BannerVo> bannerList) {
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        for (int i = 0; i < bannerList.size(); i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);
            ImageCache.imageLoader(bannerList.get(i).getBanner_url(),iv);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            guideList.add(iv);
        }
        bannerView.setViewList(guideList);
        bannerView.addViewPager(this);
        bannerView.requestFocus();
    }
    @Override
    public void rushData() {
        type = MainRecommendPresenter.TYPE_MUISC;
        if (type == MainRecommendPresenter.TYPE_MUISC) {
            type = MainRecommendPresenter.TYPE_VIDEO;
            presenter.getRecommendResource(tsid, 4, type);
        } else if (type == MainRecommendPresenter.TYPE_VIDEO) {
            type = MainRecommendPresenter.TYPE_CONSULT;
            presenter.getRecommendResource(tsid, 4, type);
        } else if (type == MainRecommendPresenter.TYPE_CONSULT) {
            presenter.getRecommendResource(tsid, 6, type);
        }
    }

    @Override
    public void showLoadingDialog() {
        if (dialog == null)
            dialog = new LoadingDialog(this, net.hongzhang.baselibrary.R.style.LoadingDialogTheme);
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
