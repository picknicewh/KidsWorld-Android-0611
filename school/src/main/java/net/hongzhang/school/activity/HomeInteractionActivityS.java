package net.hongzhang.school.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.baselibrary.widget.LoadingDialog;
import net.hongzhang.school.R;
import net.hongzhang.school.adapter.MyHomeInteractionExpendAdapter;
import net.hongzhang.school.bean.ActivityInfoVo;
import net.hongzhang.school.bean.ActivityInfoVos;
import net.hongzhang.school.presenter.HomeInteractionSContract;
import net.hongzhang.school.presenter.HomeInteractionSPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class HomeInteractionActivityS extends Activity implements View.OnClickListener, HomeInteractionSContract.View {
    private HomeInteractionSPresenter presenter;
    private MyHomeInteractionExpendAdapter finishAdapter;
    private MyHomeInteractionExpendAdapter publishAdapter;
    private List<ActivityInfoVos> finishActivityVosList;
    private List<ActivityInfoVos> publishActivityVosList;
    private ExpandableListView expandableListView;
    private ImageView iv_left;
    private RadioButton rb_finish;
    private RadioButton rb_publish;
    private String tsId;
    public LoadingDialog dialog;
    private TextView tv_nodata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_interaction_s);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        expandableListView = (ExpandableListView) findViewById(R.id.expand_list_view);
        rb_finish = (RadioButton) findViewById(R.id.rb_finish);
        rb_publish = (RadioButton) findViewById(R.id.rb_publish);
        iv_left = (ImageView) findViewById(R.id.iv_left);
        tv_nodata = (TextView) findViewById(R.id.tv_nodata);
        finishActivityVosList = new ArrayList<>();
        publishActivityVosList = new ArrayList<>();
        rb_publish.setOnClickListener(this);
        rb_finish.setOnClickListener(this);
        iv_left.setOnClickListener(this);
        expandableListView.setGroupIndicator(null);
        ColorDrawable dw = new ColorDrawable(0000000000);
        expandableListView.setChildDivider(dw);
        G.setTranslucent(this);
        initData();
    }

    private void initData() {
        tsId = UserMessage.getInstance(this).getTsId();
        finishAdapter = new MyHomeInteractionExpendAdapter(this, finishActivityVosList);
        publishAdapter = new MyHomeInteractionExpendAdapter(this, publishActivityVosList);
        expandableListView.setAdapter(finishAdapter);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                Intent intent = null;
                ActivityInfoVo activityVo;
                if (finishActivityVosList != null && finishActivityVosList.size() > 0) {
                    activityVo = finishActivityVosList.get(groupPosition).getActivityJsonList().get(childPosition);
                    if (activityVo.getAppraiseId() != null) {
                        intent = new Intent(getApplicationContext(), CommentTaskActivityS.class);
                        intent.putExtra("appraiseId", activityVo.getAppraiseId());
                        intent.putExtra("activityId", activityVo.getActivityId());
                    } else {
                        if (activityVo.getActivityWorksId() == null) {
                            intent = new Intent(getApplicationContext(), TaskDetailActivity.class);
                            intent.putExtra("activityId", activityVo.getActivityId());
                            intent.putExtra("deadLine", activityVo.getDeadline());
                        } else {
                            intent = new Intent(getApplicationContext(), SubmitTaskDetailActivity.class);
                            intent.putExtra("activityWorksId", activityVo.getActivityWorksId());
                            intent.putExtra("activityId", activityVo.getActivityId());
                            intent.putExtra("name", UserMessage.getInstance(HomeInteractionActivityS.this).getUserName());
                        }
                    }
                } else if (publishActivityVosList != null && publishActivityVosList.size() > 0) {
                    activityVo = publishActivityVosList.get(groupPosition).getActivityJsonList().get(childPosition);
                    intent = new Intent(getApplicationContext(), TaskDetailActivity.class);
                    intent.putExtra("activityId", activityVo.getActivityId());
                    intent.putExtra("deadLine", activityVo.getDeadline());
                }

                startActivity(intent);
                return true;
            }
        });
        presenter = new HomeInteractionSPresenter(HomeInteractionActivityS.this, this);
        presenter.getEndActivityList(tsId);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (rb_finish.isChecked() && finishActivityVosList.size() ==finishSize) {
            presenter.getEndActivityList(tsId);
        } else if (rb_publish.isChecked() && publishActivityVosList.size()==publishSize) {
            presenter.getPublishActivityList(tsId);
        }
    }
    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.tv_subtitle) {
            startActivity(new Intent(this, PublishActiveActivityT.class));
        } else if (viewId == R.id.rb_publish) {
            if (publishActivityVosList.size()==0&& publishSize!=0){
                presenter.getPublishActivityList(tsId);
            }
            expandableListView.setAdapter(publishAdapter);
            setExpandableList(publishAdapter);
        } else if (viewId == R.id.rb_finish) {
            if (finishActivityVosList.size()==0 && finishSize!=0){
                presenter.getEndActivityList(tsId);
            }
            expandableListView.setAdapter(finishAdapter);
            setExpandableList(finishAdapter);
        } else if (viewId == R.id.iv_left) {
            finish();
        }
    }

    @Override
    public void showLoadingDialog() {
        if (dialog == null)
            dialog = new LoadingDialog(this, R.style.LoadingDialogTheme);
        try {
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        dialog.setCancelable(true);
        dialog.setLoadingText("数据加载中...");
    }

    public void stopLoadingDialog() {
        if (dialog != null) {
            try {
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private int finishSize = 0;
    private int publishSize = 0;

    @Override
    public void setPublishActivityList(List<ActivityInfoVos> activityVosList) {
        publishSize = activityVosList.size();
            publishActivityVosList.clear();
            publishActivityVosList.addAll(activityVosList);
            if (publishAdapter != null) {
                publishAdapter.notifyDataSetChanged();
            }
        setExpandableList(publishAdapter);

    }

    @Override
    public void setEndActivityList(List<ActivityInfoVos> activityVosList) {
            finishSize = activityVosList.size();
            finishActivityVosList.clear();
            finishActivityVosList.addAll(activityVosList);
            if (finishAdapter != null) {
                finishAdapter.notifyDataSetChanged();
            }
            setExpandableList(finishAdapter);

    }
    private void setExpandableList(MyHomeInteractionExpendAdapter adapter){
        if (adapter.getGroupCount()>0){
            for (int i = 0; i < adapter.getGroupCount(); i++)
                expandableListView.expandGroup(i);
            tv_nodata.setVisibility(View.GONE);
        }else {
            tv_nodata.setVisibility(View.VISIBLE);
        }
    }
}
