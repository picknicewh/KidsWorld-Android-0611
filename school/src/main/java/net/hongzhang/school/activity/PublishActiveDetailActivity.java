package net.hongzhang.school.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.school.R;
import net.hongzhang.status.util.PictrueUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class PublishActiveDetailActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvTitle;
    private TextView tvContent;
    private RelativeLayout rlPicture;
    private TextView tv_comment;
    private List<String> imageList;

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
        rlPicture = $(R.id.rl_picture);
        tv_comment = $(R.id.tv_comment);
        tv_comment.setOnClickListener(this);
        imageList = new ArrayList<>();
        initData();
    }

    private void initData() {
        imageList.add("http://hongzhang.net.cn/dynamic/1490323359509/s/14903233595622371635.png");
        imageList.add("http://hongzhang.net.cn/dynamic/1492415888321/s/14924158883211492415886089.png");
        imageList.add("http://hongzhang.net.cn/head/1492407777439/1492407777439526145.png");
        imageList.add("http://hongzhang.net.cn/myFlickr/1490947295042/s/14909472955764658452.png");
        imageList.add("http://hongzhang.net.cn/myFlickr/1490947295042/s/14909472950430131000.png");
        imageList.add("http://hongzhang.net.cn/myFlickr/1490947295042/s/14909472959959376185.png");
        new PictrueUtils().setPictrueLoad(this, imageList, rlPicture);
    }

    @Override
    protected void setToolBar() {
        setCententTitle("桦桦作业详情");
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.tv_comment) {
            Intent intent = new Intent(this, TaskCommentActivity.class);
            startActivity(intent);
        }
    }
}
