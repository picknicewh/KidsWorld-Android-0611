package net.hongzhang.school.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 作者： wanghua
 * 时间： 2017/5/3
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class ActivityInfoVo implements Parcelable {
    /**
     * activityId : tr4gh86fd41gr8eg4ds54ads8
     * appraise : null
     * content : null
     * deadline : 1493195400000
     * postTime : 1493195400000
     * title : 这是一个标题
     * number : 0
     * dimensionalityName : ["思维方式","完成情况","能力提升"]
     * postName : 杜耀天
     * appraised : null
     * upload : null
     */
    private String activityId;
    private int appraise;
    private String content;
    private long deadline;
    private long postTime;
    private String title;
    private int number;
    private String  postName;
    private int appraised;
    private int upload;
    private List<String> dimensionalityName;
    private List<String> imgUrls;
    private String  appraiseId;
    private String activityWorksId;
    private int studentNumber;


    protected ActivityInfoVo(Parcel in) {
        activityId = in.readString();
        appraise = in.readInt();
        content = in.readString();
        deadline = in.readLong();
        postTime = in.readLong();
        title = in.readString();
        number = in.readInt();
        postName = in.readString();
        appraised = in.readInt();
        upload = in.readInt();
        dimensionalityName = in.createStringArrayList();
        imgUrls = in.createStringArrayList();
        appraiseId = in.readString();
        activityWorksId = in.readString();
        studentNumber = in.readInt();
    }

    public static final Creator<ActivityInfoVo> CREATOR = new Creator<ActivityInfoVo>() {
        @Override
        public ActivityInfoVo createFromParcel(Parcel in) {
            return new ActivityInfoVo(in);
        }

        @Override
        public ActivityInfoVo[] newArray(int size) {
            return new ActivityInfoVo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(activityId);
        parcel.writeInt(appraise);
        parcel.writeString(content);
        parcel.writeLong(deadline);
        parcel.writeLong(postTime);
        parcel.writeString(title);
        parcel.writeInt(number);
        parcel.writeString(postName);
        parcel.writeInt(appraised);
        parcel.writeInt(upload);
        parcel.writeStringList(dimensionalityName);
        parcel.writeStringList(imgUrls);
        parcel.writeString(appraiseId);
        parcel.writeString(activityWorksId);
        parcel.writeInt(studentNumber);
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public int getAppraise() {
        return appraise;
    }

    public void setAppraise(int appraise) {
        this.appraise = appraise;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getDeadline() {
        return deadline;
    }

    public void setDeadline(long deadline) {
        this.deadline = deadline;
    }

    public long getPostTime() {
        return postTime;
    }

    public void setPostTime(long postTime) {
        this.postTime = postTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public int getAppraised() {
        return appraised;
    }

    public void setAppraised(int appraised) {
        this.appraised = appraised;
    }

    public int getUpload() {
        return upload;
    }

    public void setUpload(int upload) {
        this.upload = upload;
    }

    public List<String> getDimensionalityName() {
        return dimensionalityName;
    }

    public void setDimensionalityName(List<String> dimensionalityName) {
        this.dimensionalityName = dimensionalityName;
    }

    public List<String> getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(List<String> imgUrls) {
        this.imgUrls = imgUrls;
    }

    public String getAppraiseId() {
        return appraiseId;
    }

    public void setAppraiseId(String appraiseId) {
        this.appraiseId = appraiseId;
    }
    public String getActivityWorksId() {
        return activityWorksId;
    }

    public void setActivityWorksId(String activityWorksId) {
        this.activityWorksId = activityWorksId;
    }
    public int getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(int studentNumber) {
        this.studentNumber = studentNumber;
    }

}
