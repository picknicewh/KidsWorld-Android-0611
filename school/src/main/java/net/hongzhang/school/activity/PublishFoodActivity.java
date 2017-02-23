package net.hongzhang.school.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.school.R;
import net.hongzhang.school.util.PublishPhotoUtil;
import net.hongzhang.school.widget.DateView;
import net.hongzhang.user.util.BitmapCache;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者： wh
 * 时间： 2016/10/10
 * 名称：发布食谱
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class PublishFoodActivity extends BaseFoodActivity {
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
     * 显示
     */
    private GridView gv_food;
    /**
     * 图片列表
     */
    private ArrayList<String> itemList;
    /**
     * 最多选择照片数
     */
    private int maxContent =3;
    /**
     * 发布食谱的编辑框
     */
    private EditText et_food;
    /**
     * 甜食
     */
    private RadioButton rb_sweetFood;
    /**
     * 午餐
     */
    private RadioButton rb_launch;
    /**
     * 发布食谱的类型
     */
    private int type=1;
    private int flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_food);
        initView();
    }
    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
        setCententTitle("食谱");
        setSubTitle("确定");
    }
    private void initView(){
        dateView  = $(R.id.dv_date);
        rl_calendar = $(R.id.rl_calendar);
        tv_calendar= $(R.id.tv_calendar);
        gv_food = $(R.id.gv_food);
        et_food= $(R.id.et_food);
        rb_sweetFood=$(R.id.rb_sweetFood);
        rb_launch=$(R.id.rb_launch);
        itemList = new ArrayList<>();
        flag = 1;
        setItemList(itemList);
        setDateView(dateView);
        setRl_calendar(rl_calendar);
        setTv_calendar(tv_calendar);
        setFrom(FOODLISTPAGE);
        setEt_food(et_food);
        setType(type);
        rl_calendar.setOnClickListener(this);
        tv_calendar.setText(dateView.getDate());
        rb_launch.setOnClickListener(this);
        rb_sweetFood.setOnClickListener(this);
        setSubTitleOnClickListener(this);
        PublishPhotoUtil.showPhoto(this,itemList,gv_food,maxContent);
    }
    @Override
    public void onClick(View view) {
        super.onClick(view);
        int viewId  =view.getId();
        if (viewId==R.id.rb_sweetFood){
            type=1;
            setType(type);
        }else  if (viewId==R.id.rb_launch){
            type=2;
            setType(type);
        }else if (viewId==R.id.tv_subtitle){
            if (flag==1){
                publishCookBook();
                flag=0;
            }
        }
    }

    /**
     * 发布食谱
     */
    public void publishCookBook(){
        if (itemList.size()==0){
            Toast.makeText(this,"请选择图片哦！",Toast.LENGTH_LONG).show();
            flag=1;
            return;
        }
        if (G.isEmteny(tv_calendar.getText().toString())){
            Toast.makeText(this,"日期不能为空！",Toast.LENGTH_LONG).show();
            flag=1;
            return;
        }
        Map<String,Object> params = new HashMap<>();
        params.put("tsId", UserMessage.getInstance(this).getTsId());
        params.put("date",getFormateDate(tv_calendar));
        params.put("type",type);
        params.put("title",et_food.getText().toString());
        Type type=new TypeToken<Result<String>>(){}.getType();
        List<File> list= BitmapCache.getFileList(itemList);
        OkHttps.sendPost(type, Apiurl.SCHOOL_PUBLISHCOOK,params,list,this);
        showLoadingDialog();
    }
    @Override
    public void onSuccess(String uri, Object date) {
        Result<String> data = (Result<String>) date;
        if (data!=null){
            String  result = data.getData();
            Toast.makeText(this,result,Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    @Override
    public void onError(String uri, String error) {
        stopLoadingDialog();
        Toast.makeText(this,error,Toast.LENGTH_SHORT).show();
    }
}
