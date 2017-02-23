package net.hongzhang.baselibrary.mode;

/**
 * 作者： Administrator
 * 时间： 2016/7/28
 * 名称：系统消息类
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class SystemInformVo {
    /**
     * id
     */
    private int  id;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 时间
     */
    private String time;
    /**
     * 标记位,保存是否被阅读
     */
    private int  flag;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
