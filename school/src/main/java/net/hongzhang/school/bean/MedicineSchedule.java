package net.hongzhang.school.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者： wh
 * 时间： 2016/11/1
 * 名称：今日喂药流程反馈
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class MedicineSchedule implements Parcelable {
    /**
     * 喂药详情
     */
     private MedicineVo medicine;
    /**
     *今日喂药流程
     */
    private Schedule schedule	;

    protected MedicineSchedule(Parcel in) {
        medicine = in.readParcelable(MedicineVo.class.getClassLoader());
        schedule = in.readParcelable(Schedule.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(medicine, flags);
        dest.writeParcelable(schedule, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MedicineSchedule> CREATOR = new Creator<MedicineSchedule>() {
        @Override
        public MedicineSchedule createFromParcel(Parcel in) {
            return new MedicineSchedule(in);
        }

        @Override
        public MedicineSchedule[] newArray(int size) {
            return new MedicineSchedule[size];
        }
    };

    public  MedicineVo getMedicine() {
        return medicine;
    }

    public void setMedicine( MedicineVo medicine) {
        this.medicine = medicine;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule  schedule) {
        this.schedule = schedule;
    }

}
