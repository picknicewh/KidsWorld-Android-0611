package net.hongzhang.school.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者： wh
 * 时间： 2016/11/1
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class Schedule implements Parcelable{
    /**
     * 喂药已完成
     */
    private String finish;
    /**
     * 知晓人融云通讯ID
     */
    private String knowRyId;
    /**
     * 已发布喂药信息
     */
    private String issue;
    /**
     * 完成人融云通讯ID
     */
    private String finishRyId;
    /**
     * 发布人ID
     */
    private String issueId;
    /**
     * 完成人头像
     */
    private String finishUrl;
    /**
     * 知晓人头像
     */
    private String knowUrl;
    /**
     * 发布人头像
     */
    private String issueUrl;
    /**
     * 完成人ID
     */
    private String finishId;
    /**
     * 已知晓
     */
    private String know;
    /**
     * 发布人融云通讯ID
     */
    private String issueRyId;
    /**
     * 知晓人ID
     */
    private String knowId;

    protected Schedule(Parcel in) {
        finish = in.readString();
        knowRyId = in.readString();
        issue = in.readString();
        finishRyId = in.readString();
        issueId = in.readString();
        finishUrl = in.readString();
        knowUrl = in.readString();
        issueUrl = in.readString();
        finishId = in.readString();
        know = in.readString();
        issueRyId = in.readString();
        knowId = in.readString();
    }

    public static final Creator<Schedule> CREATOR = new Creator<Schedule>() {
        @Override
        public Schedule createFromParcel(Parcel in) {
            return new Schedule(in);
        }

        @Override
        public Schedule[] newArray(int size) {
            return new Schedule[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(finish);
        parcel.writeString(knowRyId);
        parcel.writeString(issue);
        parcel.writeString(finishRyId);
        parcel.writeString(issueId);
        parcel.writeString(finishUrl);
        parcel.writeString(knowUrl);
        parcel.writeString(issueUrl);
        parcel.writeString(finishId);
        parcel.writeString(know);
        parcel.writeString(issueRyId);
        parcel.writeString(knowId);
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

    public String getKnowId() {
        return knowId;
    }

    public void setKnowId(String knowId) {
        this.knowId = knowId;
    }

    public String getIssueRyId() {
        return issueRyId;
    }

    public void setIssueRyId(String issueRyId) {
        this.issueRyId = issueRyId;
    }

    public String getKnow() {
        return know;
    }

    public void setKnow(String know) {
        this.know = know;
    }

    public String getFinishId() {
        return finishId;
    }

    public void setFinishId(String finishId) {
        this.finishId = finishId;
    }

    public String getIssueUrl() {
        return issueUrl;
    }

    public void setIssueUrl(String issueUrl) {
        this.issueUrl = issueUrl;
    }

    public String getKnowUrl() {
        return knowUrl;
    }

    public void setKnowUrl(String knowUrl) {
        this.knowUrl = knowUrl;
    }

    public String getFinishUrl() {
        return finishUrl;
    }

    public void setFinishUrl(String finishUrl) {
        this.finishUrl = finishUrl;
    }

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public String getFinishRyId() {
        return finishRyId;
    }

    public void setFinishRyId(String finishRyId) {
        this.finishRyId = finishRyId;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getKnowRyId() {
        return knowRyId;
    }

    public void setKnowRyId(String knowRyId) {
        this.knowRyId = knowRyId;
    }
}


