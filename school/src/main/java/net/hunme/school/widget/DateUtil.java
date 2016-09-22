package net.hunme.school.widget;

/**
 * 作者： wh
 * 时间： 2016/9/20
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class DateUtil  {
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
}
