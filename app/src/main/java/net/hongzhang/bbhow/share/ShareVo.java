package net.hongzhang.bbhow.share;

import java.util.List;

/**
 * 作者： wanghua
 * 时间： 2017/3/27
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class ShareVo {
    private String type;
    private String title;
    private String text;
    private List<String> paths;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getPaths() {
        return paths;
    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
    }
}
