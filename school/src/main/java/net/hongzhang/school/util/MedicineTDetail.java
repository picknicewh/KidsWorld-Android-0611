package net.hongzhang.school.util;

/**
 * 作者： wh
 * 时间： 2016/10/31
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class MedicineTDetail{
    public interface  View{
        void setHeadImageView(String url);
        void setTsName(String name);
        void setFeedDate(String date);
        void setMedicineName(String medicineName);
        void setMedicineDosage(String medicineDosage);
        void setLaunchTime(int LaunchTime);
        void setRemark(String remark);
        void setIsFeed(int feed);
    }
   public interface Presenter{
        void getMedicineDetils(String tsId,String medicineId);
        void finishFeedMedicine(String tsId,String medicineId);
    }
}
