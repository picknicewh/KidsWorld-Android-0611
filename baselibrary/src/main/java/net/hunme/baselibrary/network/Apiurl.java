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

    //通讯
    /**
     * 获取通讯成员
     */
    public static final String MESSAGE_GETGTOUP = "/message/getGroup.do";
    /**
     * 获取用户详情
     */
    public static final String MESSAGE_GETDETAIL = "/message/getTS.do";
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

    //学校
    /**
     * 提交请假申请
     */
    public static final String SCHOOL_SUBLEAVE = "/school/subLeave.do" ;
    /**
     * 发布课程
     */
    public static final String SCHOOL_PUBLISHCAURSE = "/school/setSyllabus.do" ;
    /**
     * 请假
     */
    public static final String SCHOOL_GETLEAVES = "/school/getLeave.do" ;
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

}
