package net.hunme.user.mode;

/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/7/26
 * 描    述：
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class CheckUpadteVo {
    private String versions;
    private String id;
    private String system;
    private String updateurl;
    private String createtime;
    public String getVersions() {
        return versions;
    }

    public void setVersions(String versions) {
        this.versions = versions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getUpdateurl() {
        return updateurl;
    }

    public void setUpdateurl(String updateurl) {
        this.updateurl = updateurl;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }
}
