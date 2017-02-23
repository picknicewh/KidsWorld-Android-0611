package net.hongzhang.school.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 作者： wh
 * 时间： 2016/10/31
 * 名称：喂药lei
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class MedicineVo implements Parcelable {

    /**
     * 角色ID
     */
    private String	 ts_id;
    /**
     *角色名称
     */
    private String ts_name;
    /**
     *角色头像
     */
    private  String	imgUrl;
    /**
     *融云通讯ID
     */
    private String ry_id;
    /**
     *喂药委托ID
     */
    private String medicine_id	;
    /**
     *药品名称
     */
    private String	 medicine_name;
    /**
     *药品用量
     */
    private String	 medicine_dosage;
    /**
     *说明
     */
    private String medicine_doc	;
    /**
     *	Integer	1=餐前 ，2=餐后
     */
    int meal_before_or_after;
    /**
     *委托时间（非吃药时间）
     */
    private String create_time;
    /**
     *	服药时间列表
     */
    private List<MedicineStatus> medicineStatusList;
    protected MedicineVo(Parcel in) {
        ts_id = in.readString();
        ts_name = in.readString();
        imgUrl = in.readString();
        ry_id = in.readString();
        medicine_id = in.readString();
        medicine_name = in.readString();
        medicine_dosage = in.readString();
        medicine_doc = in.readString();
        meal_before_or_after = in.readInt();
        create_time = in.readString();
    }
    public static final Creator<MedicineVo> CREATOR = new Creator<MedicineVo>() {
        @Override
        public MedicineVo createFromParcel(Parcel in) {
            return new MedicineVo(in);
        }

        @Override
        public MedicineVo[] newArray(int size) {
            return new MedicineVo[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(ts_id);
        parcel.writeString(ts_name);
        parcel.writeString(imgUrl);
        parcel.writeString(ry_id);
        parcel.writeString(medicine_id);
        parcel.writeString(medicine_name);
        parcel.writeString(medicine_dosage);
        parcel.writeString(medicine_doc);
        parcel.writeInt(meal_before_or_after);
        parcel.writeString(create_time);
    }
    public String getTs_id() {
        return ts_id;
    }

    public void setTs_id(String ts_id) {
        this.ts_id = ts_id;
    }

    public List<MedicineStatus> getMedicineStatusList() {
        return medicineStatusList;
    }

    public void setMedicineStatusList(List<MedicineStatus> medicineStatusList) {
        this.medicineStatusList = medicineStatusList;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getMeal_before_or_after() {
        return meal_before_or_after;
    }

    public void setMeal_before_or_after(int meal_before_or_after) {
        this.meal_before_or_after = meal_before_or_after;
    }

    public String getMedicine_doc() {
        return medicine_doc;
    }

    public void setMedicine_doc(String medicine_doc) {
        this.medicine_doc = medicine_doc;
    }

    public String getMedicine_dosage() {
        return medicine_dosage;
    }

    public void setMedicine_dosage(String medicine_dosage) {
        this.medicine_dosage = medicine_dosage;
    }

    public String getMedicine_name() {
        return medicine_name;
    }

    public void setMedicine_name(String medicine_name) {
        this.medicine_name = medicine_name;
    }

    public String getMedicine_id() {
        return medicine_id;
    }

    public void setMedicine_id(String medicine_id) {
        this.medicine_id = medicine_id;
    }

    public String getRy_id() {
        return ry_id;
    }

    public void setRy_id(String ry_id) {
        this.ry_id = ry_id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTs_name() {
        return ts_name;
    }

    public void setTs_name(String ts_name) {
        this.ts_name = ts_name;
    }
}
