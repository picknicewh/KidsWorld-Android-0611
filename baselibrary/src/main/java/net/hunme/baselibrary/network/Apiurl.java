package net.hunme.baselibrary.network;

/**
 * 作者： Administrator
 * 时间： 2016/7/25
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class Apiurl {

    //=============================通讯=====================================
    /**
     * 获取通讯成员
     */
    public static final String MESSAGE_GETGTOUP = "/message/getGroup.do";
    /**
     * 获取用户详情
     */
    public static final String MESSAGE_GETDETAIL = "/message/getTS.do";

    //=============================学校======================================
    /**
     * 提交请假申请
     */
    public static final String SCHOOL_SUBLEAVE = "/school/subLeave.do" ;
    /**
     * 发布课程
     */
    public static final String SCHOOL_PUBLISHCAURSE = "/school/setSyllabus.do" ;
    /**
     * 创建群
     */
    public static final String MESSAGE_CREATE_GROUP = "/message/createGroupChat.do";
    /**
     * 加入群
     */
    public  static  final String  MESSAGE_ADD_MEMBER= "/message/addGroupChat.do";
    /**
     *退出群
     */
    public static final String  MESSAGE_EXIT_MEMBER = "/message/exitGroupChat.do";
    /**
     *查看群成员
     */
    public static final String  MESSAGE_SEARCH_MEMBER = "/message/groupMember.do";
    /**
     *查看不在群内成员
     */
    public static final String  MESSAGE_WITHOUT_MEMBER = "/message/notGroupMember.do";
    /**
     * 解散群
     */
    public static final String  MESSAGE_DISSORE_GROUP = "/message/dissolve.do";
    /**
     * 编辑群名称
     */
    public static final String  MESSAGE_EDITGROUPNAME = "/message/editGroupChatName.do";
    /**
     * 获取课程表
     */
    public static final String SCHOOL_GETSYLLABUSLISTS = "/school/getSyllabusList.do" ;
    /**
     * 删除课程表
     */
    public static final String SCHOOL_DETLTESYLLABUSLISTS = "/school/deleteSyllabus.do";
    /**
     * 获取食谱
     */
    public static final String SCHOOL_GETCOOKBOOK = "/school/getCookbook.do";
    /**
     * 请假
     */
    public static final String SCHOOL_GETLEAVES = "/school/getLeave.do" ;

    //==============================个人中心============================
    /**
     * 意见反馈
     */
    public static final String FEEDBACK="/appUser/feedback.do";
    /**
     * 获取相册所有图片
     */
    public static final String FILCKR="/appUser/flickr.do";
    /**
     * 修改用户个性签名
     */
    public static final String SETSIGN="/appUser/setSignature.do";
    /**
     * 修改用户头像
     */
    public static final String AVATAR="/appUser/setAvatar.do";
    /**
     * 获取我的相册
     */
    public static final String MYFlICKR="/appUser/myFlickr.do";
    /**
     * 新建相册
     */
    public static final String CREATEEFILCK="/appUser/createFlickr.do";
    /**
     * 上传图片
     */
    public static final String UPLOADPHOTO="/appUser/uploadPhoto.do";
    /**
     * 我的收藏
     */
    public static final String MYCOLLECTION=ServerConfigManager.WEB_IP+"/paradise/index.html";
    /**
     * 我的动态
     */
    public static final String MYDYNAMICS= "/dynamics/myDynamic.do";
    /**
     * 删除动态
     */
    public static final String DELETEMYDYNAMICS= "/dynamics/deleteDynamic.do";

    /**
     * 版本更新地址
     */
    public static final String UPDATESYSTEM="/appUser/getVersion.do";

    //=========================动态================================

    /**
     * 获取动态详情
     */
    public static final String STATUSLIST ="/dynamics/getDynamic.do";
    /**
     * 获取动态详情
     */
    public static final String STATUSDETILS ="/dynamics/getDynamicDetail.do";
    /**
     * 提交评论
     */
    public static final String SUBCOMMENT ="/dynamics/subComment.do";
    /**
     * 提交赞
     */
    public static final String SUBPRAISE ="/dynamics/subPraise.do";
    /**
     * 删除评论
     */
    public static final String DELETERCOMMENTD ="/dynamics/deleteComment.do";
    /**
     * 获取班级列表uri
     */
    public static final String DYNAMICHEAD="/dynamics/getDynamicHead.do";

    //=========================登录==============================
    /**
     * 用户登录
     */
    public static final String APPLOGIN="/app/login.do";
    /**
     * 用户身份选择
     */
    public static final String SELECTUSER="/app/selectUser.do";
}
