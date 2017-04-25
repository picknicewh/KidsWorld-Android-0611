package net.hongzhang.school.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.util.DateUtil;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.school.R;
import net.hongzhang.school.adapter.AttacthmentAdapter;
import net.hongzhang.school.widget.CustomDateTimeDialog;
import net.hongzhang.status.util.PictrueUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;


/**
 * 作者： wh
 * 时间： 2017/4/19
 * 名称：活动详情页/作业详情页
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class TaskDetailActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvTitle;
    private TextView tvInvest;
    private LinearLayout llInvest;
    private TextView tvActiveContent;
    private TextView tvContent;
    private RelativeLayout rlPicture;
    private RecyclerView rvEnclosure;
    private TextView tvPublishTime;
    private TextView tvEndTime;
    private LinearLayout ll_conform_comment;
    /**
     * 是否评估
     */
    private TextView tvConformComment;
    /**
     * 发布时间选择控件
     */
    private CustomDateTimeDialog publishTimeDialog;
    /**
     * 截至时间选择控件
     */
    private CustomDateTimeDialog endTimeDialog;
    /**
     * 最后一次点击时间
     */
    private long lastClickTime;
    /**
     * 开始时间
     */
    private String publicDate;
    /**
     * 结束时间
     */
    private String endDate;
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
        if (UserMessage.getInstance(this).getType().equals("1")) {
            setCententTitle("作业详情");
            setSubTitle("作业上交");
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
        tvInvest = $(R.id.tv_invest);
        llInvest = $(R.id.ll_invest);
        tvContent = $(R.id.tv_content);
        tvActiveContent = $(R.id.tv_active_content);
        rlPicture = $(R.id.rl_picture);
        rvEnclosure = $(R.id.rv_enclosure);
        tvPublishTime = $(R.id.tv_publish_time);
        tvEndTime = $(R.id.tv_end_time);
        tvConformComment = $(R.id.tv_conform_comment);
        ll_conform_comment = $(R.id.ll_conform_comment);
        imageList = new ArrayList<>();
        tvPublishTime.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);
        initData();
    }

    private void initData() {
        imageList.add("http://hongzhang.net.cn/dynamic/1490323359509/s/14903233595622371635.png");
        imageList.add("http://hongzhang.net.cn/dynamic/1492415888321/s/14924158883211492415886089.png");
        imageList.add("http://hongzhang.net.cn/head/1492407777439/1492407777439526145.png");
        imageList.add("http://hongzhang.net.cn/myFlickr/1490947295042/s/14909472955764658452.png");
        imageList.add("http://hongzhang.net.cn/myFlickr/1490947295042/s/14909472950430131000.png");
        imageList.add("http://hongzhang.net.cn/myFlickr/1490947295042/s/14909472959959376185.png");
        publishTimeDialog = new CustomDateTimeDialog(this, R.style.MyDialog, 1);
        endTimeDialog = new CustomDateTimeDialog(this, R.style.MyDialog, 0);
        int type = Integer.parseInt(UserMessage.getInstance(this).getType());
       // int type = 1;
        llInvest.setVisibility(type == 1 ? View.VISIBLE : View.GONE);
        tvActiveContent.setText(type == 1 ? "活动内容:" : "作业要求:");
        ll_conform_comment.setVisibility(type == 1 ? View.VISIBLE : View.GONE);
        rvEnclosure.setAdapter(new AttacthmentAdapter(this));
        rvEnclosure.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        new PictrueUtils().setPictrueLoad(this, imageList, rlPicture);
    }
    /**
     * 是否为快速双击
     */
    public boolean isFastDoubleClick() {
        long now = System.currentTimeMillis();
        long offset = now - lastClickTime;
        if (offset <= 1000) {
            return true;
        }
        lastClickTime = now;
        return false;
    }
    /**
     * 设置选择时间
     */
    public void setDateTextView(long millis, int flag) {
        Date date = new Date(millis);
        String dateStr = DateUtil.format_chinese.format(date);
        if (flag == 1) {
            tvPublishTime.setText(dateStr);
            publicDate = DateUtil.DATE.format(date);
        } else {
            tvEndTime.setText(dateStr);
            endDate = DateUtil.DATE.format(date);
        }
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.tv_publish_time) {
            if (isFastDoubleClick()) {
                return;
            } else {
                lastClickTime = System.currentTimeMillis();
                publishTimeDialog.show();
            }
        } else if (viewId == R.id.tv_end_time) {
            if (isFastDoubleClick()) {
                return;
            } else {
                lastClickTime = System.currentTimeMillis();
                endTimeDialog.show();
            }
        }else if (viewId==R.id.tv_subtitle){
            Intent intent;
            if (UserMessage.getInstance(this).getType().equals("1")){
                intent = new Intent(this,PublishActiveSActivity.class);
                intent.putExtra("publicDate",tvPublishTime.getText().toString());
                intent.putExtra("endDate",tvEndTime.getText().toString());
                intent.putExtra("title",tvTitle.getText().toString());
            }else {
                intent = new Intent(this,PublishActiveTActivity.class);
            }

            startActivity(intent);
        }
    }
}
