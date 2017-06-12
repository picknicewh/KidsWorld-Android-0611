package net.hongzhang.school.bean;

/**
 * 作者： wanghua
 * 时间： 2017/6/7
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class MyMedicineVo {
    /**
     * 喂药时间
     */
    private String MEDICINE_TIME;
    /**
     * 角色名称
     */
    private String TS_NAME;
    /**
     * 药品编码
     */
    private String DRIG_TYPE;
    /**
     * 患病类型编码
     */
    private String SICKEN_TYPE;
    /**
     * 用药数量
     */
    private String MEDICINE_NUMBER;
    /**
     * 备注
     */
    private String REMARK;
    /**
     * 药品名称
     */
    private String DRUG_NAME;
    /**
     * 患病类型名称
     */
    private String SICKEN_NAME;

    public String getMEDICINE_TIME() {
        return MEDICINE_TIME;
    }

    public void setMEDICINE_TIME(String MEDICINE_TIME) {
        this.MEDICINE_TIME = MEDICINE_TIME;
    }

    public String getTS_NAME() {
        return TS_NAME;
    }

    public void setTS_NAME(String TS_NAME) {
        this.TS_NAME = TS_NAME;
    }

    public String getDRIG_TYPE() {
        return DRIG_TYPE;
    }

    public void setDRIG_TYPE(String DRIG_TYPE) {
        this.DRIG_TYPE = DRIG_TYPE;
    }

    public String getSICKEN_TYPE() {
        return SICKEN_TYPE;
    }

    public void setSICKEN_TYPE(String SICKEN_TYPE) {
        this.SICKEN_TYPE = SICKEN_TYPE;
    }

    public String getMEDICINE_NUMBER() {
        return MEDICINE_NUMBER;
    }

    public void setMEDICINE_NUMBER(String MEDICINE_NUMBER) {
        this.MEDICINE_NUMBER = MEDICINE_NUMBER;
    }

    public String getREMARK() {
        return REMARK;
    }

    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }

    public String getDRUG_NAME() {
        return DRUG_NAME;
    }

    public void setDRIG_NAME(String DRUG_NAME) {
        this.DRUG_NAME = DRUG_NAME;
    }

    public String getSICKEN_NAME() {
        return SICKEN_NAME;
    }

    public void setSICKEN_NAME(String SICKEN_NAME) {
        this.SICKEN_NAME = SICKEN_NAME;
    }
}
