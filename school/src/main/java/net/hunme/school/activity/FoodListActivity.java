package net.hunme.school.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.hunme.baselibrary.mode.Result;
import net.hunme.baselibrary.util.G;
import net.hunme.baselibrary.widget.NoScrollListView;
import net.hunme.school.R;
import net.hunme.school.adapter.FoodListAdapter;
import net.hunme.school.bean.CooikeVo;
import net.hunme.school.bean.DishesVo;
import net.hunme.school.widget.DateView;

import java.util.List;

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
       /* if (!UserMessage.getInstance(this).getType().equals("1")){
          setSubTitle("发布食谱");
        }*/
        setSubTitle("发布食谱");
        setSubTitleOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(FoodListActivity.this,PublishFoodActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onSuccess(String uri, Object date) {
        Result<CooikeVo> data = (Result<CooikeVo>) date;
        if (data!=null){
            stopLoadingDialog();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(recevier);
    }
}
