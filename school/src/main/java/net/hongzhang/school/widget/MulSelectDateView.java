package net.hongzhang.school.widget;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.hongzhang.baselibrary.util.DateUtil;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.school.R;
import net.hongzhang.school.activity.MedicineEntrustActivity;
import net.hongzhang.school.util.DateDb;

import java.util.Calendar;

/**
 * 作者： wh
 * 时间： 2016/11/7
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class MulSelectDateView extends RelativeLayout implements View.OnClickListener {
    private static final String weeks[] = new String[]{"日", "一", "二", "三", "四", "五", "六"};
    private static final int TGA_MOUTH_LEFT = 1;
    private static final int TGA_MOUTH_RIGNT = 2;
    private static final int TGA_YEAR_LEFT = 3;
    private static final int TGA_YEAR_RIGHT = 4;
    private static final int TGA_CANCEL = 5;
    private static final int TGA_FINISH = 6;
    private static final int TGA_MOUTH = 7;
    private static final int TGA_YEAR = 8;
    private static final int TAG_SELECT_DATE = 9;
    private static final int TAG_UNSELECT_DATE = 10;
    private static final int TAG_OTHER_DATE = 11;
    /**
     * 当月日期的颜色
     */
    private final static int CURRENT_MOUTH_COLOR = Color.parseColor("#494949");
    /**
     * 其他月的颜色
     */
    private final static int OTHER_MOUTH_COLOR = Color.parseColor("#a0a0a0");
    /**
     * 其他月的其他月的颜色
     */
    private final static int OTHER_MOUTH_OTHER_COLOR = Color.parseColor("#e1e1e1");
    /**
     * 选中背景的颜色
     */
    private final static int SELECT_BG_COLOR = Color.parseColor("#8dc700");

    /**
     * 选中的颜色
     */
    private final static int BG_COLOR = 0xffffff;
    /**
     * 蒙层的颜色
     */
    public static final int MASK_COLOR = 0x80000000;
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

    /**
     * 日历
     */
    private Calendar calendar;

    /**
     * 日历的矩形绘画范围
     */
    private Rect frameRect;

    /**
     * 日历里面的间距
     */
    private int padding;
    private Context context;
    /**
     * 标记
     */
    private int sign;
    /**
     * 选中的日期
     */
    private DateDb dateDb;
    private SQLiteDatabase db;

    public MulSelectDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0);
    }

    public MulSelectDateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;
        dateDb = new DateDb(context);
        db = dateDb.getWritableDatabase();
        calendar = Calendar.getInstance();
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MulSelectDateView, defStyleAttr, 0);
        currentYear = array.getInteger(R.styleable.MulSelectDateView_dv_year, calendar.get(Calendar.YEAR));
        currentMouth = array.getInteger(R.styleable.MulSelectDateView_dv_mouth, calendar.get(Calendar.MONTH)) + 1;
        currentDay = array.getInteger(R.styleable.MulSelectDateView_dv_day, calendar.get(Calendar.DAY_OF_MONTH));
        nextYear = currentYear;
        nextMouth = currentMouth;
        nextDay = currentDay;
        dateDb.delete(db);
        insetDate();
        frameRect = new Rect();
        padding = G.dp2px(context, 10);
    }

    private void addTopView(Context context, LinearLayout ll) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams lps = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lps.setMargins(10, 10, 10, 10);
        linearLayout.setLayoutParams(lps);
        ll.addView(linearLayout);
        //添加年和左右两边按钮
        getImageView(TGA_YEAR_LEFT, context, linearLayout);
        getTextView(TGA_YEAR, context, linearLayout);
        getImageView(TGA_YEAR_RIGHT, context, linearLayout);
        //添加月和左右两边按钮
        getImageView(TGA_MOUTH_LEFT, context, linearLayout);
        getTextView(TGA_MOUTH, context, linearLayout);
        getImageView(TGA_MOUTH_RIGNT, context, linearLayout);
        getTextView(TGA_CANCEL, context, linearLayout);
        getTextView(TGA_FINISH, context, linearLayout);

    }
    /**
     * 获取头部不同内容的textView
     * @param  tag 标签
     * @param  context
     * @param  linearLayout
     */
    private TextView getTextView(int tag, Context context, LinearLayout linearLayout) {
        TextView textView = new TextView(context);
        switch (tag) {
            case TGA_YEAR:
                String year = nextYear + "年";
                textView.setText(year);
                break;
            case TGA_MOUTH:
                String mouth;
                if (nextMouth < 10) {
                    mouth = "0" + nextMouth + "月";
                } else {
                    mouth = nextMouth + "月";
                }
                textView.setText(mouth);
                break;
            case TGA_FINISH:
                //文字与左右箭头按钮间距
                textView.setTextColor(SELECT_BG_COLOR);
                textView.setText("完成");
                textView.setPadding(padding, 0, padding, 0);
                break;
            case TGA_CANCEL:
                textView.setTextColor(OTHER_MOUTH_COLOR);
                textView.setPadding(padding, 0, 0, 0);
                textView.setText("取消");
                break;
        }
        textView.setTag(tag);
        textView.setOnClickListener(this);
        linearLayout.addView(textView);
        return textView;
    }
    /**
     * 获取头部不同内容的ImageView
     * @param  tag 标签
     * @param  context
     * @param  ll
     */
    private ImageView getImageView(int tag, Context context, LinearLayout ll) {
        ImageView imageView = new ImageView(context);
        switch (tag) {
            case TGA_YEAR_LEFT:
                imageView.setImageResource(R.mipmap.ic_green_left);
                break;
            case TGA_YEAR_RIGHT:
                imageView.setImageResource(R.mipmap.ic_green_right);
                break;
            case TGA_MOUTH_LEFT:
                imageView.setImageResource(R.mipmap.ic_green_left);
                break;
            case TGA_MOUTH_RIGNT:
                imageView.setImageResource(R.mipmap.ic_green_right);
                break;
        }
        //文字与左右箭头按钮间距
        LinearLayout.LayoutParams lps = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lps.setMargins(15, 0, 15, 0);
        imageView.setTag(tag);
        imageView.setLayoutParams(lps);
        imageView.setOnClickListener(this);
        ll.addView(imageView);
        return imageView;
    }

    /**
     * 设置下划线
     */
    private void addLine(Context context, LinearLayout ll) {
        View line = new View(context);
        line.setBackgroundColor(context.getResources().getColor(R.color.line_gray));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, G.dp2px(context, 1));
        line.setLayoutParams(lp);
        ll.addView(line);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int marginLeft = (int) (width * 0.05);
        int marginTop = (int) (height * 0.25);
        frameRect.left = marginLeft;
        frameRect.right = width - marginLeft;
        frameRect.top = marginTop;
        frameRect.bottom = height - marginTop;
        addMainView(context);
    }

    /**
     * 绘画主体
     * @param  context
     */
    private void addMainView(Context context) {
        this.setBackgroundColor(MASK_COLOR);
        LinearLayout linearLayout = new LinearLayout(context);
        LayoutParams lps = new LayoutParams(frameRect.width(), frameRect.height());
        lps.addRule(RelativeLayout.CENTER_IN_PARENT);
        linearLayout.setLayoutParams(lps);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackgroundColor(Color.WHITE);
        this.addView(linearLayout);
        this.addTopView(context, linearLayout);
        this.addLine(context, linearLayout);
        this.addCalenderView(context, linearLayout);
    }

    /**
     * 绘制下面的星期
     *
     * @param context 文本
     */
    private void addCalenderView(Context context, LinearLayout ll) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        int eachWidth = (frameRect.width() - padding * 14) / 7;
        //绘制星期汉字
        for (int i = 0; i < weeks.length; i++) {
            TextView textView = new TextView(context);
            textView.setText(weeks[i]);
            textView.setTextColor(OTHER_MOUTH_COLOR);
            LinearLayout.LayoutParams lps = new LinearLayout.LayoutParams
                    (eachWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            lps.setMargins(padding, 0, padding, 0);
            textView.setLayoutParams(lps);
            textView.setGravity(Gravity.CENTER);
            linearLayout.addView(textView);
        }
        ll.addView(linearLayout);
        drawDateView(ll);
    }


    /**
     * 设置每月的天数，对应的日期
     *
     * @param ll 文本
     */
    private void drawDateView(LinearLayout ll) {
        LinearLayout ll_calender = new LinearLayout(context);
        ll_calender.setOrientation(LinearLayout.VERTICAL);
        int rows = 6;
        int week = DateUtil.getWeekofday(nextYear, nextMouth);
        int days = DateUtil.getMouthDays(nextYear, nextMouth);
        int lastMouthday = DateUtil.getMouthDays(nextYear, nextMouth) - week;
        Log.i("ssss", week + "=============week");
        Log.i("sss", days + "=============days");
        Log.i("sss", lastMouthday + "=============lastMouthday");
        int date = 1;
        int count = 0;
        int j = 1;
        for (int row = 1; row <= rows; row++) {
            if (row == 1) {
                LinearLayout linearLayout = new LinearLayout(context);
                for (int i = 0; i < 7; i++) {
                    if (i < week && week != 7) {
                        setDate(lastMouthday, linearLayout, count);
                        lastMouthday++;
                    } else {
                        setDate(date, linearLayout, count);
                        date++;
                    }
                    count++;
                }
                ll_calender.addView(linearLayout);
            } else {
                LinearLayout linearLayout = new LinearLayout(context);
                for (int i = 0; i < 7; i++) {
                    if (date > days) {
                        setDate(j++, linearLayout, count);
                    } else {
                        setDate(date, linearLayout, count);
                        date++;
                    }
                    count++;
                }

                ll_calender.addView(linearLayout);
            }
        }
        ll.addView(ll_calender);
    }

    /**
     * 设置每个日期的数据和显示设置
     *
     * @param day          日期
     * @param linearLayout
     */
    private void setDate(final int day, LinearLayout linearLayout, final int tag) {
        int eachWidth = (frameRect.width() - padding * 14) / 7;
        final TextView tv_day = new TextView(context);
        tv_day.setText(String.valueOf(day));
        tv_day.setTextSize(16);
        tv_day.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(eachWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(padding, padding, padding, padding);
        tv_day.setLayoutParams(lp);
        tv_day.setTag(tag);
        int state = dateDb.getState(db, tag);
        if (state == 2) {
            tv_day.setTextColor(Color.WHITE);
            tv_day.setBackgroundResource(R.drawable.circle_green_bg);
        } else if (state == 1) {
            tv_day.setTextColor(CURRENT_MOUTH_COLOR);
            tv_day.setBackgroundColor(Color.WHITE);
        } else if (state == 0 || state == 3) {
            tv_day.setTextColor(OTHER_MOUTH_COLOR);
            tv_day.setBackgroundColor(Color.WHITE);
        } else if (state == 4) {
            tv_day.setTextColor(OTHER_MOUTH_OTHER_COLOR);
            tv_day.setBackgroundColor(Color.WHITE);
        }
        linearLayout.addView(tv_day);
        if (state == 1 || state == 2) {
            tv_day.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dateDb.getSign(db, tag) == 1 && dateDb.getSelectedSize(db, 2) < 5) {
                        dateDb.updateSign(db, tag, 0);
                        dateDb.updateState(db, tag, 2);
                    } else {
                        dateDb.updateSign(db, tag, 1);
                        dateDb.updateState(db, tag, 1);
                    }
                    rushData();
                }
            });
        } else if (state == 3) {
            tv_day.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dateDb.getSign(db, tag) == 1 && dateDb.getSelectedSize(db, 2) < 5) {
                        dateDb.updateSign(db, tag, 0);
                        dateDb.updateState(db, tag, 2);
                    } else {
                        dateDb.updateSign(db, tag, 1);
                        dateDb.updateState(db, tag, 3);
                    }
                    rushData();
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        int tag = (int) view.getTag();
        switch (tag) {
            case TGA_YEAR_LEFT:
                nextYear--;
                dateDb.delete(db);
                insetDate();
                rushData();
                break;
            case TGA_YEAR_RIGHT:
                nextYear++;
                dateDb.delete(db);
                insetDate();
                rushData();
                break;
            case TGA_MOUTH_LEFT:
                nextMouth = nextMouth - 1;
                if (nextMouth < 1) {
                    nextYear--;
                    nextMouth = 12;
                }
                dateDb.delete(db);
                insetDate();
                rushData();
                break;
            case TGA_MOUTH_RIGNT:
                nextMouth = nextMouth + 1;
                if (nextMouth > 12) {
                    nextYear++;
                    nextMouth = 1;
                }
                dateDb.delete(db);
                insetDate();
                rushData();
                break;
            case TGA_FINISH:

                MedicineEntrustActivity.getEditText().setText(dateDb.getDate(db, 2));
                dismiss();
                break;
            case TGA_CANCEL:
                dismiss();
                break;

        }
    }

    /**
     * 刷新数据
     */
    private void rushData() {

        this.removeAllViews();
        this.addMainView(context);
    }

    //state=0 state=4不可点击(已经过去的日期)，state=3表示未来日期 state=2表示当天
    private void insetDate() {
        int week = DateUtil.getWeekofday(nextYear, nextMouth);
        int days = DateUtil.getMouthDays(nextYear, nextMouth);
        int date = 1;
        int count = 0;
        int j = 1;
        for (int row = 1; row <= 6; row++) {
            if (nextMouth == currentMouth && nextYear == currentYear) {
                Log.i("2222", "=======当前月======");
                if (row == 1) {
                    for (int i = 0; i < 7; i++) {
                        if (nextMouth == 1) {
                            dateDb.insert(db, count, 1, DateUtil.getFromatDate(nextYear - 1, 12, date), 0);
                        } else {
                            dateDb.insert(db, count, 1, DateUtil.getFromatDate(nextYear, nextMouth - 1, date), 0);
                        }
                        date++;
                        count++;
                    }
                } else {
                    for (int i = 0; i < 7; i++) {
                        if (date > days) {
                            if (nextMouth == 12) {

                                dateDb.insert(db, count, 1, DateUtil.getFromatDate(nextYear + 1, 1, j), 3);
                            } else {
                                dateDb.insert(db, count, 1, DateUtil.getFromatDate(nextYear, nextMouth + 1, j), 3);
                            }
                            j++;
                        } else {
                            if (date == currentDay) {
                                //本月的当前日期state=2
                                dateDb.insert(db, count, 0, DateUtil.getFromatDate(nextYear, nextMouth, date), 2);
                            } else if (date < currentDay) {
                                //本月小于当前日期state=0
                                dateDb.insert(db, count, 1, DateUtil.getFromatDate(nextYear, nextMouth, date), 0);
                            } else {
                                //本月大于当前日期
                                dateDb.insert(db, count, 1, DateUtil.getFromatDate(nextYear, nextMouth, date), 1);
                            }
                            date++;
                        }

                        count++;
                    }
                }
            }//这个月之后的日期
            else if ((nextMouth > currentMouth && nextYear == currentYear) || nextYear > currentYear) {
                Log.i("2222", "=======下个月======");
                if (row == 1) {
                    for (int i = 0; i < 7; i++) {
                        if (i < week && week != 7) {
                            if (nextMouth == 1) {
                                dateDb.insert(db, count, 1, DateUtil.getFromatDate(nextYear - 1, 12, date), 3);
                            } else {
                                dateDb.insert(db, count, 1, DateUtil.getFromatDate(nextYear, nextMouth - 1, date), 3);
                            }
                        } else {
                            dateDb.insert(db, count, 1, DateUtil.getFromatDate(nextYear, nextMouth, date), 1);
                            date++;
                        }
                        count++;
                    }
                } else {
                    for (int i = 0; i < 7; i++) {
                        if (date > days) {
                            if (nextMouth == 12) {
                                dateDb.insert(db, count, 1, DateUtil.getFromatDate(nextYear + 1, 1, j), 3);
                            } else {
                                dateDb.insert(db, count, 1, DateUtil.getFromatDate(nextYear, nextMouth + 1, j), 3);
                            }
                            j++;
                        } else {
                            dateDb.insert(db, count, 1, DateUtil.getFromatDate(nextYear, nextMouth, date), 1);
                            date++;
                        }
                        count++;
                    }
                }
            } else {
                Log.i("2222", "=======上个月======");
                //这个月的之前的月份
                if (row == 1) {
                    for (int i = 0; i < 7; i++) {
                        if (i < week && week % 7 != 0) {
                            if (nextMouth == 1) {

                                dateDb.insert(db, count, 1, DateUtil.getFromatDate(nextYear - 1, 12, date), 4);
                            } else {
                                dateDb.insert(db, count, 1, DateUtil.getFromatDate(nextYear, nextMouth - 1, date), 4);
                            }
                        } else {
                            dateDb.insert(db, count, 1, DateUtil.getFromatDate(nextYear, nextMouth, date), 0);
                            date++;
                        }
                        count++;
                    }
                } else {
                    for (int i = 0; i < 7; i++) {
                        if (date > days) {
                            if (nextMouth == 12) {
                                dateDb.insert(db, count, 1, DateUtil.getFromatDate(nextYear + 1, 1, j), 4);
                            } else {
                                dateDb.insert(db, count, 1, DateUtil.getFromatDate(nextYear, nextMouth + 1, j), 4);
                            }
                            j++;
                        } else {
                            dateDb.insert(db, count, 1, DateUtil.getFromatDate(nextYear, nextMouth, date), 0);
                            date++;
                        }
                        count++;
                    }
                }
            }
        }
    }

    int dwonx = 0;
    int upx = 0;
    int dwony = 0;
    int upy = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int mimBetween = 10;
        Log.i("ssssss", "upx:" + event.getX() + "===" + "dwonx:" + event.getY() + "===");
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                upx = (int) event.getX();
                upy = (int) event.getY();
                if (upx < dwonx) {
                    //左边
                    nextMouth = nextMouth - 1;
                    if (nextMouth < 1) {
                        nextYear--;
                        nextMouth = 12;
                    }
                } else {
                    //右边
                    nextMouth = nextMouth + 1;
                    if (nextMouth > 12) {
                        nextYear++;
                        nextMouth = 1;
                    }
                }
                dateDb.delete(db);
                insetDate();
                rushData();
                int slide = (int) Math.sqrt((upx - dwonx) * (upx - dwonx) + (upy - dwony) * (upy - dwony));
                if (slide > mimBetween) {
                }
                break;
            case MotionEvent.ACTION_DOWN:
                dwonx = (int) event.getX();
                dwony = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
        }
        return super.onTouchEvent(event);
    }

    public void show() {
        this.setVisibility(VISIBLE);
    }

    public void dismiss() {
        this.setVisibility(GONE);
        Intent intent = new Intent(DatePopWindow.DISMISS);
        context.sendBroadcast(intent);
    }
}
