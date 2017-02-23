package net.hongzhang.school.bean;

/**
 * 作者： wh
 * 时间： 2016/11/1
 * 名称：服药时间列表
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class MedicineStatus {


    /**
     * 喂药委托ID
     */
    private String medicine_id	;
    /**
     *服药日期
     */
    private String create_time	;
    /**
     *知晓人ID
     */
    private String know_id	;
    /**
     *知晓人融云通讯ID
     */
    private String know_ry_id	;
    /**
     *知晓人头像
     */
    private String know_url	;
    /**
     *完成人ID
     */
    private String finish_id	;
    /**
     *完成人融云通讯ID
     */
    private String finish_ry_id	;
    /**
     *完成人头像
     */
   private String finish_url	;

    public String getMedicine_id() {
        return medicine_id;
    }

    public void setMedicine_id(String medicine_id) {
        this.medicine_id = medicine_id;
    }

    public String getFinish_url() {
        return finish_url;
    }

    public void setFinish_url(String finish_url) {
        this.finish_url = finish_url;
    }

    public String getFinish_ry_id() {
        return finish_ry_id;
    }

    public void setFinish_ry_id(String finish_ry_id) {
        this.finish_ry_id = finish_ry_id;
    }

    public String getFinish_id() {
        return finish_id;
    }

    public void setFinish_id(String finish_id) {
        this.finish_id = finish_id;
    }

    public String getKnow_url() {
        return know_url;
    }

    public void setKnow_url(String know_url) {
        this.know_url = know_url;
    }

    public String getKnow_ry_id() {
        return know_ry_id;
    }

    public void setKnow_ry_id(String know_ry_id) {
        this.know_ry_id = know_ry_id;
    }

    public String getKnow_id() {
        return know_id;
    }

    public void setKnow_id(String know_id) {
        this.know_id = know_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
