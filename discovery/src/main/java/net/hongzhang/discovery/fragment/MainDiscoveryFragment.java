package net.hongzhang.discovery.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youth.banner.Banner;

import net.hongzhang.baselibrary.base.BaseFragement;
import net.hongzhang.baselibrary.image.ImageCache;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.baselibrary.widget.LoadingDialog;
import net.hongzhang.baselibrary.widget.NoScrollListView;
import net.hongzhang.discovery.R;
import net.hongzhang.discovery.adapter.CompilationAdapter;
import net.hongzhang.discovery.adapter.ConsultAdapter;
import net.hongzhang.discovery.modle.CompilationVo;
import net.hongzhang.discovery.modle.ResourceVo;
import net.hongzhang.discovery.presenter.MainRecommendContract;
import net.hongzhang.discovery.presenter.MainRecommendPresenter;
import net.hongzhang.discovery.util.BannerImageLoaderUtil;
import net.hongzhang.user.mode.BannerVo;

import java.util.ArrayList;
import java.util.List;

/**
 * 修改时间：2017/3/15
 * 修改:首页banner滑动bug 将首页banner修改其它第三方库 github地址：https://github.com/youth5201314/banner
 * 新增音乐播放Trans过度动画 5.0系统才支持
 */
public class MainDiscoveryFragment extends BaseFragement implements View.OnClickListener, MainRecommendContract.View {
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
    private Banner bannerView;

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
    private RecyclerView gv_music;
    /**
     * 推荐课堂列表
     */
    private RecyclerView gv_class;
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
     * 用户信息
     */
    private UserMessage userMessage;
    /**
     * 装载需要展示导航图
     */
    private List<ImageView> guideList;
   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_discovery);
        initView();
    }*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main_discovery, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        iv_left = (ImageView) view.findViewById(R.id.iv_dleft);
        tv_title = (TextView) view.findViewById(R.id.tv_dtitle);
        iv_right = (ImageView) view.findViewById(R.id.iv_dright);
        bannerView = (Banner) view.findViewById(R.id.bv_home);
        bt_search = (Button) view.findViewById(R.id.et_search);
        ll_music = (LinearLayout) view.findViewById(R.id.ll_music);
        ll_class = (LinearLayout) view.findViewById(R.id.ll_class);
        ll_consult = (LinearLayout) view.findViewById(R.id.ll_consult);
        gv_music = (RecyclerView) view.findViewById(R.id.gv_music);
        gv_class = (RecyclerView) view.findViewById(R.id.gv_class);
        lv_consult = (NoScrollListView) view.findViewById(R.id.lv_consult);
        tv_more_music = (TextView) view.findViewById(R.id.tv_more_music);
        tv_more_clsss = (TextView) view.findViewById(R.id.tv_more_class);
        tv_more_consult = (TextView) view.findViewById(R.id.tv_more_consult);
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
        G.setTranslucent(getActivity());
        guideList = new ArrayList<>();
        presenter = new MainRecommendPresenter(getActivity(), this);
        userMessage = UserMessage.getInstance(getActivity());
        tsid = userMessage.getTsId();
        ImageCache.imageLoader(userMessage.getHoldImgUrl(), iv_left);
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
        } else if (viewId == R.id.ll_music || viewId == R.id.tv_more_music) {
            presenter.startMusicListActivity();
        } else if (viewId == R.id.ll_class || viewId == R.id.tv_more_class) {
            presenter.startVideoListActivity();
        } else if (viewId == R.id.ll_consult || viewId == R.id.tv_more_consult) {
            presenter.startConsultListActivity();
        }
    }

    @Override
    public void setRecommendVoMusicList(final List<CompilationVo> compilationVos) {
        if (compilationVos != null && compilationVos.size() > 0) {
            music_recommend_id = String.valueOf(compilationVos.get(0).getAlbumId());
            final CompilationAdapter adapter = new CompilationAdapter(getActivity(), compilationVos);
            gv_music.setAdapter(adapter);
            gv_music.setNestedScrollingEnabled(false);
            gv_music.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            adapter.setOnItemClickListener(new CompilationAdapter.onItemClickListener() {
                @Override
                public void OnItemClick(View view, int position) {
                    presenter.startMusicActivity(String.valueOf(compilationVos.get(position).getAlbumId()), null,adapter.getAlbumImageView());
                }
            });
        }
    }

    @Override
    public void setRecommendVoClassList(final List<CompilationVo> compilationVos) {
        if (compilationVos != null && compilationVos.size() > 0) {
            video_recommend_id = String.valueOf(compilationVos.get(0).getAlbumId());
            CompilationAdapter adapter = new CompilationAdapter(getActivity(), compilationVos);
            gv_class.setAdapter(adapter);
            gv_class.setNestedScrollingEnabled(false);
            gv_class.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            adapter.setOnItemClickListener(new CompilationAdapter.onItemClickListener() {
                @Override
                public void OnItemClick(View view, int position) {
                    presenter.startVideoActivity(String.valueOf(compilationVos.get(position).getAlbumId()));
                }
            });
        }

    }

    @Override
    public void setRecommendVoConsultList(final List<ResourceVo> consultInfovos) {
        if (consultInfovos != null && consultInfovos.size() > 0) {
            consult_recommend_id = String.valueOf(consultInfovos.get(0).getAlbumId());
            ConsultAdapter adapter = new ConsultAdapter(getActivity(), consultInfovos);
            lv_consult.setAdapter(adapter);
            lv_consult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    presenter.startConsultActivity(consultInfovos.get(i).getResourceId());
                }
            });
        }

    }

    @Override
    public void setBannerList(List<BannerVo> bannerList) {
//        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        List<String> bannerImgUrlList = new ArrayList<>();
        for (int i = 0; i < bannerList.size(); i++) {
//            ImageView iv = new ImageView(getActivity());
//            iv.setLayoutParams(mParams);
//            ImageCache.imageLoader(bannerList.get(i).getBanner_url(),iv);
//            iv.setScaleType(ImageView.ScaleType.FIT_XY);
//            guideList.add(iv);
            bannerImgUrlList.add(bannerList.get(i).getBanner_url());
        }
//        bannerView.setViewList(guideList);
//        bannerView.addViewPager(getActivity());
//        bannerView.requestFocus();
        bannerView.setImageLoader(new BannerImageLoaderUtil())//设置图片加载方式
                .setImages(bannerImgUrlList)//设置轮播图片
                .setDelayTime(3000)//设置轮播时间
                .start();
    }

    @Override
    public void onStart() {
        super.onStart();
        //开始轮播
        bannerView.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        //停止轮播
        bannerView.stopAutoPlay();
    }

    @Override
    public void rushData() {
        type = MainRecommendPresenter.TYPE_MUISC;
        if (type == MainRecommendPresenter.TYPE_MUISC) {
            presenter.getRecommendResource(tsid, 4, type);
            type = MainRecommendPresenter.TYPE_VIDEO;
        } else if (type == MainRecommendPresenter.TYPE_VIDEO) {
            presenter.getRecommendResource(tsid, 4, type);
            type = MainRecommendPresenter.TYPE_CONSULT;
        } else if (type == MainRecommendPresenter.TYPE_CONSULT) {
            presenter.getRecommendConsult(tsid, 6, userMessage.getAccount_id());
        }
        // 1视频 2音频 3资讯 4视频和音频
    }

    @Override
    public void showLoadingDialog() {
        if (dialog == null)
            dialog = new LoadingDialog(getActivity(), R.style.LoadingDialogTheme);
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
