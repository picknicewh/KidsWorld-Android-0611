package net.hongzhang.school.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.school.R;
import net.hongzhang.school.adapter.TaskCommentAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class TaskCommentActivity extends BaseActivity implements View.OnClickListener {
    private RatingBar ratingBar;

    private RecyclerView rvFinish;

    private RecyclerView rvAttitude;

    private RecyclerView rvAbility;

    private EditText etTag;

    private TextView tvConfrom;
    private List<String> tagFinishList;
    private List<String> tagAttitudeList;
    private List<String> tagAbilyList;
    private TaskCommentAdapter finishAdapter;
    private TaskCommentAdapter attitudeAdapter;
    private TaskCommentAdapter ablityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_comment);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        ratingBar = $(R.id.ratingBar);
        rvFinish = $(R.id.rv_finish);
        rvAttitude = $(R.id.rv_attitude);
        rvAbility = $(R.id.rv_ability);
        etTag = $(R.id.et_tag);
        tvConfrom = $(R.id.tv_confrom);
        tvConfrom.setOnClickListener(this);
        tagAbilyList = new ArrayList<>();
        tagAttitudeList = new ArrayList<>();
        tagFinishList = new ArrayList<>();
        initData();
    }

    private void initData() {
        tagFinishList.add("成果");
        tagFinishList.add("用时");
        tagAttitudeList.add("效率");
        tagAttitudeList.add("执行力");
        tagAttitudeList.add("主动性");
        tagAbilyList.add("理解性");
        tagAbilyList.add("创造性");
        tagAbilyList.add("动手性");
        tagAbilyList.add("记忆性");
        finishAdapter = new TaskCommentAdapter(this, tagFinishList);
        attitudeAdapter = new TaskCommentAdapter(this, tagAttitudeList);
        ablityAdapter = new TaskCommentAdapter(this, tagAbilyList);
        rvAbility.setAdapter(ablityAdapter);
        rvAbility.setLayoutManager(new GridLayoutManager(this,5));
        rvFinish.setAdapter(finishAdapter);
        rvFinish.setLayoutManager(new GridLayoutManager(this,5));
        rvAttitude.setAdapter(attitudeAdapter);
        rvAttitude.setLayoutManager(new GridLayoutManager(this,5));
    }

    @Override
    protected void setToolBar() {
        setCententTitle("桦桦作业详情");
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
    }

    @Override
    public void onClick(View view) {

    }
}
