package net.hongzhang.school.bean;

import java.util.List;

/**
 * 作者： wanghua
 * 时间： 2017/6/8
 * 名称： 食谱类
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class RecipesVo {
    /**
     * 班级ID
     */
    private String class_id;
    /**
     * 就餐日期	YYYY—MM—DD
     */
    private String dinner_time;
    /**
     * 班级名称
     */
    private String class_name;
    /**
     * 食谱详情
     */
    private List<CBInfo> cb_info_list;

    public static class CBInfo {
        /**
         * 食谱ID
         */
        private String cb_id;
        /**
         * 标题
         */
        private String title;
        /**
         * 就餐时间编码	10001=早餐 10002=早点 10003=午餐 10004=午点 10005=晚餐
         */
        private int type;
        /**
         * 就餐时间名称	如：早餐
         */
        private String type_name;
        /**
         * 图片	未改变图片大小（有横图或竖图的可能），请客户端自己做好处理，如需原图，请去掉”/s”即可。
         */
        private List<String> imgUrl;

        public String getCb_id() {
            return cb_id;
        }

        public void setCb_id(String cb_id) {
            this.cb_id = cb_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getType_name() {
            return type_name;
        }

        public void setType_name(String type_name) {
            this.type_name = type_name;
        }

        public List<String> getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(List<String> imgUrl) {
            this.imgUrl = imgUrl;
        }
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getDinner_time() {
        return dinner_time;
    }

    public void setDinner_time(String dinner_time) {
        this.dinner_time = dinner_time;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public List<CBInfo> getCb_info_list() {
        return cb_info_list;
    }

    public void setCb_info_list(List<CBInfo> cb_info_list) {
        this.cb_info_list = cb_info_list;
    }
}
