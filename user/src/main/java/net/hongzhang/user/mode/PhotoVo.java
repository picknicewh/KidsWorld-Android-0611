package net.hongzhang.user.mode;

import java.io.Serializable;

/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/7/18
 * 描    述：我的相册实体类
 * 版    本：1.0
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class PhotoVo implements Serializable{
    private String id;       //相册ID
    private String ts_id;     //角色ID
    private String name;        //相册名称
    private String createtime;  //创建时间
    private String url;       //首张图片地址
    private String count;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTs_id() {
        return ts_id;
    }

    public void setTs_id(String ts_id) {
        this.ts_id = ts_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
