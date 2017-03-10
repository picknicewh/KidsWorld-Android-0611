package net.hongzhang.discovery.modle;

import java.util.List;

/**
 * 作者： wanghua
 * 时间： 2017/3/9
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class ResourceVos {
    /**
     * albumManageList : []
     * resourceManageList : []
     * start : 5
     * tsId : ef06b6f312fe4c21b0c599e8c3b51228
     * tag : 我
     * type : 5
     * end : 1
     */
    private int start;
    private String tsId;
    private String tag;
    private String type;
    private int end;
    private List<CompilationVo> albumManageList;
    private List<ConsultInfoVo> resourceManageList;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public String getTsId() {
        return tsId;
    }

    public void setTsId(String tsId) {
        this.tsId = tsId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public List<CompilationVo> getAlbumManageList() {
        return albumManageList;
    }

    public void setAlbumManageList(List<CompilationVo> albumManageList) {
        this.albumManageList = albumManageList;
    }

    public List<ConsultInfoVo> getResourceManageList() {
        return resourceManageList;
    }

    public void setResourceManageList(List<ConsultInfoVo> resourceManageList) {
        this.resourceManageList = resourceManageList;
    }
}
