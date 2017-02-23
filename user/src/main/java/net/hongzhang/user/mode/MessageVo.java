package net.hongzhang.user.mode;

/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/7/26
 * 描    述：系统消息实体类
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class MessageVo {
    private String title;
    private String date;
    private String content;
    private boolean isRead;
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
