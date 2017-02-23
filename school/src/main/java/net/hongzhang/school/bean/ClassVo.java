package net.hongzhang.school.bean;

/**
 * 作者： wh
 * 时间： 2016/10/26
 * 名称：该角色所有班级类
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class ClassVo {
    /**
     * 班级ID
     */
    private String class_id	;
    /**
     * 	班级名称
     */
    private String class_name;

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }
}
