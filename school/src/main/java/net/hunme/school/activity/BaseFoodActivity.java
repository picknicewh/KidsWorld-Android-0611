package net.hunme.school.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.baselibrary.mode.Result;
import net.hunme.baselibrary.network.Apiurl;
import net.hunme.baselibrary.network.OkHttpListener;
import net.hunme.baselibrary.network.OkHttps;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.baselibrary.widget.LoadingDialog;
import net.hunme.school.R;
import net.hunme.school.bean.CooikeVo;
import net.hunme.school.widget.DateView;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者： Administrator
 * 时间： 2016/10/11
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public abstract class BaseFoodActivity extends BaseActivity implements OkHttpListener , View.OnClickListener{
    public  static  final  String ACTION_GEFOOD = "net.hunme.school.getfoodlist";
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
     * 广播
     */
    public getFoodListRecevier recevier;


    /**
     * 来自哪个页面 =1食谱页面 =2 发布食谱页面
     */
    public static int from =1;
    public boolean isvisible = false;
    public     LoadingDialog dialog;
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);


    }
    public void registerboradcast(){
        IntentFilter intentFilter = new IntentFilter(ACTION_GEFOOD);
        recevier = new getFoodListRecevier();
        registerReceiver(recevier,intentFilter);
    }
    private  class  getFoodListRecevier extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_GEFOOD)){
                String date = tv_calendar.getText().toString();
                String year = date.substring(0,4);
                String mouth =date.substring(5,7);
                String day  = date.substring(8,10);
                String mDate= year+"-"+mouth+"-"+day;
                if (from==1){
                    getCookBook(mDate);
                }else {
                    publishCookBook(mDate);
                }
            }
        }
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
    /**
     * 获取食谱
     * @param date 日期
     */
    public  void getCookBook(String date){
        Map<String,Object> params = new HashMap<>();
        params.put("tsId", UserMessage.getInstance(this).getTsId());
        params.put("date",date);
        Type type = new TypeToken<Result<CooikeVo>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.SCHOOL_GETCOOKBOOK,params,this);
        showLoadingDialog();
    }
    /**
     * 发布食谱
     * @param date 日期
     */
    public void publishCookBook(String date){
        Map<String,Object> params = new HashMap<>();
        params.put("tsId", UserMessage.getInstance(this).getTsId());
        params.put("date",date);
        Type type = new TypeToken<Result<CooikeVo>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.SCHOOL_GETCOOKBOOK,params,this);
        showLoadingDialog();
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

}
