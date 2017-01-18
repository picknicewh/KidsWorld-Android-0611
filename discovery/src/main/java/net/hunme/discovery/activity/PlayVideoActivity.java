package net.hunme.discovery.activity;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import net.hunme.baselibrary.image.ImageCache;
import net.hunme.baselibrary.mode.ResourceVo;
import net.hunme.baselibrary.util.G;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.baselibrary.widget.CircleImageView;
import net.hunme.baselibrary.widget.LoadingDialog;
import net.hunme.discovery.R;
import net.hunme.discovery.adapter.PlayDetailRecommedAdapter;
import net.hunme.discovery.adapter.VideoAlbumAdapter;
import net.hunme.discovery.adapter.VideoAlbumDetailAdapter;
import net.hunme.discovery.modle.CompilationVo;
import net.hunme.discovery.util.PlayVideoDetailContract;
import net.hunme.discovery.util.PlayVideoDetailPresenter;

import java.util.ArrayList;
import java.util.List;

public class PlayVideoActivity extends Activity implements View.OnClickListener,
        SeekBar.OnSeekBarChangeListener, PlayVideoDetailContract.View {
    /**
     * 承载视频画面
     */
    private SurfaceView surfaceView;
    /**
     * 视频播放控制台
     */
    private RelativeLayout rl_media;
    /**
     * 全屏按钮
     */
    private ImageView iv_srceen;
    /**
     * 上一曲
     */
    private ImageView iv_pre;
    /**
     * 播放
     */
    private ImageView iv_play;
    /**
     * 下一曲
     */
    private ImageView iv_next;
    /**
     * 导航栏左边放回按钮
     */
    private ImageView iv_left;
    /**
     * 进度条
     */
    private SeekBar seekBar;
    /**
     * 当前播放时间
     */
    private TextView tv_current;
    /**
     *视频总时长
     */
    private TextView tv_duration;
    /**
     * 播放次数
     */
    private TextView tv_alubm_num;
    /**
     * 播放的名字
     */
    private TextView tv_video_name;
    /**
     * 收藏
     */
    private ImageView iv_alubm_collect;
    /**
     * 加载更多
     */
    private ImageView iv_alubm_more;
    /**
     * 播放次数
     */
    private TextView tv_play_count;
    /**
     * 简介
     */
    private TextView tv_brief;
    /**
     * 专辑列表
     */
    private RecyclerView rv_play_list;
    /**
     * 推荐列表
     */
    private RecyclerView ry_recommend;
    /**
     * 用户头像
     */
    private CircleImageView iv_user_image;
    /**
     * 评论编辑框
     */
    private EditText et_comment;
    /**
     * 评论按钮
     */
    private TextView tv_comment;
    /**
     * 播放下面整个布局
     */
    private LinearLayout ll_detail;
    /**
     * 竖屏下的下面控制台
     */
    private RelativeLayout rl_control;
    /**
     * 横屏下的下面控制台
     */
    private RelativeLayout rl_control_full;
    /**
     * 横屏下的控件初始化
     */
    private RecyclerView rv_play_list_full;
    /**
     * 数据处理类
     */
    private PlayVideoDetailPresenter presenter;
    /**
     * 是否播放
     */
    private boolean isPlaying;
    /**
     * 加载中对话框
     */
    public     LoadingDialog dialog;
    /**
     * 当前播放的位置
     */
    private int position;
    /**
     * 专辑列表适配器
     */
    private VideoAlbumDetailAdapter albumAdapter;
    /**
     * 全屏专辑列表适配器
     */
    private VideoAlbumAdapter fullalbumAdapter;
    /**
     * 资源列表
     */
    private List<ResourceVo> resourceVos;
    /**
     * 推荐列表适配器
     */
    private PlayDetailRecommedAdapter adapter;
    /**
     * 用户角色id
     */
    private String tsId;
    /**
     * 是否收藏
     */
    private int cancel;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        G.initDisplaySize(this);
        G.setTranslucent(this);
        initSurfView();
    }

    private void initSurfView() {
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        rl_media = (RelativeLayout) findViewById(R.id.rl_media);
        iv_srceen = (ImageView) findViewById(R.id.iv_screen);
        iv_pre = (ImageView) findViewById(R.id.iv_pre);
        iv_next = (ImageView) findViewById(R.id.iv_next);
        iv_play = (ImageView) findViewById(R.id.iv_play);
        iv_left = (ImageView) findViewById(R.id.iv_left);
        seekBar = (SeekBar) findViewById(R.id.sb_video);
        tv_current = (TextView) findViewById(R.id.tv_current);
        tv_duration = (TextView) findViewById(R.id.tv_duration);
        tv_alubm_num  =(TextView)findViewById(R.id.tv_album_num);
        iv_alubm_collect = (ImageView)findViewById(R.id.iv_album_collect);
        iv_alubm_more= (ImageView)findViewById(R.id.iv_album_more);
        tv_play_count = (TextView)findViewById(R.id.tv_play_count);
        tv_brief = (TextView)findViewById(R.id.tv_brief);
        rv_play_list = (RecyclerView)findViewById(R.id.rv_play_list);
        ry_recommend = (RecyclerView)findViewById(R.id.ry_recommend);
        iv_user_image = (CircleImageView)findViewById(R.id.iv_user_image);
        tv_video_name= (TextView)findViewById(R.id.tv_video_name);
        ll_detail  = (LinearLayout)findViewById(R.id.ll_detail);
        et_comment = (EditText)findViewById(R.id.et_comment);
        tv_comment = (TextView)findViewById(R.id.tv_comment);
        rl_control = (RelativeLayout)findViewById(R.id.rl_control) ;
        rl_control_full = (RelativeLayout)findViewById(R.id.rl_control_full);
        iv_srceen.setOnClickListener(this);
        iv_pre.setOnClickListener(this);
        iv_next.setOnClickListener(this);
        iv_play.setOnClickListener(this);
        iv_left.setOnClickListener(this);
        iv_alubm_collect.setOnClickListener(this);
        tv_comment.setOnClickListener(this);
        resourceVos = new ArrayList<>();
        tsId = UserMessage.getInstance(this).getTsId();
        presenter = new PlayVideoDetailPresenter(this,surfaceView,this,null);
    }
    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.iv_screen) {
         /*   //设置横屏模式
            Intent intent = new Intent(this,FullPlayVideoActivity.class);
            startActivity(intent);*/
            presenter.setScreenDirection(this.getResources().getConfiguration().orientation,rl_control, rl_control_full,ll_detail,surfaceView);
        } else if (viewId == R.id.iv_left) {
            finish();
        } else if (viewId == R.id.iv_play) {
           if (isPlaying){
               presenter.pause();
           }else {
              presenter.play();
           }
        }else if (viewId==R.id.iv_album_collect){
            if (cancel==1) {
                cancel = 2;
                iv_alubm_collect.setImageResource(R.mipmap.star_dark);
            } else {
                cancel = 1;
                iv_alubm_collect.setImageResource(R.mipmap.star_dark_full);
            }
            presenter.subFavorate(resourceVos.get(position).getAlbumId(), cancel);
        }else if (viewId==R.id.tv_comment){
            presenter.subComment(tsId,resourceVos.get(position).getResourceId(),et_comment.getText().toString());
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                presenter.setScreenDirection(this.getResources().getConfiguration().
                        orientation,rl_control,rl_control_full, ll_detail,surfaceView);
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (b){
            presenter.changeSeeBar(i);
        }
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        presenter.pause();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        presenter.play();
    }

    @Override
    public void setVideoList(List<ResourceVo> resourceVos) {
        cancel = resourceVos.get(position).getIsFavorites() ;
        this.resourceVos = resourceVos;
        albumAdapter = new VideoAlbumDetailAdapter(this, resourceVos);
        rv_play_list.setAdapter(albumAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_play_list.setLayoutManager(manager);
        albumAdapter.setOnItemClickListener(new VideoAlbumDetailAdapter.onItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                if (presenter.getPrepared()){
                    presenter.setPosition(position);
                }
                if (albumAdapter!=null){
                    albumAdapter.setCurrentPosition(position);
                }
            }
        });

        fullalbumAdapter = new VideoAlbumAdapter(this, resourceVos);
        rv_play_list_full.setAdapter(albumAdapter);
        LinearLayoutManager manager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_play_list_full.setLayoutManager(manager2);
        fullalbumAdapter.setOnItemClickListener(new VideoAlbumAdapter.onItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                if (presenter.getPrepared()){
                    presenter.setPosition(position);
                }
                if (albumAdapter!=null){
                    albumAdapter.setCurrentPosition(position);
                }
            }
        });
    }

    @Override
    public void setVideoInfo(ResourceVo resourceVo, int position) {
        this.position  = position;
        tv_alubm_num.setText(resourceVo.getResourceName());
        tv_video_name.setText(resourceVo.getResourceName());
        if (resourceVo.getIsFavorites()==1){
            iv_alubm_collect.setImageResource(R.mipmap.star_dark_full);
        }else {
            iv_alubm_collect.setImageResource(R.mipmap.star_dark);
        }
        tv_brief.setText(resourceVo.getDescription());
        tv_play_count.setText(String.valueOf(resourceVo.getPvcount()));
        ImageCache.imageLoader(UserMessage.getInstance(this).getHoldImgUrl(),iv_user_image);
    }

    @Override
    public void setIsPlay(boolean isPlay) {
        isPlaying = isPlay;
        if (isPlay) {
            iv_play.setImageResource(R.mipmap.ic_pause);
        } else {
            iv_play.setImageResource(R.mipmap.ic_play);
        }
    }
    @Override
    public void setDuration(int duration) {
        seekBar.setMax(duration);
        seekBar.setProgress(0);
        tv_duration.setText(G.toTime(duration));
    }
    @Override
    public void setCurrent(int progress) {
        seekBar.setProgress(progress);
        tv_current.setText(G.toTime(progress));
    }
    @Override
    public void setRecommendList(final List<CompilationVo> compilationVos) {
        adapter = new PlayDetailRecommedAdapter(this,compilationVos);
        ry_recommend.setAdapter(adapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        ry_recommend.setLayoutManager(gridLayoutManager);
        adapter.setOnItemClickListener(new PlayDetailRecommedAdapter.onItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                 presenter.getVideoList(tsId, String.valueOf(compilationVos.get(position).getAlbumId()));
            }
        });
    }
    @Override
    public void showLoadingDialog() {
        if(dialog==null)
            dialog=new LoadingDialog(this, net.hunme.baselibrary.R.style.LoadingDialogTheme);
        dialog.show();
        dialog.setCancelable(true);
        dialog.setLoadingText("数据加载中...");
    }
    @Override
    public void stopLoadingDialog() {
        if (dialog!=null) {
            dialog.dismiss();
        }
    }
}
