package net.hongzhang.discovery.modle;

/**
 * 作者： Administrator
 * 时间： 2016/11/10
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class ThemeInfoVo {

    private int type;
    private int pvcount;
    /**
     *主题ID
     */
   private String  id	;
    /**
     *名称
     */
   private String  name	;
    /**
     *	封面url
     */
    private  String imgUrl;
    /**
     *	状态（0显示 1隐藏）
     */
   private int    status;
    /**
     *排序号
     */
    private  int ordernumber;
    /**
     *分类id
     */
    private int restypeid;
    /**
     *描述备注
     */
    private String description;
    /**
     *添加时间（上传时间）
     */
   private String createTime;
    /**
     *是否推荐(0推荐，1不推荐)
     */
    private int isrecommend	;
    public String  getId() {
        return id;
    }

    public void setId(int String) {
        this.id = id;
    }

    public int getIsrecommend() {
        return isrecommend;
    }

    public void setIsrecommend(int isrecommend) {
        this.isrecommend = isrecommend;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getOrdernumber() {
        return ordernumber;
    }

    public void setOrdernumber(int ordernumber) {
        this.ordernumber = ordernumber;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getRestypeid() {
        return restypeid;
    }

    public void setRestypeid(int restypeid) {
        this.restypeid = restypeid;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPvcount() {
        return pvcount;
    }

    public void setPvcount(int pvcount) {
        this.pvcount = pvcount;
    }

}
