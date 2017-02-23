package net.hongzhang.discovery.modle;

/**
 * 作者： wh
 * 时间： 2016/11/10
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class SongInfoVo {
    /**
     * 资源ID
     */
    private int id	;
    /**
     *类型（0视频，1音频，2资讯）
     */
   private int   type;
    /**
     *名称
     */
   private String name;
    /**
     *封面url
     */
    private String imgUrl;
    /**
     *资源url（音频、视频、资讯）
     */
    private String resourceUrl;
    /**
     *状态（0显示 1隐藏）
     */
   private int status	;
    /**
     *	排序号
     */
    private int  ordernumber	;
    /**
     *主题id（视频、音频）
     */
   private int  themeid	;
    /**
     *描述备注
     */
    private String  description		;
    /**
     *添加时间（上传时间）
     */
    private String create;
    /**
     *	是否推荐(0推荐，1不推荐)
     */
    private int	 isrecommend;
    /**
     *点击率(播放次数)
     */
    private int  pv;
    /**
     *字幕url（视频，音频）
     */
   private String subtitlesurl;
    /**
     *	分类id（资讯只有分类id）
     */
   private int typeId	;
    /**
     *内容（资讯、歌词）
     */
    private String   content;
    /**
     *资源编码（视频、音频）
     */
    private String  code;
    /**
     *出处（视频、音频、资讯）
     */
    private String source;
    /**
     *上传人
     */
    private String uploaduser;
    /**
     *上传时间
     */
    private String uploaddate;
    /**
     *	评论数
     */
    private  int commentcount;
    /**
     *审核状态
     */
   private int infostatus	;
    /**
     *收藏id，不为空就是收藏过
     */
   private int  attentionid	;
    /**
     *点赞id，不为空就是点赞过
     */
    private  int   praiseid	;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getOrdernumber() {
        return ordernumber;
    }

    public void setOrdernumber(int ordernumber) {
        this.ordernumber = ordernumber;
    }

    public int getPv() {
        return pv;
    }

    public void setPv(int pv) {
        this.pv = pv;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUploaduser() {
        return uploaduser;
    }

    public void setUploaduser(String uploaduser) {
        this.uploaduser = uploaduser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getSubtitlesurl() {
        return subtitlesurl;
    }

    public void setSubtitlesurl(String subtitlesurl) {
        this.subtitlesurl = subtitlesurl;
    }

    public int getIsrecommend() {
        return isrecommend;
    }

    public void setIsrecommend(int isrecommend) {
        this.isrecommend = isrecommend;
    }

    public String getCreate() {
        return create;
    }

    public void setCreate(String create) {
        this.create = create;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getThemeid() {
        return themeid;
    }

    public void setThemeid(int themeid) {
        this.themeid = themeid;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUploaddate() {
        return uploaddate;
    }

    public void setUploaddate(String uploaddate) {
        this.uploaddate = uploaddate;
    }

    public int getCommentcount() {
        return commentcount;
    }

    public void setCommentcount(int commentcount) {
        this.commentcount = commentcount;
    }

    public int getInfostatus() {
        return infostatus;
    }

    public void setInfostatus(int infostatus) {
        this.infostatus = infostatus;
    }

    public int getAttentionid() {
        return attentionid;
    }

    public void setAttentionid(int attentionid) {
        this.attentionid = attentionid;
    }

    public int getPraiseid() {
        return praiseid;
    }

    public void setPraiseid(int praiseid) {
        this.praiseid = praiseid;
    }
}
