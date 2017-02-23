package net.hongzhang.school.bean;

/**
 * 作者： wh
 * 时间： 2016/9/19
 * 名称：请假类
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class LeaveVo {


    /**
     * 角色ID
     */
    private String tsId	;
    /**
     *结束时间
     */
    private String  endDate	;
    /**
     *  开始时间
     */
    private String startDate;
    /**
     *  用餐状况
     1=早餐，2=中餐，3=晚餐
     多选时，用英文逗号分隔
     */
    private String diningStatus;
    /**
     *	事由
     */
    private String cause;
    /**
     *     角色名称
     */
     private String tsName;
    /**
     *  申请时间
     */
    private String creationTime	;
    /**
     * 请假状态
     2=已读，1=未读
     */
    private int status	;
    /**
     请假ID
     */
    private String vacation_id	;
    public String getTsId() {
        return tsId;
    }

    public void setTsId(String tsId) {
        this.tsId = tsId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getVacation_id() {
        return vacation_id;
    }

    public void setVacation_id(String vacation_id) {
        this.vacation_id = vacation_id;
    }

    public String getTsName() {
        return tsName;
    }

    public void setTsName(String tsName) {
        this.tsName = tsName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDiningStatus() {
        return diningStatus;
    }

    public void setDiningStatus(String diningStatus) {
        this.diningStatus = diningStatus;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

}
