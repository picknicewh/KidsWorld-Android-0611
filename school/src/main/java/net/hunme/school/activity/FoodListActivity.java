package net.hunme.school.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hunme.baselibrary.mode.Result;
import net.hunme.baselibrary.network.Apiurl;
import net.hunme.baselibrary.network.OkHttps;
import net.hunme.baselibrary.util.G;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.baselibrary.widget.NoScrollListView;
import net.hunme.school.R;
import net.hunme.school.adapter.FoodListAdapter;
import net.hunme.school.bean.CooikeVo;
import net.hunme.school.bean.DishesVo;
import net.hunme.school.widget.DateView;
import net.hunme.user.util.PublishPhotoUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodListActivity extends BaseFoodActivity   {

    /**
     * 日历view
     */
    private DateView dateView;
    /**
     * 选择日期
     */
    private RelativeLayout rl_calendar;
    /**
     *日期显示
     */
    private static TextView tv_calendar;
    /**
     *适配器
     */
    private FoodListAdapter adapter;
    /**
     *食谱列表显示
     */
    private static NoScrollListView lv_foodlist;
    /**
     * 没有食谱数据显示
     */
    private static TextView tv_nodata;
    /**
     * 广播
     */
    public getFoodListRecevier recevier;
    private int flag=0;
   private List<DishesVo> dishesVos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        G.initDisplaySize(this);
        setContentView(R.layout.activity_food_list);
        initView();

    }
   private void initView(){
       dateView  = $(R.id.dateview);
       rl_calendar = $(R.id.rl_calendar);
       tv_calendar= $(R.id.tv_calendar);
       lv_foodlist = $(R.id.lv_foodlist);
       tv_nodata = $(R.id.tv_nodata);
       setDateView(dateView);
       setRl_calendar(rl_calendar);
       setTv_calendar(tv_calendar);
       setFrom(FOODLISTPAGE);
       registerboradcast();
       rl_calendar.setOnClickListener(this);
       tv_calendar.setText(dateView.getDate());
       getCookBook(dateView.getFormatDate());
   }

    @Override
    protected void onResume() {
        super.onResume();
        if (flag==1){
            getCookBook(dateView.getFormatDate());
            flag = 0;
        }
    }

    public static NoScrollListView getListView(){
        return  lv_foodlist;
    }
    public static TextView getNodataView(){
        return  tv_nodata;
    }
    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
        setCententTitle("食谱");
        if (!UserMessage.getInstance(this).getType().equals("1")){
          setSubTitle("发布食谱");
        }
        setSubTitleOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(FoodListActivity.this,PublishFoodActivity.class);
                startActivity(intent);
                flag = 1;
            }
        });
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
        if (G.isNetworkConnected(this)){
            showLoadingDialog();
        }
    }

    @Override
    public void onSuccess(String uri, Object date) {
        Result<CooikeVo> data = (Result<CooikeVo>) date;
        if (data!=null){
            stopLoadingDialog();
            CooikeVo cooikeVo = data.getData();
            dishesVos = cooikeVo.getDishesList();
            if (dishesVos.size()==0){
                lv_foodlist.setVisibility(View.GONE);
                tv_nodata.setVisibility(View.VISIBLE);
            }else {
                lv_foodlist.setVisibility(View.VISIBLE);
                tv_nodata.setVisibility(View.GONE);
                adapter = new FoodListAdapter(this,dishesVos);
                lv_foodlist.setAdapter(adapter);
                lv_foodlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        PublishPhotoUtil.imageBrowernet(0, (ArrayList<String>) dishesVos.get(position).getCookUrl(),FoodListActivity.this);
                    }
                });
            }

        }
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
                if (from==1){
                    getCookBook(getFormateDate(tv_calendar));
                }
            }
        }
    }
    @Override
    public void onError(String uri, String error) {
        Toast.makeText(this,error,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(recevier);
    }
}
