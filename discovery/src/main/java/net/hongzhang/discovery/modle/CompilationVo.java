package net.hongzhang.discovery.modle;

/**
 * 作者： wh
 * 时间： 2016/11/30
 * 名称：专辑
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class CompilationVo {
   /*

            "brief":"Muffin Songs美国家喻户晓的童谣启蒙早教，国内很多英语启蒙早教都引用了其中的部分经典歌谣，比如清华的语感启蒙中绝大部分都是Muffin Songs中的童谣。


           ,"currentProgress":"1","albumId":"119",
            "albumName":"Muffin Songs A-D",
            "imageUrl":"http://zhu.hunme.net:8080/resource/albumManage/Image/英语专栏/Muffin Songs A-D.jpg",
            "createTime":null,"pvcount":201
    */
    /**
     * 该专辑资源数量
     */
    private String size;
    /**
     * 是否更新完毕  1完毕，2持续更新中
     */
    private int isEnd;
    /**
     * 简介
     */
    private String brief;
    /**
     * 收藏数
     */
    private int favorites;
    /**
     * 在上次观看后是否有新的更新    1有，2没有
     */
    private int isUpdate;
    /**
     * 是否主题还是专题 1主题2专题
     */
    private int isTheme;
    /**
     * 搜索数
     */
    private int searchCount;
    /**
     * 是否已收藏    1已收藏，2=未收藏
     */
    private int isFavorites;
    /**
     * 上次观看集数
     */
    private String currentProgress;
    /**
     * 专辑ID
     */
    private String albumId;
    /**
     * 专辑名称
     */
    private String albumName;

    /**
     * 专辑图片
     */
    private String imageUrl;
    /**
     * 最后一次观看时间
     */
    private String createTime;

    /**
     * 播放次数
     */

    private int pvcount;


    public int getPvcount() {
        return pvcount;
    }

    public void setPvcount(int pvcount) {
        this.pvcount = pvcount;
    }


    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }


    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getCurrentProgress() {
        return currentProgress;
    }

    public void setCurrentProgress(String currentProgress) {
        this.currentProgress = currentProgress;
    }
    public int getIsTheme() {
        return isTheme;
    }

    public void setIsTheme(int isTheme) {
        this.isTheme = isTheme;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public int getIsEnd() {
        return isEnd;
    }

    public void setIsEnd(int isEnd) {
        this.isEnd = isEnd;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getFavorites() {
        return favorites;
    }

    public void setFavorites(int favorites) {
        this.favorites = favorites;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public int getIsFavorites() {
        return isFavorites;
    }

    public void setIsFavorites(int isFavorites) {
        this.isFavorites = isFavorites;
    }

    public int getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(int isUpdate) {
        this.isUpdate = isUpdate;
    }

    public int getSearchCount() {
        return searchCount;
    }

    public void setSearchCount(int searchCount) {
        this.searchCount = searchCount;
    }
}
