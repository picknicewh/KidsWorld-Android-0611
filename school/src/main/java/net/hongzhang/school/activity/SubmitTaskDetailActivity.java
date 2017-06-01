package net.hongzhang.school.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.school.R;
import net.hongzhang.school.adapter.TaskPhotoListAdapter;
import net.hongzhang.school.bean.TaskDetailInfoVo;
import net.hongzhang.school.presenter.SubmitActiveDetailContract;
import net.hongzhang.school.presenter.SubmitActiveDetailPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * 作者： wanghua
 * 时间： 2017/5/3
 * 名称：家长上传作业详情
 * 版本说明：接口返回有问题
 * 附加注释：
 * 主要接口：获取作品详情
 */
public class SubmitTaskDetailActivity extends BaseActivity implements View.OnClickListener, SubmitActiveDetailContract.View {
    private TextView tvTitle;
    private TextView tvContent;
    private RecyclerView rv_picture;
    private TextView tv_comment;
    private List<String> imageList;
    private SubmitActiveDetailPresenter presenter;
    private String activityWorksId;
    private TaskPhotoListAdapter taskPhotoListAdapter;
    private int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_active_detail);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitle = $(R.id.tv_title);
        tvContent = $(R.id.tv_content);
        rv_picture = $(R.id.rv_picture);
        tv_comment = $(R.id.tv_comment);
        tv_comment.setOnClickListener(this);
        imageList = new ArrayList<>();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        activityWorksId = intent.getStringExtra("activityWorksId");
     //   G.log("--------------xxxxxxxxxxxxxx"+activityWorksId);
        if (UserMessage.getInstance(this).getType().equals("1")) {
            //家长版
            tv_comment.setVisibility(View.VISIBLE);
            tv_comment.setText("等待老师评价中.....");
        } else {
            //老师版
            boolean isAppraise = intent.getBooleanExtra("isAppraise", false);
            status = getIntent().getIntExtra("status", -1);
            tv_comment.setVisibility(status == HomeInteractionActivityT.DONE && isAppraise ? View.VISIBLE : View.GONE);
        }
        presenter = new SubmitActiveDetailPresenter(SubmitTaskDetailActivity.this, this);
        presenter.getActiveDetail(activityWorksId);
        taskPhotoListAdapter = new TaskPhotoListAdapter(this, imageList);
    }

    private String name;

    @Override
    protected void setToolBar() {
        name = getIntent().getStringExtra("name");
        setCententTitle(name + "作业详情");
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.tv_comment) {
            if (!UserMessage.getInstance(this).getType().equals("1")) {
                Intent intent = new Intent(this, CommentTaskActivityT.class);
                intent.putExtra("activityWorksId", taskDetailInfoVo.getActivityWorksId());
                intent.putExtra("activityId", getIntent().getStringExtra("activityId"));
                intent.putExtra("name", name);
                startActivity(intent);
            }
        } else if (viewId == R.id.tv_subtitle) {
            Intent intent = new Intent(this, RankingListActivity.class);
            intent.putExtra("activityId", getIntent().getStringExtra("activityId"));
            startActivity(intent);
        }
    }

    private TaskDetailInfoVo taskDetailInfoVo;

    @Override
    public void setActiveDetailInfo(TaskDetailInfoVo taskDetailInfoVo) {
        this.taskDetailInfoVo = taskDetailInfoVo;
        imageList.addAll(taskDetailInfoVo.getImgUrls());
        //把视频放在最后
        if (!G.isEmteny(taskDetailInfoVo.getVideo())){
            imageList.add(taskDetailInfoVo.getVideo());
        }
        if (imageList.size() == 1) {
            rv_picture.setLayoutManager(new LinearLayoutManager(this));
        } else if (imageList.size() == 2) {
            rv_picture.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            rv_picture.setLayoutManager(new GridLayoutManager(this, 3));
        }

        rv_picture.setAdapter(taskPhotoListAdapter);
        tvTitle.setText(taskDetailInfoVo.getTitle());
        tvContent.setText(taskDetailInfoVo.getContent());
    }
}
