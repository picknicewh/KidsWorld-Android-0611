package net.hongzhang.user.mode;

/**
 * 作者： wh
 * 时间： 2016/12/1
 * 名称：专辑类
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class CompilationsVo {
    /**
     * 专辑ID
     */
   private  String albumId;
    /**
     * 专辑名称
     */
    private String  name;
    /**
     * 该专辑资源数量
     */
    private String size	;
    /**
     * 上次观看集数
     */
    private String  currentProgress	;
    /**
     *是否更新完毕   1完毕，2持续更新中
     */
    private int isEnd;
    /**
     * 专辑图片
     */
    private String imageUrl;
    /**
     * 最后一次观看时间
     */
    private String  createTime	;
    /**
     *简介
     */
    private  String brief	;
    /**
     * 收藏数
     */
    private int favorites;
    /**
     * 	是否已收藏  1已收藏，2=未收藏
     */
    private  int isFavorites	;
    /**
     * 在上次观看后是否有新的更新   1有，2没有
     */
    private  int isUpdate;
    /**
     * 搜索数
     */
    private int searchCount;

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    private   String albumName;
    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public int getSearchCount() {
        return searchCount;
    }

    public void setSearchCount(int searchCount) {
        this.searchCount = searchCount;
    }

    public int getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(int isUpdate) {
        this.isUpdate = isUpdate;
    }

    public int getIsFavorites() {
        return isFavorites;
    }

    public void setIsFavorites(int isFavorites) {
        this.isFavorites = isFavorites;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getIsEnd() {
        return isEnd;
    }

    public void setIsEnd(int isEnd) {
        this.isEnd = isEnd;
    }

    public String getCurrentProgress() {
        return currentProgress;
    }

    public void setCurrentProgress(String currentProgress) {
        this.currentProgress = currentProgress;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
