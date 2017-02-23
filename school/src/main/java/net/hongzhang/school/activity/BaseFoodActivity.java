package net.hongzhang.school.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.widget.LoadingDialog;
import net.hongzhang.school.R;
import net.hongzhang.school.widget.DateView;

import java.util.ArrayList;

/**
 * 作者： wh
 * 时间： 2016/10/11
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public abstract class BaseFoodActivity extends BaseActivity implements OkHttpListener , View.OnClickListener{
    public  static  final  String ACTION_GEFOOD = "net.hongzhang.school.getfoodlist";
    public  static  final  int  FOODLISTPAGE = 1;
    public  static  final  int  PUBLICFOODPAGE = 2;
    /**
    * 日历view
    */
    public DateView dateView;
    /**
     * 选择日期
     */
    public RelativeLayout rl_calendar;
    /**
     *日期显示
     */
    public static TextView tv_calendar;

    /**
     * 来自哪个页面 =1食谱页面 =2 发布食谱页面
     */
    public static int from =1;
    public boolean isvisible = false;
    public     LoadingDialog dialog;
    /**
     * 图片列表
     */
    public ArrayList<String> itemList;
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }
    /**
     * 特定格式日期
     * @param tv_calendar
     */
    public String getFormateDate(TextView tv_calendar){
        String date = tv_calendar.getText().toString();
        String year = date.substring(0,4);
        String mouth =date.substring(5,7);
        String day  = date.substring(8,10);
        String mDate= year+"-"+mouth+"-"+day;
        return mDate;
    }
    @Override
    public void onClick(View view) {
        if (view.getId()== R.id.rl_calendar){
            if (isvisible){
                dateView.setVisibility(View.GONE);
                isvisible = false;
            }else {
                dateView.setVisibility(View.VISIBLE);
                isvisible = true;
            }
        }
    }
    public static TextView getCalender(){
        return tv_calendar;
    }

    public void setDateView(DateView dateView) {
        this.dateView = dateView;
    }

    public void setRl_calendar(RelativeLayout rl_calendar) {
        this.rl_calendar = rl_calendar;
    }

    public  void setTv_calendar(TextView tv_calendar) {
        this.tv_calendar = tv_calendar;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    //发布食谱需要的
    public String date;
    public EditText et_food;
    public int type;
    public void setEt_food(EditText et_food) {
        this.et_food = et_food;
    }
    public void setType(int type) {
        this.type = type;
    }
    public void setItemList(ArrayList<String> itemList) {
        this.itemList = itemList;
    }
}
