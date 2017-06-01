package net.hongzhang.school.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.util.DateUtil;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.school.R;
import net.hongzhang.school.adapter.AttacthmentAdapter;
import net.hongzhang.school.adapter.TaskPhotoListAdapter;
import net.hongzhang.school.bean.ActivityDetailVo;
import net.hongzhang.school.presenter.TaskDetailContract;
import net.hongzhang.school.presenter.TaskDetailPresenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;

import static net.hongzhang.school.R.id.tv_invest;


/**
 * 作者： wh
 * 时间： 2017/4/19
 * 名称：活动详情页/作业详情页
 * 版本说明：未完成。。。。。。。。。。
 * 附加注释：
 * 主要接口：获取活动详情
 */
public class TaskDetailActivity extends BaseActivity implements View.OnClickListener, TaskDetailContract.View {
    /**
     * 标题
     */
    private TextView tvTitle;
    private TextView tvInvest;
    private LinearLayout llInvest;
    /**
     * 内容
     */
    private TextView tvContent;
    /**
     * 图片列表
     */
    private RecyclerView rvPicture;
    /**
     * 附件
     */
    private RecyclerView rvEnclosure;
    /**
     * 发布时间
     */
    private TextView tvPublishTime;
    /**
     * 截止时间
     */
    private TextView tvEndTime;
    /**
     * 提交评价
     */
    private LinearLayout ll_conform_comment;
    /**
     * 是否评估
     */
    private TextView tvConformComment;
    private boolean isFinish;
    private TaskDetailPresenter presenter;
    /**
     * 文件列表
     */
    private List<String> fileList;
    /**
     * 附件列表
     */
    private AttacthmentAdapter adapter;
    /**
     * 活动详情
     */
    private ActivityDetailVo activityDetailVo;
    /**
     * 截止日期
     */
    private long deadLine;
    private TaskPhotoListAdapter taskPhotoListAdapter;
    private List<String> imageList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void setToolBar() {
        deadLine = getIntent().getLongExtra("deadLine",0);
        if (UserMessage.getInstance(this).getType().equals("1")) {
            setCententTitle("作业详情");
            if (deadLine > System.currentTimeMillis()) {
                setSubTitle("作业上交");
                isFinish = true;
            }
        } else {
            setCententTitle("活动详情");
            setSubTitle("活动上传情况");
        }
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
        setSubTitleOnClickListener(this);
    }

    private void initView() {
        tvTitle = $(R.id.tv_title);
        tvInvest = $(tv_invest);
        llInvest = $(R.id.ll_invest);
        tvContent = $(R.id.tv_content);
        rvPicture = $(R.id.rv_picture);
        rvEnclosure = $(R.id.rv_enclosure);
        tvPublishTime = $(R.id.tv_publish_time);
        tvEndTime = $(R.id.tv_end_time);
        tvConformComment = $(R.id.tv_conform_comment);
        ll_conform_comment = $(R.id.ll_conform_comment);
        fileList = new ArrayList<>();
        initData();
    }

    private void initData() {
        imageList = new ArrayList<>();
        int type = Integer.parseInt(UserMessage.getInstance(this).getType());
        llInvest.setVisibility(type == 1 ? View.VISIBLE : View.GONE);
        ll_conform_comment.setVisibility(type == 1 ? View.GONE : View.VISIBLE);
        adapter = new AttacthmentAdapter(this, fileList);
        rvEnclosure.setAdapter(adapter);
        rvEnclosure.setLayoutManager(new GridLayoutManager(this, 2));
        taskPhotoListAdapter = new TaskPhotoListAdapter(this, imageList);
        presenter = new TaskDetailPresenter(TaskDetailActivity.this, this);
        presenter.getTaskDetail(getIntent().getStringExtra("activityId"));

    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.tv_subtitle) {
            Intent intent;
            if (UserMessage.getInstance(this).getType().equals("1")) {
                if (isFinish) {
                    intent = new Intent(this, SubmitTaskActivityS.class);
                    intent.putExtra("publicDate", tvPublishTime.getText().toString());
                    intent.putExtra("endDate", tvEndTime.getText().toString());
                    intent.putExtra("title", tvTitle.getText().toString());
                } else {
                    intent = new Intent(this, CommentTaskActivityS.class);
                }
            } else {
                intent = new Intent(this, SubmitTaskListActivityT.class);
            }
            intent.putExtra("activityId", activityDetailVo.getActivityId());
            startActivity(intent);
        }
    }
    @Override
    public void setDetailInfo(ActivityDetailVo activityDetailVo) {
        this.activityDetailVo = activityDetailVo;
        tvTitle.setText(activityDetailVo.getTitle());
        tvInvest.setText(presenter.getChecks(activityDetailVo.getDimensionalityName()));
        tvContent.setText(activityDetailVo.getContent());
        tvEndTime.setText(DateUtil.format_chinese.format(new Date(activityDetailVo.getDeadline())));
        tvPublishTime.setText(DateUtil.format_chinese.format(new Date(activityDetailVo.getPostTime())));
        tvConformComment.setText(activityDetailVo.getAppraise() == 1 ? "是" : "否");
        fileList.addAll(activityDetailVo.getFiles());
        imageList.addAll(activityDetailVo.getImgUrls());
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        if (imageList.size()==1){
            rvPicture.setLayoutManager(new LinearLayoutManager(this));
        }else if (imageList.size()==2){
            rvPicture.setLayoutManager(new GridLayoutManager(this, 2));
        }else {
            rvPicture.setLayoutManager(new GridLayoutManager(this, 3));
        }
        rvPicture.setAdapter(taskPhotoListAdapter);
        if (taskPhotoListAdapter != null) {
            taskPhotoListAdapter.notifyDataSetChanged();
        }
    }
}
