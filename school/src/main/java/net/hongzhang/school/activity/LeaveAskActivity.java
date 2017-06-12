package net.hongzhang.school.activity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;

import net.hongzhang.baselibrary.util.DateUtil;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.school.R;
import net.hongzhang.school.presenter.LeaveAskContract;
import net.hongzhang.school.presenter.LeaveAskPresenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 作者： wh
 * 时间： 2016/7/18
 * 名称：请假申请
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class LeaveAskActivity extends BaseDateSelectActivity implements View.OnClickListener, LeaveAskContract.View {
    /**
     * 开始时间
     */
    private LinearLayout ll_start;
    private TextView tv_start;
    /**
     * 结束时间
     */
    private LinearLayout ll_end;
    private TextView tv_end;
    /**
     * 姓名
     */
    private TextView tv_name;
    /**
     * 事由
     */
    private EditText et_cause;
    /**
     * 用户信息
     */
    private UserMessage message;
    private LeaveAskPresenter presenter;
    private TextView tv_course;
    private LinearLayout ll_course;
    private String courseCode;
    private List<Map<String, String>> mapList;
    private List<String> courseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_informask);
        init();
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
        setCententTitle("我要请假");
        setSubTitle("完成");
    }

    private void initdata() {
        presenter = new LeaveAskPresenter(LeaveAskActivity.this, this);
        presenter.getVacationContent();
        tv_name.setText(message.getUserName());
        courseList = new ArrayList<>();
        initDate();
        setTv_end(tv_end);
        setTv_start(tv_start);
        /* Date date = new Date(System.currentTimeMillis());
        String currentTime = DateUtil.format_chinese.format(date);
        tv_start.setText(currentTime);
        startDate = DateUtil.DATE_TIME.format(date);*/
        // tv_end.setText("请选择时间");

    }

    private void init() {
        ll_start = $(R.id.ll_istarttime);
        ll_end = $(R.id.ll_iendtime);
        tv_end = $(R.id.tv_iendtime);
        tv_start = $(R.id.tv_istarttime);
        tv_name = $(R.id.tv_name);
        et_cause = $(R.id.et_cause);
        tv_course = $(R.id.tv_course);
        ll_course = $(R.id.ll_course);
        setTv_end(tv_end);
        setTv_start(tv_start);
        ll_course.setOnClickListener(this);
        ll_end.setOnClickListener(this);
        ll_start.setOnClickListener(this);
        setSubTitleOnClickListener(this);
        message = UserMessage.getInstance(this);
        // DateUtil.setEditContent(et_cause,null,200);
        //   et_cause.setFilters(new InputFilter[]{new InputFilter.LengthFilter(200)});
        initdata();
    }


    @Override
    public void onClick(View view) {
        int viewID = view.getId();
        if (viewID == R.id.ll_istarttime) {
            presenter.getTimePickerView(this, new TimePickerView.OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
                    startTime = date.getTime();
                    startDate = DateUtil.DATE_TIME.format(date.getTime());
                    tv_start.setText(DateUtil.format_chinese.format(date));
                }
            }).show();

        } else if (viewID == R.id.ll_iendtime) {
            presenter.getTimePickerView(this, new TimePickerView.OnTimeSelectListener() {

                @Override
                public void onTimeSelect(Date date, View v) {
                    endTime = date.getTime();
                    endDate = DateUtil.DATE_TIME.format(date.getTime());
                    tv_end.setText(DateUtil.format_chinese.format(date));
                }
            }).show();
        } else if (viewID == R.id.tv_subtitle) {
            presenter.publishLeave(message.getTsId(), courseCode, startDate, endDate);
        } else if (viewID == R.id.ll_course) {
            presenter.getOptionsPickerView(this, new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    Map<String, String> params = mapList.get(options1);
                    courseCode = params.get("code");
                    String course = params.get("course");
                    tv_course.setText(course);
                }
            }, courseList).show();
        }
    }

    @Override
    public void setCourseList(List<Map<String, String>> mapList) {
        this.mapList = mapList;
        for (int i = 0; i < mapList.size(); i++) {
            Map<String, String> params = mapList.get(i);
            String course = params.get("course");
            courseList.add(course);
        }
    }

 /*   *//**
     * 提交请假申请
     *//*
    private void subLeaveAsk() {
        Map<String, Object> params = new HashMap<>();
        if (G.isEmteny(et_cause.getText().toString())) {
            Toast.makeText(this, "请假事由不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (G.isEmteny(endDate)) {
            Toast.makeText(this, "结束时间不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (endDate.compareTo(startDate) <= 0) {
            Toast.makeText(this, "请假开始时间不能比结束时间晚哦！", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean isallsapce = DateUtil.isallsapce(et_cause);
        if (isallsapce) {
            Toast.makeText(this, "请假事由不能为全部空格哦！", Toast.LENGTH_SHORT).show();
            return;
        }
        //提交角色ID
        UserMessage userMessage = UserMessage.getInstance(this);
        params.put("tsId", userMessage.getTsId());
        //需要请假人员角色ID
        params.put("leaveTsId", userMessage.getTsId());
        params.put("endDate", endDate);
        params.put("startDate", startDate);
        //1=早餐，2=中餐，3=晚餐 多选时，用英文逗号分隔
        params.put("diningStatus", "1");
        params.put("cause", et_cause.getText().toString());
        Type type = new TypeToken<Result<String>>() {
        }.getType();
        OkHttps.sendPost(type, Apiurl.SCHOOL_SUBLEAVE, params, this);
        showLoadingDialog();
    }*/

 /*   @Override
    public void onSuccess(String uri, Object date) {
        showLoadingDialog();
        Result<String> data = (Result<String>) date;
        if (data.isSuccess()) {
            String result = data.getData();
            Toast.makeText(LeaveAskActivity.this, result, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onError(String uri, Result error) {
        stopLoadingDialog();
        DetaiCodeUtil.errorDetail(error, this);
    }*/
}
