package net.hongzhang.school.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.school.R;
import net.hongzhang.school.adapter.SubmitTaskListAdapter;
import net.hongzhang.school.bean.TaskInfoVo;
import net.hongzhang.school.presenter.SubmitTaskListTContract;
import net.hongzhang.school.presenter.SubmitTaskListTPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * 作者： wh
 * 时间： 2017/4/19
 * 名称： 教师端提交作业列表
 * 版本说明：
 * 附加注释：
 * 主要接口：获取作业列
 */
public class SubmitTaskListActivityT extends BaseActivity implements SubmitTaskListTContract.View {
    private TextView tvPublishCount;
    private ListView lvPublishList;
    private String activityId;
    private SubmitTaskListTPresenter presenter;
    private SubmitTaskListAdapter adapter;
    private List<TaskInfoVo> taskListInfoVoList;
    private int status;
    private TextView tv_nodata;
    private boolean isAppraise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_task_list_t);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        taskListInfoVoList = new ArrayList<>();
        Intent intent = getIntent();
        activityId = intent.getStringExtra("activityId");
        status = intent.getIntExtra("status", 0);
        tvPublishCount = (TextView) findViewById(R.id.tv_publish_count);
        lvPublishList = (ListView) findViewById(R.id.lv_publish_list);
        tv_nodata = (TextView) findViewById(R.id.tv_nodata);
        adapter = new SubmitTaskListAdapter(this, taskListInfoVoList);
        lvPublishList.setAdapter(adapter);
        lvPublishList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent;
                if (status == HomeInteractionActivityT.DOING || !isAppraise) {
                    intent = new Intent(getApplicationContext(), SubmitTaskDetailActivity.class);
                    intent.putExtra("activityWorksId", taskListInfoVoList.get(i).getActivityWorksId());
                    G.log("--------------x-x-x-x-"+ taskListInfoVoList.get(i).getActivityWorksId());
                    intent.putExtra("name", taskListInfoVoList.get(i).getTsName());
                    intent.putExtra("status", status);
                    intent.putExtra("isAppraise", taskListInfoVoList.get(i).getAppraiseId()==null?true:false);
                } else {
                    intent = new Intent(getApplicationContext(), CommentTaskActivityS.class);
                    intent.putExtra("appraiseId", taskListInfoVoList.get(i).getAppraiseId());
                }
                intent.putExtra("activityId", activityId);
                startActivity(intent);
            }
        });
        presenter = new SubmitTaskListTPresenter(SubmitTaskListActivityT.this, this);
        presenter.getTaskList(activityId, UserMessage.getInstance(this).getTsId());
    }

    @Override
    protected void setToolBar() {
        setCententTitle("活动上传");
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
    }

    @Override
    public void setTaskList(List<TaskInfoVo> taskListInfoVos) {
        for (TaskInfoVo taskInfoVo : taskListInfoVos) {
            if (taskInfoVo != null) {
                taskListInfoVoList.add(taskInfoVo);
            }
        }
        if (taskListInfoVoList.size() == 0) {
            tvPublishCount.setVisibility(View.GONE);
            tv_nodata.setVisibility(View.VISIBLE);
        } else {
            tvPublishCount.setText("已有" + taskListInfoVoList.size() + "位家长上传活动");
        }
        isAppraise = presenter.isAllAppraise(taskListInfoVoList);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (taskListInfoVoList.size()>0){
            taskListInfoVoList.clear();
        }
        presenter.getTaskList(activityId, UserMessage.getInstance(this).getTsId());
    }
}
