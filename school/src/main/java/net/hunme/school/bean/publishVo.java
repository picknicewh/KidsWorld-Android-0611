package net.hunme.school.bean;

import java.io.Serializable;

/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/8/1
 * 描    述：通知实体类
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class PublishVo implements Serializable{
    private String tsName;
    private String imgUrl;
    private String message;
    private String dateTime;
    private String messageId;

    private boolean isRead;

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getTsName() {
        return tsName;
    }

    public void setTsName(String tsName) {
        this.tsName = tsName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
