package net.hongzhang.school.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import net.hongzhang.baselibrary.util.DateUtil;
import net.hongzhang.school.R;
import net.hongzhang.school.adapter.MouthDateAdapter;
import net.hongzhang.school.adapter.WeekTopAdapter;
import net.hongzhang.school.bean.MulDateVo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * 作者： wh
 * 时间： 2016/12/9
 * 名称：收费弹框
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class DatePopWindow2 extends PopupWindow implements View.OnClickListener {
    private View contentView;
    private Context context;
    /**
     * 切换-year
     */
    private ImageView iv_year_left;
    /**
     * 切换+year
     */
    private ImageView iv_year_right;
    /**
     * 切换+year
     */
    private ImageView iv_mouth_left;
    /**
     * 切换+mouth
     */
    private ImageView iv_mouth_right;
    /**
     * 当前年
     */
    private TextView tv_year;
    /**
     * 当前月
     */
    private TextView tv_mouth;
    /**
     * 取消选择
     */
    private TextView tv_concal;
    /**
     * 完成选择
     */
    private TextView tv_finish;
    /**
     * 上面部分的日期选项
     */
    private RecyclerView rv_week_top;
    /**
     * 所有的日期
     */
    private RecyclerView rv_date;
    /**
     * 当前年
     */
    private int currentYear;
    /**
     * 当前月
     */
    private int currentMouth;
    /**
     * 当前日
     */
    private int currentDay;
    /**
     * 过去日
     */
    private int nextDay;
    /**
     * 过去年
     */
    private int nextYear;
    /**
     * 过去月
     */
    private int nextMouth;
    private Calendar calendar;
    private MouthDateAdapter adapter;
    /**
     * 当前月的所有数据
     */
    private List<MulDateVo> dateList;
    private EditText et_date;
    public DatePopWindow2(Context context,EditText et_date) {
        this.context = context;
        this.et_date = et_date;
        init();
        initData();
    }
    private void initData(){
        dateList = new ArrayList<>();
        calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR);
        currentMouth = calendar.get(Calendar.MONTH) + 1;
        currentDay =calendar.get(Calendar.DAY_OF_MONTH);
        nextYear = currentYear;
        nextMouth = currentMouth;
        nextDay = currentDay;
        dateList = getDateList(nextYear,nextMouth,currentYear,currentMouth,currentDay);
        setMouthList(dateList);
        setNextYear(nextYear);
        setMouthText(nextMouth);
    }
    private void setNextYear(int nextYear){
        String year = nextYear + "年";
        tv_year.setText(year);
    }
    private void setMouthText(int nextMouth){
        String mouth;
        if (nextMouth < 10) {
            mouth = "0" + nextMouth + "月";
        } else {
            mouth = nextMouth + "月";
        }
        tv_mouth.setText(mouth);
    }
    public void initView() {
        contentView = LayoutInflater.from(context).inflate(R.layout.muldate_head, null);
        iv_year_left = (ImageView) contentView.findViewById(R.id.iv_year_left);
        iv_year_right = (ImageView) contentView.findViewById(R.id.iv_year_right);
        iv_mouth_left = (ImageView) contentView.findViewById(R.id.iv_mouth_left);
        iv_mouth_right = (ImageView) contentView.findViewById(R.id.iv_mouth_right);
        tv_year = (TextView)contentView.findViewById(R.id.tv_year);
        tv_mouth = (TextView)contentView.findViewById(R.id.tv_mouth);
        tv_concal = (TextView)contentView.findViewById(R.id.tv_concal);
        tv_finish = (TextView)contentView.findViewById(R.id.tv_finish);
        rv_week_top = (RecyclerView)contentView.findViewById(R.id.rv_week_top);
        rv_date = (RecyclerView)contentView.findViewById(R.id.rv_date);
        iv_year_left.setOnClickListener(this);
        iv_year_right.setOnClickListener(this);
        iv_mouth_left.setOnClickListener(this);
        iv_mouth_right.setOnClickListener(this);
        tv_concal.setOnClickListener(this);
        tv_finish.setOnClickListener(this);
        rv_week_top.setAdapter(new WeekTopAdapter(context));
        rv_week_top.setLayoutManager(new GridLayoutManager(context,7));
    }
    private void init() {
        initView();
        //设置SignPopupWindow弹出窗体的宽
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        //设置SignPopupWindow弹出窗体的高
        this.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        //设置SignPopupWindow的View
        this.setContentView(contentView);
        //设置SignPopupWindow弹出窗体可点击
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SignPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        this.setFocusable(true);
        this.setTouchable(true);
        this.setOutsideTouchable(true);
   }
   private void setMouthList(List<MulDateVo> dateList){
       if (adapter!=null){
           adapter =null;
       }
       adapter = new MouthDateAdapter(context,dateList);
       rv_date.setAdapter(adapter);
       rv_date.setLayoutManager(new GridLayoutManager(context,7));
   }
    @Override
    public void onClick(View view) {
        int viewId= view.getId();
        if (viewId== R.id.iv_year_left) {
            nextYear--;
        }else if (viewId==R.id.iv_year_right){
            nextYear++;
        }else if (viewId==R.id.iv_mouth_left){
            nextMouth = nextMouth - 1;
            if (nextMouth < 1) {
                nextYear--;
                nextMouth = 12;
            }
        }else if (viewId==R.id.iv_mouth_right){
            nextMouth = nextMouth + 1;
            if (nextMouth > 12) {
                nextYear++;
                nextMouth = 1;
            }
        }else if (viewId==R.id.tv_concal){
            dismiss();
        }else if (viewId==R.id.tv_finish) {
            StringBuffer buffer = new StringBuffer();
            for (int i = 0 ; i< dateList.size();i++){
                if (MouthDateAdapter.map.get(i)){
                    String date =    DateUtil.getFromatDate(nextYear, nextMouth,
                            Integer.parseInt(dateList.get(i).getDate()));
                    buffer.append(date).append(";");
                }
            }
            if (buffer.length()>0){
                buffer.deleteCharAt(buffer.length()-1);
                et_date.setText(buffer.toString());
            }
            dismiss();
        }
        setNextYear(nextYear);
        setMouthText(nextMouth);
        dateList = getDateList(nextYear,nextMouth,currentYear,currentMouth,currentDay);
        setMouthList(dateList);
    }

    /**
     * 设置每月的天数，对应的日期
     */
    public  List<MulDateVo> getDateList(int year, int mouth,int currentYear,int currentMouth,int currentDay) {
        List<MulDateVo> dataList = new ArrayList<>();
        int rows = 6;
        int week = DateUtil.getWeekofday(year, mouth);
        int days = DateUtil.getMouthDays(year, mouth);
        int lastMouthday = DateUtil.getMouthDays(year, mouth-1) - week+1;
        int date = 1;
        int j = 1;
        for (int row = 1; row <= rows; row++) {
            if (row == 1) {
                for (int i = 0; i < 7; i++) {
                    MulDateVo mulDateVo = new MulDateVo();
                    if (i < week && week != 7) {
                        mulDateVo.setDate(String.valueOf(lastMouthday++));
                        mulDateVo.setSign(-1);
                    } else {
                        mulDateVo.setDate(String.valueOf(date++));
                        mulDateVo.setSign(0);
                    }
                    dataList.add(mulDateVo);
                }
            } else {
                for (int i = 0; i < 7; i++) {
                    MulDateVo mulDateVo = new MulDateVo();
                    if (date > days) {
                        mulDateVo.setDate(String.valueOf(j++));
                        mulDateVo.setSign(1);
                    } else {
                        mulDateVo.setDate(String.valueOf(date++));
                        mulDateVo.setSign(0);

                    }
                    dataList.add(mulDateVo);
                }
            }
        }
       for (int i = 0 ; i<dataList.size();i++){
           MulDateVo mulDateVo = dataList.get(i);
           int mDate =  Integer.parseInt(mulDateVo.getDate());
           int mSign = mulDateVo.getSign();
           if (year==currentYear && mouth==currentMouth){
               if (mSign==0){
                   if (mDate==currentDay){
                       mulDateVo.setSign(2);
                   }else if (mDate<currentDay){
                       mulDateVo.setSign(-1);
                   }else {
                       mulDateVo.setSign(0);
                   }
               }
           }else if (year<currentYear||mouth<currentMouth){
               if (mSign==0){
                   mulDateVo.setSign(-1);
               }else {
                   mulDateVo.setSign(-2);
               }
           }
           dataList.set(i,mulDateVo);
       }
        return dataList;
    }
}
