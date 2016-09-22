package net.hunme.school.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.baselibrary.mode.Result;
import net.hunme.baselibrary.network.Apiurl;
import net.hunme.baselibrary.network.OkHttpListener;
import net.hunme.baselibrary.network.OkHttps;
import net.hunme.baselibrary.util.G;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.school.R;
import net.hunme.school.adapter.FoodListAdapter;
import net.hunme.school.bean.CooikeVo;
import net.hunme.school.bean.DishesVo;
import net.hunme.school.widget.DateView;
import net.hunme.status.widget.ListViewForScrollView;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodListActivity extends BaseActivity implements View.OnClickListener, OkHttpListener {
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
    private TextView tv_calendar;
    /**
     *适配器
     */
    private FoodListAdapter adapter;
    /**
     *食谱列表显示
     */
    private ListViewForScrollView lv_foodlist;
    /**
     * 没有食谱数据显示
     */
    private TextView tv_nodata;
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
       rl_calendar.setOnClickListener(this);
       tv_calendar.setText(dateView.getDate());
       getCookBook(dateView.getDate());
   }
    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
        setCententTitle("食谱");
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.rl_calendar){
            dateView.setVisibility(View.VISIBLE);
            tv_calendar.setText(dateView.getDate());
            getCookBook(dateView.getFormatDate());
        }
    }
    /**
     * 获取食谱
     * @param date 日期
     */
    private void getCookBook(String date){
        Map<String,Object> params = new HashMap<>();
        params.put("tsId", UserMessage.getInstance(this).getTsId());
        params.put("date",date);
        Type type = new TypeToken<Result<CooikeVo>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.SCHOOL_GETCOOKBOOK,params,this);
    }

    @Override
    public void onSuccess(String uri, Object date) {
        Result<CooikeVo> data = (Result<CooikeVo>) date;
        if (data!=null){
            CooikeVo cooikeVo = data.getData();
            List<DishesVo> dishesVos = cooikeVo.getDishesList();
            if (dishesVos.size()==0){
                lv_foodlist.setVisibility(View.GONE);
                tv_nodata.setVisibility(View.VISIBLE);
            }else {
                lv_foodlist.setVisibility(View.VISIBLE);
                tv_nodata.setVisibility(View.GONE);
                adapter = new FoodListAdapter(this,dishesVos);
                lv_foodlist.setAdapter(adapter);
            }

        }
    }

    @Override
    public void onError(String uri, String error) {
        Toast.makeText(this,error,Toast.LENGTH_SHORT).show();
    }
}
