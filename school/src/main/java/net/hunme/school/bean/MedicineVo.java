package net.hunme.school.bean;

import java.util.List;

/**
 * 作者： wh
 * 时间： 2016/10/31
 * 名称：喂药lei
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class MedicineVo{
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

   /*  public static final String KEY_TS_ID = "ts_id";
    public static final String KEY_TS_NAME = "ts_name";
    public static final String KEY_IMGURL = "imgUrl";
    public static final String KEY_RY_ID = "ry_id";
    public static final String KEY_MEDICINE_ID = "medicine_id";
    public static final String KEY_MEDICINE_NAME = "medicine_name";
    public static final String KEY_MEDICINE_DOSAFE = "medicine_dosage";
    public static final String KEY_MEDICINE_DOC = "medicine_doc";
    public static final String KEY_MEAL = "meal_before_or_after";
    public static final String KEY_CREATE_TIME = "create_time";

      /*  @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TS_ID,ts_id);
        bundle.putString(KEY_TS_NAME, ts_name);
        bundle.putString(KEY_IMGURL, imgUrl);
        bundle.putString(KEY_RY_ID, ry_id);
        bundle.putString(KEY_MEDICINE_ID, medicine_id);
        bundle.putString(KEY_MEDICINE_NAME, medicine_name);
        bundle.putString(KEY_MEDICINE_DOSAFE, medicine_dosage);
        bundle.putString(KEY_MEDICINE_DOC, medicine_doc);
        bundle.putInt(KEY_MEAL, meal_before_or_after);
        bundle.putString(KEY_CREATE_TIME, create_time);
        dest.writeBundle(bundle);
    }
    public static final Parcelable.Creator<MedicineVo> CREATOR = new Parcelable.Creator<MedicineVo>() {

        @Override
        public MedicineVo createFromParcel(Parcel source) {
            MedicineVo medicineVo = new MedicineVo();
            Bundle bundle = new Bundle();
            bundle = source.readBundle();
            medicineVo.ts_id = bundle.getString(KEY_TS_ID);
            medicineVo.ts_name = bundle.getString(KEY_TS_NAME);
            medicineVo.imgUrl = bundle.getString(KEY_IMGURL);
            medicineVo.ry_id = bundle.getString(KEY_RY_ID);
            medicineVo.medicine_id = bundle.getString(KEY_MEDICINE_ID);
            medicineVo.medicine_name = bundle.getString(KEY_MEDICINE_NAME);
            medicineVo.medicine_dosage = bundle.getString(KEY_MEDICINE_DOSAFE);
            medicineVo.medicine_doc = bundle.getString(KEY_MEDICINE_DOC);
            medicineVo.meal_before_or_after = bundle.getInt(KEY_MEAL);
            medicineVo.create_time = bundle.getString(KEY_CREATE_TIME);
            return medicineVo;
        }

        @Override
        public MedicineVo[] newArray(int size) {
            return new MedicineVo[size];
        }
    };*/
}
