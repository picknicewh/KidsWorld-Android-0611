package net.hongzhang.school.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.hongzhang.baselibrary.util.G;
import net.hongzhang.school.R;

import java.util.Calendar;

/**
 * 作者： Administrator
 * 时间： 2016/9/21
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class MouthView extends LinearLayout {
    /**
     * 年
     */
    private int year;
    /**
     * 月
     */
    private int mouth;
    /**
     * 日
     */
    private int day;
    private Context context;
    private  String[][] mouths = new String[][]
            {{"一月","二月","三月","四月"},
            {"五月","六月","七月","八月"},
            {"久月","十月","十一月","十二月"}
         };
    public MouthView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        initView(context,attrs, 0);
    }
    public MouthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context,attrs, defStyleAttr);
        AddView(year,mouth);
    }
    /**
     * 添加view
     *  @param year 年
     *  @param  mouth 月
     */
    private void AddView(int year, int mouth) {

    }
    /**
     * 设置头部星期文字
     */
    private void  setMouthView(){
        LinearLayout ll_datemain  = new LinearLayout(context);
     //   setLinearlayoutParam(linearLayout);
        for (int i = 0 ; i <4;i++){
            LinearLayout linearLayout  = new LinearLayout(context);
            for (int j = 0 ; j<3;j++){
                TextView textView = new TextView(context);
                textView.setText(mouths[i][j]);
                textView.setTextColor(Color.BLACK);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                textView.setGravity(Gravity.CENTER);
                setMouthParam(textView);
                linearLayout.addView(textView);
                ll_datemain.addView(linearLayout);
            }
        }
        this.addView(ll_datemain);
    }
    /**
     * 设置头部星期的参数
     * @param  view 控件
     */
    private void setMouthParam(TextView view){
        if (view!=null){
            this.removeView(view);
        }
        int width =(G.size.W)/4;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.weight = 1;
        lp.gravity = Gravity.CENTER;
        view.setLayoutParams(lp);
    }

    /**
     * 设置整体布局，和初始化数据
     *  @param context
     *  @param  attrs
     *  @param  defStyleAttr
     */
    private void initView(Context context,AttributeSet attrs, int defStyleAttr){
        this.context = context;
        this.setBackgroundColor(context.getResources().getColor(R.color.harf_white));
        Calendar calendar =  Calendar.getInstance();
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MouthView, defStyleAttr, 0);
        year = a.getInteger(R.styleable.MouthView_my_year, calendar.get(Calendar.YEAR));
        mouth = a.getInteger(R.styleable.MouthView_my_mouth, calendar.get(Calendar.MONTH))+1;
    }
}
