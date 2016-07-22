package net.hunme.school.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.school.R;
import net.hunme.school.widget.CustomDateTimeDialog;

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
    private Spinner sp_name;
    private Spinner sp_eat;

    private  String[] student = new String[]{"王小二","刘德华","吴亦凡","吴用","周磊","鹿晗","郑爽","大幂幂","吴彦祖","刘诗诗"};
    private ArrayAdapter<String> adapter;

    private CustomDateTimeDialog customDateTimeDialog;

    private long lastClickTime;
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
        tv_end.setText("请选择时间");
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,student);
        sp_name.setAdapter(adapter);
        customDateTimeDialog = new CustomDateTimeDialog(LeaveAskActivity.this,R.style.MyDialog);

    }
    /**
     * 设置选择时间
     */
    public void setDateTextView(long millis) {
        if (millis > System.currentTimeMillis()) {
            millis = System.currentTimeMillis();
            Toast.makeText(this, "日期不能设置超过未来的日子哦！", Toast.LENGTH_LONG).show();
        }
        Date date = new Date(millis);
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
        String datestr = format.format(date);
        tv_start.setText(datestr);
        tv_end.setText(datestr);
    }
    private void init(){
        ll_start = $(R.id.ll_istarttime);
        ll_end = $(R.id.ll_iendtime);
        tv_end = $(R.id.tv_iendtime);
        tv_start = $(R.id.tv_istarttime);
        sp_eat = $(R.id.sp_eat);
        sp_name = $(R.id.sp_name);
        ll_end.setOnClickListener(this);
        ll_start.setOnClickListener(this);
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
        if (view.getId()==R.id.ll_istarttime){
            if (isFastDoubleClick()) {
                return;
            } else {
                markLastClickTime();
                customDateTimeDialog.show();

            }
        }else if (view.getId()==R.id.ll_iendtime){
            if (isFastDoubleClick()) {
                return;
            } else {
                markLastClickTime();
                customDateTimeDialog.show();
            }
        }
    }
}
