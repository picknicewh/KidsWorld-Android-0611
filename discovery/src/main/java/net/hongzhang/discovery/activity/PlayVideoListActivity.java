package net.hongzhang.discovery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import net.hongzhang.baselibrary.image.ImageCache;
import net.hongzhang.baselibrary.mode.ResourceVo;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.baselibrary.widget.CircleImageView;
import net.hongzhang.baselibrary.widget.LoadingDialog;
import net.hongzhang.baselibrary.widget.PromptPopWindow;
import net.hongzhang.discovery.R;
import net.hongzhang.discovery.adapter.CompilationPlayCountAdapter;
import net.hongzhang.discovery.adapter.VideoAlbumDetailAdapter;
import net.hongzhang.discovery.adapter.VideoCommentListAdapter;
import net.hongzhang.discovery.modle.CommentInfoVo;
import net.hongzhang.discovery.modle.CompilationVo;
import net.hongzhang.discovery.presenter.PlayVideoDetailContract;
import net.hongzhang.discovery.presenter.PlayVideoDetailPresenter;

import java.util.ArrayList;
import java.util.List;

import static net.hongzhang.discovery.R.id.iv_album_more;

/**
 * 作者： wh
 * 时间： 2017/3/5
 * 名称： 播放视频详情页
 * 版本说明：
 * 附加注释：
 * 主要接口：获取专辑列表，获取视频详情，获取推荐视频列表，提交评论，获取评论列表，收藏视频
 */
public class PlayVideoListActivity extends AppCompatActivity implements View.OnClickListener,
        PlayVideoDetailContract.View {
    private ImageView iv_left;
    /**
     * 专辑封面
     */
    private ImageView iv_album;
    private ImageView iv_play_full;
    /**
     * 收藏
     */
    private ImageView iv_alubm_collect;
    /**
     * 点赞
     */
    private ImageView iv_album_head;
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
     * 评论列表
     */
    private RecyclerView rv_comment_list;
    /**
     * 数据处理类
     */
    private PlayVideoDetailPresenter presenter;
    /**
     * 加载中对话框
     */
    public LoadingDialog dialog;
    /**
     * 当前播放的位置
     */
    private int position = 0;
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
    private CompilationPlayCountAdapter adapter;
    /**
     * 用户角色id
     */
    private String tsId;
    /**
     * 是否收藏
     */
    private int cancel;
    /**
     * 主题id
     */
    private String alubmId;
    /**
     * 当前资源id
     */
    private String resourceId;
    /**
     * 版权
     */
    private TextView tv_copy_right;
    private int isResourceFree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video_list);
        G.initDisplaySize(this);
        initSurfView();
    }

    private void initSurfView() {
        iv_left = (ImageView) findViewById(R.id.iv_left);
        iv_album = (ImageView) findViewById(R.id.iv_album);
        iv_alubm_collect = (ImageView) findViewById(R.id.iv_album_collect);
        iv_alubm_more = (ImageView) findViewById(iv_album_more);
        tv_play_count = (TextView) findViewById(R.id.tv_play_count);
        tv_brief = (TextView) findViewById(R.id.tv_brief);
        rv_play_list = (RecyclerView) findViewById(R.id.rv_play_list);
        ry_recommend = (RecyclerView) findViewById(R.id.ry_recommend);
        rv_comment_list = (RecyclerView) findViewById(R.id.rv_comment_list);
        iv_user_image = (CircleImageView) findViewById(R.id.iv_user_image);
        ll_detail = (LinearLayout) findViewById(R.id.ll_detail);
        et_comment = (EditText) findViewById(R.id.et_comment);
        tv_comment = (TextView) findViewById(R.id.tv_comment);
        tv_album_num = (TextView) findViewById(R.id.tv_album_num);
        tv_copy_right = (TextView) findViewById(R.id.tv_copy_right);
        iv_play_full = (ImageView) findViewById(R.id.iv_play_full);
        iv_album_head = (ImageView) findViewById(R.id.iv_album_head);
        iv_alubm_collect.setOnClickListener(this);
        tv_comment.setOnClickListener(this);
        iv_play_full.setOnClickListener(this);
        iv_alubm_more.setOnClickListener(this);
        iv_left.setOnClickListener(this);
        iv_album_head.setOnClickListener(this);
        resourceVos = new ArrayList<>();
        tsId = UserMessage.getInstance(this).getTsId();
        alubmId = getIntent().getStringExtra("themeId");
        resourceId = getIntent().getStringExtra("resourceId");
        presenter = new PlayVideoDetailPresenter(this, this, alubmId, resourceId);
        presenter.getVideoList(tsId, alubmId);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.iv_album_collect) {
            if (cancel == 1) {
                cancel = 2;
                iv_alubm_collect.setImageResource(R.mipmap.star_dark);
            } else {
                cancel = 1;
                iv_alubm_collect.setImageResource(R.mipmap.star_dark_full);
            }
            presenter.subFavorate(resourceVos.get(position).getAlbumId(), cancel);
        } else if (viewId == R.id.tv_comment) {
            String content = et_comment.getText().toString().trim();
            if (!G.isEmteny(content) && !G.isAllSpace(content)) {
                presenter.subComment(tsId, resourceVos.get(position).getResourceId(), content);
                et_comment.setText("");
            } else {
                G.showToast(this, "评论内容不能为空！");
            }
        } else if (viewId == R.id.iv_play_full) {
            if (resourceVos != null && resourceVos.size() > 0) {
                if (isResourceFree == 1) {
                    PromptPopWindow promptPopWindow = new PromptPopWindow(this, "小主人，您的套餐已到期暂不能收看了。别难受，尽快续订，和贝贝虎一起愉快的玩耍吧!");
                    promptPopWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                } else {
                    Intent intent = new Intent(this, FullPlayVideoActivity.class);
                    intent.putExtra("albumId", alubmId);
                    intent.putExtra("resourceId", resourceVos.get(position).getResourceId());
                    startActivity(intent);
                }

            }
        } else if (viewId == iv_album_more) {
            if (visible == 0) {
                tv_copy_right.setVisibility(View.VISIBLE);
                visible = 1;
            } else {
                tv_copy_right.setVisibility(View.GONE);
                visible = 0;
            }
        } else if (viewId == R.id.iv_left) {
            finish();
        } else if (viewId == R.id.iv_album_head) {
            praiseType = praiseType == 1 ? 2 : 1;
            presenter.subPraise(resourceVos.get(position).getResourceId(), String.valueOf(praiseType));
            iv_album_head.setImageResource(praiseType == 1 ? R.mipmap.ic_heat_on : R.mipmap.ic_heat_off);
        }
    }

    private int visible = 0;
    private int praiseType = 1;

    /**
     * 设置列表
     *
     * @param resourceVos 资源列表
     * @param position    默认为0，如果搜索进来的话根据搜索出专辑的第几首歌来定位当前的位置
     */
    @Override
    public void setVideoList(final List<ResourceVo> resourceVos, int position) {
        this.position = position;
        this.resourceVos = resourceVos;
        alubmId = resourceVos.get(0).getAlbumId();
        albumAdapter = new VideoAlbumDetailAdapter(this, resourceVos);
        rv_play_list.setAdapter(albumAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_play_list.setLayoutManager(manager);
        albumAdapter.setCurrentPosition(position);//定位当前选中框的位置
        rv_play_list.scrollToPosition(position);//把recycleView移动到当前播放视频的的位置
        albumAdapter.setOnItemClickListener(new VideoAlbumDetailAdapter.onItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                if (albumAdapter != null) {
                    albumAdapter.setCurrentPosition(position);
                    setVideoInfo(resourceVos.get(position));
                }
            }
        });
    }
    /**
     * 设置当前播放视频的的显示的信息
     *
     * @param resourceVo 资源信息
     */
    @Override
    public void setVideoInfo(ResourceVo resourceVo) {
        isResourceFree = resourceVo.getPay();
        cancel = resourceVo.getIsFavorites();
        praiseType = resourceVo.getIsPraise();
        presenter.setResourceId(resourceVo.getResourceId(),alubmId);
        tv_album_num.setText(resourceVo.getResourceName());
        ImageCache.imageLoader(resourceVo.getImageUrl(), iv_album);
        iv_alubm_collect.setImageResource(resourceVo.getIsFavorites() == 1 ? R.mipmap.star_dark_full : R.mipmap.star_dark);
        iv_album_head.setImageResource(resourceVo.getIsPraise() == 1 ? R.mipmap.ic_heat_on : R.mipmap.ic_heat_off);
        tv_brief.setVisibility(G.isEmteny(resourceVo.getDescription())  ? View.GONE : View.VISIBLE);
        tv_brief.setText(resourceVo.getDescription());
        tv_play_count.setText(String.valueOf(resourceVo.getPvcount()));
        ImageCache.imageLoader(UserMessage.getInstance(this).getHoldImgUrl(), iv_user_image);
    }

    @Override
    public void setRecommendList(final List<CompilationVo> compilationVos) {
        adapter = new CompilationPlayCountAdapter(this, compilationVos);
        ry_recommend.setAdapter(adapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        ry_recommend.setNestedScrollingEnabled(false);
        ry_recommend.setLayoutManager(gridLayoutManager);
        adapter.setOnItemClickListener(new CompilationPlayCountAdapter.onItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                alubmId = String.valueOf(compilationVos.get(position).getAlbumId());
                presenter.getVideoList(tsId, alubmId);
            }
        });
    }

    @Override
    public void setCommentList(List<CommentInfoVo> commentInfoVos) {
        rv_comment_list.setNestedScrollingEnabled(false);
        rv_comment_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        VideoCommentListAdapter adapter = new VideoCommentListAdapter(this, commentInfoVos);
        rv_comment_list.setAdapter(adapter);
    }
    public void showLoadingDialog() {
        if (dialog == null)
            dialog = new LoadingDialog(this, R.style.LoadingDialogTheme);
        dialog.show();
        dialog.setCancelable(true);
        dialog.setLoadingText("数据加载中...");
    }
    public void stopLoadingDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        //向友盟提供统计参数
        MobclickAgent.onPageStart(G.getClassName(this));
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(G.getClassName(this));
        MobclickAgent.onPause(this);
    }

}
