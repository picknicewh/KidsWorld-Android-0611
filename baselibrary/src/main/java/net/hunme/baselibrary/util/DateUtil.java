package net.hunme.baselibrary.util;


import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	private final static SimpleDateFormat DATE_TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private final static SimpleDateFormat DATE = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	public static Date parseDateTime(String dateStr) {
		Date date = null;
		try {
			date = DATE_TIME.parse(dateStr);
		} catch (Exception e) {
			return null;
		}
		return date;
	}

	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	public static String formatDateTime(Date date) {
		if (date != null) {
			return DATE_TIME.format(date);
		}
		return "";
	}

	/**
	 * yyyy-MM-dd
	 */
	public static Date parseDate(String dateStr) {
		Date date = null;
		try {
			date = DATE.parse(dateStr);
		} catch (Exception e) {
			return null;
		}
		return date;
	}

	/**
	 * yyyy-MM-dd
	 */
	public static String formatDate(Date date) {
		if (date != null) {
			return DATE.format(date);
		}
		return "";
	}

	/**
	 * 获取当前时间
	 */
	public static String getCurrentDateTime() {
		return formatDateTime(new Date());
	}

	/**
	 * 获取当前日期
	 */
	public static String getCurrentDate() {
		return formatDate(new Date());
	}

	/**
	 * 获取N天后的日期
	 * 
	 * @return
	 */
	public static String getNDaysLaterDate(int n) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, n);
		return formatDate(calendar.getTime());
	}

	/**
	 * 比较目标时间与当前时间的天数差
	 */
	public static int getDaysLeft(String dateStr) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseDate(dateStr));//传
		int targetDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
		calendar.setTime(new Date());//现在
		int currentDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
		return targetDayOfYear - currentDayOfYear;
	}
	/**
	/**
	 * 获取传入的date为星期几
	 * 
	 * @param dt
	 * @return
	 */
	public static String getWeekOfDate(Date dt) {
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}
	
	/**
	 * 获取i天后的工作日
	 * @param i 如今天星期4,2或3或4天后就是星期一
	 * @return yyyy年MM月dd日 HH时
	 */
	public static String nextBusinessDay(int i) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时");
		Date dt = new Date();
		dt = new Date(dt.getTime()+i*86400000L);
		String s = getWeekOfDate(dt);
		if(s.equals("星期六")){
			dt.setTime(dt.getTime()+2*86400000L);
		}else if(s.equals("星期日")){
			dt.setTime(dt.getTime()+86400000L);
		}else{
			dt.setTime(dt.getTime());
		}
        return sdf.format(dt);
    }
	
	/**
	 * 获取传入的yyyy-MM-dd为星期几
	 * 
	 * @param str
	 * @return
	 */
	public static String getWeekOfDate(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		try {
			date = sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}
	/**
	 * 获取传入的yyyy-MM-dd与今天间隔i天的时间
	 *
	 * @param i
	 * @return
	 */
	public static String getIntrventDaysTime(int i){
		String date;
		long l = new Date().getTime()-(1000*60*60*24*i);
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
		date = sdformat.format(new Date(l));
		return  date;
	}
	public static void main(String[] args) {
		System.out.println(getDaysLeft("2015-6-27"));
	}

	/**
	 * 获取月份
	 */
	public static String getMyMouth(String time){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String date = format.format(new Date(Long.parseLong(time)));
		String mouth=  getMouthByChinese(Integer.parseInt(date.substring(5,7)));

		return mouth ;
	}
	/**
	 * 获取日期
	 */
	public static String getMyDay(String time){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String date = format.format(new Date(Long.parseLong(time)));
		String day  =date.substring(8,10);
		return day ;
	}
	/**
	 * 获取多少列
	 * @param  mouth 月
	 * @param  year 年
	 */
	public static int  getRows(int year,int mouth){
		int rows;
		int days= getMouthDays(year,mouth);
		int week = getWeekofday(year,mouth);
		int totledays = week+days;
		rows =totledays/7;
		if (totledays%7!=0) {
			rows = rows+1;
		}
		return rows;
	}
	/**
	 *获取某年某月的第一天是星期几
	 * @param  year 年
	 * @param  month 月
	 */
	public static int getWeekofday(int year,int month)
	{
		int day =1;
		if (month == 1 || month == 2) {
			month+= 12;
			year--;
		}
		int week = (day + 2 * month + 3 * (month + 1) / 5 + year + year / 4 - year / 100 + year / 400) % 7;
		return  week+1;
	}
	/**
	 * 判读是否是闰年
	 * @param  year 年
	 */
	private static boolean isleap(int year){
		if(year%4 == 0 && year%100!=0 || year%400 == 0){//是闰年
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 获取某个月多少天
	 * @param  year 年
	 * @param  mouth 月
	 */
	public static int  getMouthDays(int year,int mouth){
		int days=31;
		if (mouth==1 || mouth==3||mouth==5|| mouth==7||mouth==8||mouth==10||mouth==12){
			days=31;
		}else if (mouth==4||mouth==6||mouth==9||mouth==11){
			days=30;
		}else if (mouth==2){
			if (isleap(year)){
				days = 29;
			}else {
				days=28;
			}
		}
		return days;
	}
	public static  String getMouthByChinese(int mouth){
		String s = "一月";
		switch (mouth){
			case 1:
				s = "一月";
				break;
			case 2:
				s = "二月";
				break;
			case 3:
				s = "三月";
				break;
			case 4:
				s = "四月";
				break;
			case 5:
				s = "五月";
				break;
			case 6:
				s = "六月";
				break;
			case 7:
				s = "七月";
				break;
			case 8:
				s = "八月";
				break;
			case 9:
				s = "九月";
				break;
			case 10:
				s = "十月";
				break;
			case 11:
				s = "十一月";
				break;
			case 12:
				s = "十二月";
				break;
		}
		return s;
	}
	 /**
	 * 计算文字的长度，如果文字的长度大于100则不能再输入
	  * @param et_content 编辑框
	  * @param tv_count 计数显示view
	 */
	public static void setEditContent(final EditText et_content, final TextView tv_count) {
		et_content.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override
			public void afterTextChanged(Editable s) {
				String str = s.toString();
				int count = 0;
				count = str.length();
				if (count > 140) {
					deleteSelection(s);
				}else {
					tv_count.setText(count+"/140");
				}
			}
			private void deleteSelection(Editable s) {
				int selection = et_content.getSelectionStart();
				if (selection > 1) {
					s.delete(selection - 1, selection);
				}
			}
		});
	}
}
