package net.hongzhang.user.mode;

import java.util.List;

/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/9/9
 * 描    述：
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class StatusDetilsVo {
    private String date;
    private String tsName;
    private String img;
    private String tsType;
    private int isAgree;
    private String createTime;
    private String tsId;
    private String dynamicId;
    private int dynamicType;
    private String videoUrl;
    private String text;
    private int isCollect;
    private List<String> imgUrl;
    /**
     * date : null
     * dynamic_id : 02b4831fb81240558640c3df09201dad
     * ts_id : 0a9004bebb0748bd9a9ef6e0736ebc2b
     * create_time : null
     * tsName : 匡军红
     * rew_id : b9a1784346704da588e77a4292b851f5
     * rew_content : ceshiceshiceshi
     * rew_ts_id : null
     * rew_type : 1
     * tsImgurl : null
     * rewTsName : 匡军红
     * rewTsImgurl : null
     * createDate : null
     */

    private List<DynamidRewListBean> dynamidRewList;
    private List<String> list;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTsName() {
        return tsName;
    }

    public void setTsName(String tsName) {
        this.tsName = tsName;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTsType() {
        return tsType;
    }

    public void setTsType(String tsType) {
        this.tsType = tsType;
    }

    public int getIsAgree() {
        return isAgree;
    }

    public void setIsAgree(int isAgree) {
        this.isAgree = isAgree;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getTsId() {
        return tsId;
    }

    public void setTsId(String tsId) {
        this.tsId = tsId;
    }

    public String getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(String dynamicId) {
        this.dynamicId = dynamicId;
    }

    public int getDynamicType() {
        return dynamicType;
    }

    public void setDynamicType(int dynamicType) {
        this.dynamicType = dynamicType;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(int isCollect) {
        this.isCollect = isCollect;
    }

    public List<String> getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(List<String> imgUrl) {
        this.imgUrl = imgUrl;
    }

    public List<DynamidRewListBean> getDynamidRewList() {
        return dynamidRewList;
    }

    public void setDynamidRewList(List<DynamidRewListBean> dynamidRewList) {
        this.dynamidRewList = dynamidRewList;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public static class DynamidRewListBean {
        private String date;
        private String dynamic_id;
        private String ts_id;
        private String create_time;
        private String tsName;
        private String rew_id;
        private String rew_content;
        private String rew_ts_id;
        private String rew_type;
        private String tsImgurl;
        private String rewTsName;
        private String rewTsImgurl;
        private String createDate;
        private String tsType;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDynamic_id() {
            return dynamic_id;
        }

        public void setDynamic_id(String dynamic_id) {
            this.dynamic_id = dynamic_id;
        }

        public String getTs_id() {
            return ts_id;
        }

        public void setTs_id(String ts_id) {
            this.ts_id = ts_id;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getTsName() {
            return tsName;
        }

        public void setTsName(String tsName) {
            this.tsName = tsName;
        }

        public String getRew_id() {
            return rew_id;
        }

        public void setRew_id(String rew_id) {
            this.rew_id = rew_id;
        }

        public String getRew_content() {
            return rew_content;
        }

        public void setRew_content(String rew_content) {
            this.rew_content = rew_content;
        }

        public String getRew_ts_id() {
            return rew_ts_id;
        }

        public void setRew_ts_id(String rew_ts_id) {
            this.rew_ts_id = rew_ts_id;
        }

        public String getRew_type() {
            return rew_type;
        }

        public void setRew_type(String rew_type) {
            this.rew_type = rew_type;
        }

        public String getTsImgurl() {
            return tsImgurl;
        }

        public void setTsImgurl(String tsImgurl) {
            this.tsImgurl = tsImgurl;
        }

        public String getRewTsName() {
            return rewTsName;
        }

        public void setRewTsName(String rewTsName) {
            this.rewTsName = rewTsName;
        }

        public String getRewTsImgurl() {
            return rewTsImgurl;
        }

        public void setRewTsImgurl(String rewTsImgurl) {
            this.rewTsImgurl = rewTsImgurl;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getTsType() {
            return tsType;
        }

        public void setTsType(String tsType) {
            this.tsType = tsType;
        }
    }
}
