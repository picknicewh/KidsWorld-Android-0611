package net.hongzhang.discovery.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.hongzhang.baselibrary.image.ImageCache;
import net.hongzhang.baselibrary.mode.ResourceVo;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.baselibrary.widget.CircleImageView;
import net.hongzhang.baselibrary.widget.LoadingDialog;
import net.hongzhang.discovery.R;
import net.hongzhang.discovery.adapter.PlayDetailRecommedAdapter;
import net.hongzhang.discovery.adapter.VideoAlbumDetailAdapter;
import net.hongzhang.discovery.modle.CompilationVo;
import net.hongzhang.discovery.util.PlayVideoDetailContract;
import net.hongzhang.discovery.util.PlayVideoDetailPresenter;

import java.util.ArrayList;
import java.util.List;

public class PlayVideoActivity extends Activity implements View.OnClickListener,
     PlayVideoDetailContract.View {

    /**
     * 专辑封面
     */
    private ImageView iv_album;
    private ImageView iv_play_full;
    /**
     * 导航栏左边放回按钮
     */
    private ImageView iv_left;
    /**
     * 收藏
     */
    private ImageView iv_alubm_collect;
    /**
     * 播放集数
     */
    private TextView tv_album_num;
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
     * 数据处理类
     */
    private PlayVideoDetailPresenter presenter;
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
        iv_album = (ImageView) findViewById(R.id.iv_album);
        iv_left = (ImageView) findViewById(R.id.iv_left);
        iv_alubm_collect = (ImageView)findViewById(R.id.iv_album_collect);
        iv_alubm_more= (ImageView)findViewById(R.id.iv_album_more);
        tv_play_count = (TextView)findViewById(R.id.tv_play_count);
        tv_brief = (TextView)findViewById(R.id.tv_brief);
        rv_play_list = (RecyclerView)findViewById(R.id.rv_play_list);
        ry_recommend = (RecyclerView)findViewById(R.id.ry_recommend);
        iv_user_image = (CircleImageView)findViewById(R.id.iv_user_image);
        ll_detail  = (LinearLayout)findViewById(R.id.ll_detail);
        et_comment = (EditText)findViewById(R.id.et_comment);
        tv_comment = (TextView)findViewById(R.id.tv_comment);
        tv_album_num = (TextView)findViewById(R.id.tv_album_num);
        iv_play_full = (ImageView) findViewById(R.id.iv_play_full);
        iv_left.setOnClickListener(this);
        iv_alubm_collect.setOnClickListener(this);
        tv_comment.setOnClickListener(this);
        iv_play_full.setOnClickListener(this);
        resourceVos = new ArrayList<>();
        tsId = UserMessage.getInstance(this).getTsId();
        presenter = new PlayVideoDetailPresenter(this,this,null);
    }
    @Override
    public void onClick(View view) {
        int viewId = view.getId();
     /*   if (viewId == R.id.iv_screen) {
         *//*   //设置横屏模式
            Intent intent = new Intent(this,FullPlayVideoActivity.class);
            startActivity(intent);*//*
            presenter.setScreenDirection(this.getResources().getConfiguration().orientation,rl_control, rl_control_full,ll_detail,surfaceView);
        } else if (viewId == R.id.iv_left) {
            finish();
        } else if (viewId == R.id.iv_play) {
           if (isPlaying){
               presenter.pause();
           }else {
              presenter.play();
           }
        }else*/ if (viewId==R.id.iv_album_collect){
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
        }else if (viewId==R.id.iv_play_full){
            Intent intent = new Intent(this,FullPlayVideoActivity.class);
            intent.putExtra("albumId",resourceVos.get(position).getAlbumId());
            intent.putExtra("resourceId",resourceVos.get(position).getResourceId());
            startActivity(intent);
        }
    }

    @Override
    public void setVideoList(final List<ResourceVo> resourceVos) {
        cancel = resourceVos.get(position).getIsFavorites() ;
        this.resourceVos = resourceVos;
        albumAdapter = new VideoAlbumDetailAdapter(this, resourceVos);
        rv_play_list.setAdapter(albumAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_play_list.setLayoutManager(manager);
        albumAdapter.setOnItemClickListener(new VideoAlbumDetailAdapter.onItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                if (albumAdapter!=null){
                    albumAdapter.setCurrentPosition(position);
                    setVideoInfo(resourceVos.get(position),position);
                }
            }
        });
    }

    @Override
    public void setVideoInfo(ResourceVo resourceVo,int position) {
        this.position = position;
        tv_album_num.setText("第"+(position+1)+"集");
        ImageCache.imageLoader(resourceVo.getImageUrl(),iv_album);
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
            dialog=new LoadingDialog(this, net.hongzhang.baselibrary.R.style.LoadingDialogTheme);
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
