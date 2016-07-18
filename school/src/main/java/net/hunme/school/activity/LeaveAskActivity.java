package net.hunme.school.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.hunme.baselibrary.activity.BaseActivity;
import net.hunme.school.R;
import net.hunme.school.util.DateTimePickDialogUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 作者： Administrator
 * 时间： 2016/7/18
 * 名称：请假申请
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class LeaveAskActivity extends BaseActivity implements View.OnClickListener{

    private LinearLayout ll_start;
    private LinearLayout ll_end;
    private TextView tv_start;
    private TextView tv_end;
    /**
     * 初始化开始时间
     */

    private String initStartDateTime ;
    /**
     * 初始化结束时间
     */

    private String initEndDateTime ;
    private SimpleDateFormat simpleDateFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informask);
        init();
    }

    @Override
    protected void setToolBar() {
      setLiftImage(R.mipmap.ic_arrow_lift);
      setLiftOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              finish();
          }
       });
        setCententTitle("请假申请");
        setSubTitle("完成");
    }
    private void initdata(){
        simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 hh:mm");
        initStartDateTime = simpleDateFormat.format(new Date());
        initEndDateTime = simpleDateFormat.format(new Date());
        tv_start.setText(initStartDateTime);
        tv_end.setText("请选择时间");
    }
    private void init(){
        ll_start = $(R.id.ll_istarttime);
        ll_end = $(R.id.ll_iendtime);
        tv_end = $(R.id.tv_iendtime);
        tv_start = $(R.id.tv_istarttime);
        ll_end.setOnClickListener(this);
        ll_start.setOnClickListener(this);
        initdata();
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.ll_istarttime){
            DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
                    LeaveAskActivity.this, initStartDateTime);
            dateTimePicKDialog.dateTimePicKDialog(tv_start);
        }else if (view.getId()==R.id.ll_iendtime){
            DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
                    this, initEndDateTime);
            dateTimePicKDialog.dateTimePicKDialog(tv_end);
        }
    }
}
