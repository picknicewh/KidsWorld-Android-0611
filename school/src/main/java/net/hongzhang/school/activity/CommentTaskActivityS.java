package net.hongzhang.school.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.school.R;
import net.hongzhang.school.adapter.ActionParentTagAdapter;
import net.hongzhang.school.adapter.ActionTagAdapter;
import net.hongzhang.school.bean.TaskCommentDetailVo;
import net.hongzhang.school.presenter.CommentTaskSContract;
import net.hongzhang.school.presenter.CommentTaskSPresenter;
import net.hongzhang.school.widget.StarProgressBar;

import java.util.List;

/**
 * 学生活动评分
 * 20170508
 */
public class CommentTaskActivityS extends BaseActivity implements View.OnClickListener, CommentTaskSContract.View {
    /**
     * 星星
     */
    private StarProgressBar ratingBar;
    /**
     * 子标签
     */
    private RecyclerView rv_action;
    /**
     * 父标签
     */
    private RecyclerView rv_action_parent;
    /**
     * 内容
     */
    private TextView tv_comment_content;
    private ActionTagAdapter adapter;
    private ActionParentTagAdapter adapterParent;
    private CommentTaskSPresenter presenter;
    private String appraiseId;
    private String activityId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_comment_s);
        initView();
        initList();
    }
    private void initView() {
        rv_action = $(R.id.rv_action);
        rv_action_parent = $(R.id.rv_action_parent);
        ratingBar = $(R.id.ratingBar);
        tv_comment_content = $(R.id.tv_comment_content);

    }
    private void initList() {
        presenter = new CommentTaskSPresenter(CommentTaskActivityS.this, this);
        Intent intent = getIntent();
        appraiseId = intent.getStringExtra("appraiseId");
        activityId = intent.getStringExtra("activityId");
        presenter.getAppraiseDetail(appraiseId);

    }
    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
        setCententTitle("活动");
        setSubTitle("排行榜");
        setSubTitleOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.tv_subtitle) {
            Intent intent = new Intent(this, RankingListActivity.class);
            intent.putExtra("activityId", activityId);
            startActivity(intent);
        }
    }

    @Override
    public void setDetailInfo(TaskCommentDetailVo taskCommentDetailVo) {
        ratingBar.setProgress(taskCommentDetailVo.getScore());
        tv_comment_content.setText(taskCommentDetailVo.getContent());
        List<TaskCommentDetailVo.ParentVo> tags = taskCommentDetailVo.getList();
        adapter = new ActionTagAdapter(presenter.getChildList(tags));
        adapterParent = new ActionParentTagAdapter(this, presenter.getParentList(tags));
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        rv_action.setLayoutManager(manager);
        rv_action.setAdapter(adapter);
        rv_action_parent.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv_action_parent.setAdapter(adapterParent);
    }
}
