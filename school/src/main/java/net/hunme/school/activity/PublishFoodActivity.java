package net.hunme.school.activity;

import android.Manifest;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.hunme.school.R;
import net.hunme.school.util.PublishPhotoUtil;
import net.hunme.school.widget.DateView;

import java.util.ArrayList;

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
    // 访问相册所需的全部权限
    private final String[] PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE, //读写权限
            Manifest.permission.CAMERA
    };
    private int maxContent =3;
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
        setDateView(dateView);
        setRl_calendar(rl_calendar);
        setTv_calendar(tv_calendar);
        setFrom(FOODLISTPAGE);
        registerboradcast();
        itemList = new ArrayList<>();
        PublishPhotoUtil.showPhoto(this,itemList,gv_food,maxContent);
        rl_calendar.setOnClickListener(this);
        tv_calendar.setText(dateView.getDate());
    }
    @Override
    public void onSuccess(String uri, Object date) {

    }
    @Override
    public void onError(String uri, String error) {

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(recevier);
    }
}
