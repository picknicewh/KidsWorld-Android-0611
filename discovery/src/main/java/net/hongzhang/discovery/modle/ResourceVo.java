package net.hongzhang.discovery.modle;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者： wh
 * 时间： 2016/11/30
 * 名称：资源
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class ResourceVo implements Parcelable {

    /**
     * length : null
     * type : 1
     * content : null
     * source : 0
     * description : 蒲公英的果实像许多的降落伞，看它们起飞了。
     * resourceId : e1884863d0c649cc83e94b243b0405ff
     * themeName : null
     * labelName : null
     * labelId : null
     * fileSize : null
     * pvcount : 24
     * audit : 1
     * grade : 2
     * sets : 1
     * author : 红掌文化
     * isPraise : 2
     * resourceName : 蒲公英飞啊飞
     * code : V854P46C
     * status : 1
     * albumId : 801ebc70299c424e9765753d5c55350c
     * themeId : null
     * albumName : 蒲公英飞啊飞
     * namejc : null
     * imageUrl : http://eduslb.openhunme.com/resource/resourceManage/Image/鹅鹅鹅红掌美术启蒙第一季/20170216163347742_QQ截图20170216163130.jpg
     * pay : 1
     * updateName : admin
     * updateTime : 1487865600000
     * createName : admin
     * createTime : 1487260800000
     * ordernumber : 50
     * recommended : 1
     * searchCount : 0
     * resourceUrl : http://eduslb.openhunme.com/resource/resourceManage/Video/鹅鹅鹅红掌美术启蒙第一季/20170216163417587_蒲公英飞啊飞.mp4
     * subtitlesUrl : null
     * commentCount : 0
     * broadcastPace : null
     * praiseNumber : null
     * isFavorites : 2
     */
    /**
     * 长度（时分秒形式）
     */
    private int length;
    /**
     * 类型（1：视频，2：音频，3：资讯）
     */
    private int type;
    /**
     * 内容（资讯：文本内容，音频：文本歌词）
     */
    private String content;
    /**
     * 来源（目前不用）
     */
    private int source;
    /**
     * 简介
     */
    private String description;
    /**
     * 资源ID
     */
    private String resourceId;
    /**
     *主题名
     */
    private String themeName;
    /**
     * 标签名字
     */
    private String labelName;
    /**
     * 标签id
     */
    private String labelId;
    /**
     * 文件大小
     */
    private String fileSize;
    /**
     * 总点击数
     */
    private int pvcount;
    /**
     * 审核状态（1：已通过，2：未通过）
     */
    private int audit;
    /**
     * 学段（1：小班，2：中班，3：大班）
     */
    private int grade;
    /**
     * 作者
     */
    private String author;
    /**
     * 1已点赞 2=未点赞
     */
    private int isPraise;
    /**
     * 资源
     */
    private String resourceName;

    /**
     * 资源编码（视频，音频）
     */
    private String code;

    /**
     * 状态（1：显示，2：隐藏）
     */
    private int status;
    /**
     * 专辑id
     */
    private String albumId;
    /**
     * 主题id
     */
    private String themeId;
    /**
     * 专辑名称
     */
    private String albumName;
    private String namejc;
    /**
     * 是否收费（1：收费，2：免费）
     */
    private int pay;
    /**
     * 第几集（1：第一集，2：第二集，.....）
     */
    private int sets;
    /**
     * 更新者
     */
    private String updateName;
    /**
     * 更新时间
     */
    private String updateTime;
    /**
     * 创建者
     */
    private String createName;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 是否推荐（1：推荐，2：不推荐）
     */
    private int recommended;
    /**
     * 搜索次数
     */
    private String searchCount;

    /**
     * 图片地址
     */
    private String imageUrl;
    /**
     * 资源地址
     */
    private String resourceUrl;
    /**
     * 字幕地址
     */
    private String subtitlesUrl;
    /**
     * 排序号
     */
    private int ordernumber;
    /**
     * 评论数
     */
    private int commentCount;
    /**
     * 播放时长
     */
    private int broadcastPace;
    /**
     * 点赞数
     */
    private int praiseNumber;
    /**
     * 1已收藏 2未收藏
     */
    private int isFavorites;


    protected ResourceVo(Parcel in) {
        length = in.readInt();
        type = in.readInt();
        content = in.readString();
        source = in.readInt();
        description = in.readString();
        resourceId = in.readString();
        themeName = in.readString();
        labelName = in.readString();
        labelId = in.readString();
        fileSize = in.readString();
        pvcount = in.readInt();
        audit = in.readInt();
        grade = in.readInt();
        author = in.readString();
        isPraise = in.readInt();
        resourceName = in.readString();
        code = in.readString();
        status = in.readInt();
        albumId = in.readString();
        themeId = in.readString();
        albumName = in.readString();
        namejc = in.readString();
        pay = in.readInt();
        sets = in.readInt();
        updateName = in.readString();
        updateTime = in.readString();
        createName = in.readString();
        createTime = in.readString();
        recommended = in.readInt();
        searchCount = in.readString();
        imageUrl = in.readString();
        resourceUrl = in.readString();
        subtitlesUrl = in.readString();
        ordernumber = in.readInt();
        commentCount = in.readInt();
        broadcastPace = in.readInt();
        praiseNumber = in.readInt();
        isFavorites = in.readInt();
    }

    public static final Creator<ResourceVo> CREATOR = new Creator<ResourceVo>() {
        @Override
        public ResourceVo createFromParcel(Parcel in) {
            return new ResourceVo(in);
        }

        @Override
        public ResourceVo[] newArray(int size) {
            return new ResourceVo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(length);
        parcel.writeInt(type);
        parcel.writeString(content);
        parcel.writeInt(source);
        parcel.writeString(description);
        parcel.writeString(resourceId);
        parcel.writeString(themeName);
        parcel.writeString(labelName);
        parcel.writeString(labelId);
        parcel.writeString(fileSize);
        parcel.writeInt(pvcount);
        parcel.writeInt(audit);
        parcel.writeInt(grade);
        parcel.writeString(author);
        parcel.writeInt(isPraise);
        parcel.writeString(resourceName);
        parcel.writeString(code);
        parcel.writeInt(status);
        parcel.writeString(albumId);
        parcel.writeString(themeId);
        parcel.writeString(albumName);
        parcel.writeString(namejc);
        parcel.writeInt(pay);
        parcel.writeInt(sets);
        parcel.writeString(updateName);
        parcel.writeString(updateTime);
        parcel.writeString(createName);
        parcel.writeString(createTime);
        parcel.writeInt(recommended);
        parcel.writeString(searchCount);
        parcel.writeString(imageUrl);
        parcel.writeString(resourceUrl);
        parcel.writeString(subtitlesUrl);
        parcel.writeInt(ordernumber);
        parcel.writeInt(commentCount);
        parcel.writeInt(broadcastPace);
        parcel.writeInt(praiseNumber);
        parcel.writeInt(isFavorites);
    }
}
