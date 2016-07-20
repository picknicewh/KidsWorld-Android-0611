package net.hunme.login.mode;

import java.util.List;

/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/7/20
 * 描    述：用户信息实体类
 * 版    本：1.0
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class CharacterSeleteVo{
    private List<characterSelete>jsonList;

    public List<characterSelete> getJsonList() {
        return jsonList;
    }

    public void setJsonList(List<characterSelete> jsonList) {
        this.jsonList = jsonList;
    }

    class characterSelete {
        private String tsId;   //角色ID
        private String img;    //用户头像URL
        private String name;   //用户名称
        private String schoolName;  //学校名称
        private String className;   //班级名称 当type=0时，此字段为空
        private String type;       //1=学生，0=老师
        private String ryId;      // 融云通讯ID（token）

        public String getTsId() {
            return tsId;
        }

        public void setTsId(String tsId) {
            this.tsId = tsId;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSchoolName() {
            return schoolName;
        }

        public void setSchoolName(String schoolName) {
            this.schoolName = schoolName;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getRyId() {
            return ryId;
        }

        public void setRyId(String ryId) {
            this.ryId = ryId;
        }
    }

}
