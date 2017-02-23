package net.hongzhang.school.activity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.DateUtil;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.baselibrary.widget.LoadingDialog;
import net.hongzhang.school.R;
import net.hongzhang.school.widget.CustomDateTimeDialog;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者： wh
 * 时间： 2016/7/18
 * 名称：请假申请
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class LeaveAskActivity extends BaseActivity implements View.OnClickListener, OkHttpListener {
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
     *开始时间选择控件
     */
    private CustomDateTimeDialog startDateTimeDialog;
    /**
     *结束时间选择控件
     */
    private CustomDateTimeDialog endDateTimeDialog;
    /**
     * 最后一次点击时间
     */
    private long lastClickTime;
    /**
     * 开始时间
     */
    private String starDate;
    /**
     * 结束时间
     */
    private String endDate;
    /**
     * 日期格式
     */
    private  SimpleDateFormat format;
    /**
     * 日期格式
     */
    private SimpleDateFormat format2;
    /**
     * 用户信息
     */
    private UserMessage message;
    private LoadingDialog dialog;

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
    private void initdata(){

        tv_name.setText(message.getUserName());
        format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
        format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String currentTime = format.format(new Date(System.currentTimeMillis()));
        tv_start.setText(currentTime);
        starDate = format2.format(new Date(System.currentTimeMillis()));
        tv_end.setText("请选择时间");
        startDateTimeDialog = new CustomDateTimeDialog(LeaveAskActivity.this,R.style.MyDialog,1);
        endDateTimeDialog = new CustomDateTimeDialog(LeaveAskActivity.this,R.style.MyDialog,0);
        dialog = new LoadingDialog(this,R.style.LoadingDialogTheme);
    }
    /**
     * 设置选择时间
     */
    public void setDateTextView(long millis,int flag) {
        Date date = new Date(millis);
        String datestr = format.format(date);
        if (flag==1){
            tv_start.setText(datestr);
            starDate = format2.format(date);
        }else {
            tv_end.setText(datestr);
            endDate = format2.format(date);
        }
    }
    private void init(){
        ll_start = $(R.id.ll_istarttime);
        ll_end = $(R.id.ll_iendtime);
        tv_end = $(R.id.tv_iendtime);
        tv_start = $(R.id.tv_istarttime);
        tv_name = $(R.id.tv_name);
        et_cause = $(R.id.et_cause);
        ll_end.setOnClickListener(this);
        ll_start.setOnClickListener(this);
        setSubTitleOnClickListener(this);
        message = UserMessage.getInstance(this);
       // DateUtil.setEditContent(et_cause,null,200);
     //   et_cause.setFilters(new InputFilter[]{new InputFilter.LengthFilter(200)});
        initdata();
    }
    /**
     *  是否为快速双击
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
     * 记录最后点击时间
     */
    public void markLastClickTime() {
        lastClickTime = System.currentTimeMillis();
    }
    @Override
    public void onClick(View view) {
        int viewID = view.getId();
        if (viewID==R.id.ll_istarttime){
            if (isFastDoubleClick()) {
                return;
            } else {
                markLastClickTime();
                startDateTimeDialog.show();

            }
        }else if (viewID==R.id.ll_iendtime){
            if (isFastDoubleClick()) {
                return;
            } else {
                markLastClickTime();
                endDateTimeDialog.show();
            }
        }else if (viewID==R.id.tv_subtitle){
            subLeaveAsk();
        }
    }

    /**
     *提交请假申请
     */
    private void subLeaveAsk(){
        Map<String,Object> params = new HashMap<>();
        if (G.isEmteny(et_cause.getText().toString())){
            Toast.makeText(this,"请假事由不能为空！",Toast.LENGTH_SHORT).show();
            return;
        }
        if (G.isEmteny(endDate)){
            Toast.makeText(this,"结束时间不能为空！",Toast.LENGTH_SHORT).show();
            return;
        }
        if (endDate.compareTo(starDate)<=0){
            Toast.makeText(this,"请假开始时间不能比结束时间晚哦！",Toast.LENGTH_SHORT).show();
            return;
        }
        boolean isallsapce =DateUtil.isallsapce(et_cause);
        if (isallsapce){
            Toast.makeText(this,"请假事由不能为全部空格哦！",Toast.LENGTH_SHORT).show();
            return;
        }
        //提交角色ID
        UserMessage userMessage = UserMessage.getInstance(this);
        params.put("tsId", userMessage.getTsId());
        //需要请假人员角色ID
        params.put("leaveTsId",userMessage.getTsId());
        params.put("endDate",endDate);
        params.put("startDate",starDate);
        //1=早餐，2=中餐，3=晚餐 多选时，用英文逗号分隔
        params.put("diningStatus","1");
        params.put("cause",et_cause.getText().toString());
        Type type = new TypeToken<Result<String>>(){}.getType();
        OkHttps.sendPost(type,Apiurl.SCHOOL_SUBLEAVE,params,this);
        dialog.show();
    }
    @Override
    public void onSuccess(String uri, Object date) {
           Result< String> data = (Result<String>) date;
           dialog.dismiss();
           if (data.isSuccess()){
            String result  = data.getData();
            Toast.makeText(LeaveAskActivity.this,result,Toast.LENGTH_SHORT).show();
            finish();
          }
    }

    @Override
    public void onError(String uri, String error) {
        dialog.dismiss();
        Toast.makeText(LeaveAskActivity.this,error,Toast.LENGTH_SHORT).show();
    }
}
