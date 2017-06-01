package net.hongzhang.school.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.school.R;
import net.hongzhang.school.adapter.MyHomeInteractionExpendAdapter;
import net.hongzhang.school.bean.ActivityInfoVo;
import net.hongzhang.school.bean.ActivityInfoVos;
import net.hongzhang.school.presenter.HomeInteractionContract;
import net.hongzhang.school.presenter.HomeInteractionPresenter;

import java.util.List;

import butterknife.ButterKnife;

public class HomeInteractionActivityT extends BaseActivity implements View.OnClickListener, HomeInteractionContract.View {
    public static final int NOBEGIN=0;
    public static final int DOING=1;
    public static final int DONE=2;
    private HomeInteractionPresenter presenter;
    private MyHomeInteractionExpendAdapter adapter;
    private ExpandableListView expandableListView;
    /**
     * 作品的状态
     */
    private int status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_interaction_t);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void setToolBar() {
        setLiftOnClickClose();
        setCententTitle("家园互动");
        setSubTitle("发布活动");
        setSubTitleOnClickListener(this);
    }

    private void initView() {
        expandableListView = $(R.id.expand_list_view);
       // Drawable drawable = ContextCompat.getDrawable(this,R.mipmap.ic_group_indicator);
        expandableListView.setGroupIndicator(null);
        ColorDrawable dw = new ColorDrawable(0000000000);
        expandableListView.setChildDivider(dw);
        initData();
    }

    private void initData() {
        presenter = new HomeInteractionPresenter(HomeInteractionActivityT.this, this);
        presenter.getActivityList(UserMessage.getInstance(this).getTsId());
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.tv_subtitle) {
            startActivity(new Intent(this, PublishActiveActivityT.class));
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (activityVoses.size() != listSize) {
            presenter.getActivityList(UserMessage.getInstance(this).getTsId());
        }
    }

    private int listSize = 0;
    private List<ActivityInfoVos> activityVoses;

    @Override
    public void setActivityList(final List<ActivityInfoVos> activityVosList) {
        this.activityVoses = activityVosList;
        listSize = activityVosList.size();
        adapter = new MyHomeInteractionExpendAdapter(this, activityVosList);
        expandableListView.setAdapter(adapter);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                Intent intent ;
                ActivityInfoVo activityInfoVo = activityVosList.get(groupPosition).getActivityJsonList().get(childPosition);
                status = presenter.getStatus(activityInfoVo);
               if (status==NOBEGIN){
                   intent = new Intent(HomeInteractionActivityT.this, TaskDetailActivity.class);
                   intent.putExtra("deadLine", activityInfoVo.getDeadline());
               }else {
                    intent = new Intent(HomeInteractionActivityT.this, SubmitTaskListActivityT.class);
                    intent.putExtra("status",status);
                }
                intent.putExtra("activityId", activityInfoVo.getActivityId());
                startActivity(intent);
                return true;
            }
        });
        for (int i = 0; i < adapter.getGroupCount(); i++) {
            expandableListView.expandGroup(i);
        }
    }

}
