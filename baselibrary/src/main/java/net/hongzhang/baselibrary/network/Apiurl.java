package net.hongzhang.baselibrary.network;

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
     * 获取角色信息-----》新接口
     */
    public static final String MESSAGE_GETTSINFO = "/appUser/getTsInfo.do";
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
    public static final String MESSAGE_ADD_MEMBER = "/message/addGroupChat.do";
    /**
     * 退出群
     */
    public static final String MESSAGE_EXIT_MEMBER = "/message/exitGroupChat.do";
    /**
     * 查看群成员
     */
    public static final String MESSAGE_SEARCH_MEMBER = "/message/groupMember.do";
    /**
     * 查看不在群内成员
     */
    public static final String MESSAGE_WITHOUT_MEMBER = "/message/notGroupMember.do";
    /**
     * 解散群
     */
    public static final String MESSAGE_DISSORE_GROUP = "/message/dissolve.do";
    /**
     * 编辑群名称
     */
    public static final String MESSAGE_EDITGROUPNAME = "/message/editGroupChatName.do";

    //=============================学校======================================
    /**
     * 提交请假申请
     */
    public static final String SCHOOL_SUBLEAVE = "/school/subLeave.do";
    /**
     * 发布课程
     */
    public static final String SCHOOL_PUBLISHCAURSE = "/school/setSyllabus.do";
    /**
     * 发布通知
     */
    public static final String SCHOOL_PUBLISHINFOR = "/school/pushSave.do";
    /**
     * 发布食谱
     */
    public static final String SCHOOL_PUBLISHCOOK = "/school/setCookbook.do";
    /**
     * 获取班级列表
     */
    public static final String SCHOOL_GETCLASSLIST = "/school/getTsClass.do";
    /**
     * 获取课程表
     */
    public static final String SCHOOL_GETSYLLABUSLISTS = "/school/getSyllabusList.do";
    /**
     * 删除课程表
     */
    public static final String SCHOOL_DETLTESYLLABUSLISTS = "/school/deleteSyllabus.do";
    /**
     * 获取食谱
     */
    public static final String SCHOOL_GETCOOKBOOK = "/school/getCookbook.do";
    /**
     * 教师发布食谱
     */
    public static final String SCHOOL_ISSUECB= "/school/teacherIssueCB.do";
    /**
     *  获取班级食谱
     */
    public static final String SCHOOL_GETCB= "/school/getCB.do";
    /**
     * 获取该角色所有班级
     */
    public static final String SCHOOL_GETUSERCLASS= "/appUser/getUserClass.do";

    /**
     * 教师获取今日请假
     */
    public static final String SCHOOL_GETTODAYLEAVES = "/school/todayVacation.do";
    /**
     * 教师获取已请假信息
     */
    public static final String SCHOOL_GETALREADYLEAVES = "/school/alreadyVacation.do";
    /**
     * 家长获取请假中信息
     */
    public static final String SCHOOL_GETPATRIARCHLEAVES = "/school/patriarchVacation.do";
    /**
     * 家长获取历史请假列表
     */
    public static final String SCHOOL_GETHISTORYLEAVES = "/school/patriarchVacationHistory.do";
    /**
     * 家长发布请假
     */
    public static final String SCHOOL_PUBLISHLEAVES = "/school/patriarchSubmitVacation.do";
    /**
     * 家长获取请假事由
     */
    public static final String SCHOOL_LEAVECOURSE = "/school/getVacationContent.do";
    /**
     * 获取常见药品
     */
    public static final String SCHOOL_GETDRIGTYPE = "/school/getDrigType.do";
    /**
     * 获取患病类型
     */
    public static final String SCHOOL_GETSICKENTYPE = "/school/getSickenType.do";
    /**
     *添加患病类型
     */
    public static final String SCHOOL_ADDSICKENTYPE = "/school/addSickenType.do";
    /**
     * 添加常见药品
     */
    public static final String SCHOOL_ADDRIGTYPE = "/school/addDrugType.do";
    /**
     * 发布委托喂药
     */
    public static final String SCHOOL_SUBMITMEICINE = "/school/issueMedicine.do";
    /**
     * 获取喂药日期
     */
    public static final String SCHOOL_GETMEDICINEDATE = "/school/getMedicineDate.do";
    /**
     *获取喂药委托
     */
    public static final String SCHOOL_GETMEDICINE = "/school/getMedicine.do";




    /**
     * 请假
     */
    public static final String SCHOOL_GETLEAVES = "/school/getLeave.do";
    /**
     * 删除请假
     */
    public static final String SCHOOL_DELETELEAVES = "/school/deleteLeave.do";
    /**
     * 教师端喂药列表
     */
    public static final String SCHOOL_MEDICINETLIST = "/school/teacherFeedingMedicine.do";
    /**
     * 教师端喂药详情
     */
    public static final String SCHOOL_MEDICINETDETAIL = "/school/medicineDetails.do";
    /**
     * 删除喂药
     */
    public static final String SCHOOL_MEDICINETDELETE = "/school/deleteMedicine.do";
    /**
     * 完成喂药
     */
    public static final String SCHOOL_MEDICINETFINSH = "/school/finishMedicine.do";
    /**
     * 家长获取喂药列表
     */
    public static final String SCHOOL_MEDICINESLIST = "/school/patriarchFeedingMedicine.do";
    /**
     * 发布委托喂药
     */
    public static final String SCHOOL_MEDICINEPUBLISH = "/school/publishMedicineCommissioned.do";
    /**
     * 教师获取家园互动活动列表
     */
    public static final String SCHOOL_THOMEINTRRACTION = "/homeInteractive/getTeacherHomeInteractive.do";
    /**
     * 家长获取正在进行中的家园互动活动列表
     */
    public static final String SCHOOL_HOMEINTERACTIVELIST = "/homeInteractive/getPatriarchHomeInteractive.do";
    /**
     * 家长获取已截止的家园互动活动列表
     */
    public static final String SCHOOL_ENDHOMEINTERACTIVELIST = "/homeInteractive/getPatriarchEndHomeInteractive.do";
    /**
     * 教师发布活动
     */
    public static final String SCHOOL_PUBLISHACTIVITY = "/homeInteractive/postActivity.do";
    /**
     * 获取活动分类
     */
    public static final String SCHOOL_GRTACTIVITYTYPE = "/homeInteractive/getActivityType.do";
    /**
     * 获取活动维度
     */
    public static final String SCHOOL_GETDIMENDIONAL = "/homeInteractive/getDimensionality.do";
    /**
     * 获取活动详情
     */
    public static final String SCHOOL_GETACTIVEDETAIL = "/homeInteractive/getHomeInteractiveDetails.do";
    /**
     * 家长上传活动
     */
    public static final String SCHOOL_COMMENTACTIVE = "/homeInteractive/patriarchSubmitHomeInteractive.do";
    /**
     * 教师获取活动上传情况
     */
    public static final String SCHOOL_COMMENTACTIVELIST = "/homeInteractive/homeInteractiveUpload.do";
    /**
     * 获取作品详情
     */
    public static final String SCHOOL_GETTEAKDETAIL = "/homeInteractive/getHomeInteractiveWorksDetails.do";
    /**
     * 教师评分获取用户维度
     */
    public static final String SCHOOL_GETDIMESIONALITY = "/homeInteractive/getActivityTsDimensionality.do";
    /**
     * 添加用户活动标签
     */
    public static final String SCHOOL_ADDACTIVETAG = "/homeInteractive/addActivityTagName.do";
    /**
     * 上传评分
     */
    public static final String SCHOOL_COMMITSCORE = "/homeInteractive/scorepoint.do";
    /**
     * 获取活动排行榜
     */
    public static final String SCHOOL_GETACTIVERANKINGLIST = "/homeInteractive/getRankingList.do";
    /**
     * 获取评价详情
     */
    public static final String SCHOOL_GETCOMMENTEDETAIL = "/homeInteractive/getAppraiseDetails.do";
    //==============================个人中心============================
    /**
     * 意见反馈
     */
    public static final String FEEDBACK = "/appUser/feedback.do";
    /**
     * 获取相册所有图片
     */
    public static final String FILCKR = "/appUser/flickr.do";
    /**
     * 修改用户个性签名
     */
    public static final String SETSIGN = "/appUser/setSignature.do";
    /**
     * 修改用户头像
     */
    public static final String AVATAR = "/appUser/setAvatar.do";
    /**
     * 获取我的相册
     */
    public static final String MYFlICKR = "/appUser/myFlickr.do";
    /**
     * 新建相册
     */
    public static final String CREATEEFILCK = "/appUser/createFlickr.do";
    /**
     * 上传图片
     */
    public static final String UPLOADPHOTO = "/appUser/uploadPhoto.do";
    /**
     * 我的收藏
     */
    public static final String MYCOLLECTION = ServerConfigManager.WEB_IP + "/paradise/index.html";
    /**
     * 我的动态
     */
    public static final String MYDYNAMICS = "/dynamics/myDynamic.do";
    /**
     * 删除动态
     */
    public static final String DELETEMYDYNAMICS = "/dynamics/deleteDynamic.do";

    /**
     * 版本更新地址
     */
    public static final String UPDATESYSTEM = "/appUser/getVersion.do";

    //=========================动态================================
    /**
     * 提交动态
     */
    public static final String DYNAMIC = "/dynamics/issueDynamic.do";
    /**
     * 获取动态详情
     */
    public static final String STATUSLIST = "/dynamics/getDynamic.do";
    /**
     * 获取动态详情
     */
    public static final String STATUSDETILS = "/dynamics/getDynamicDetail.do";
    /**
     * 提交评论
     */
    public static final String SUBCOMMENT = "/dynamics/subComment.do";
    /**
     * 提交赞
     */
    public static final String SUBPRAISE = "/dynamics/subPraise.do";
    /**
     * 删除评论
     */
    public static final String DELETERCOMMENTD = "/dynamics/deleteComment.do";
    /**
     * 获取班级列表uri
     */
    public static final String DYNAMICHEAD = "/dynamics/getDynamicHead.do";
    /**
     * 获取动态小窗口
     */
    public static final String STATUSINFO = "/dynamics/getDynamicLittleWindow.do";
    //=========================登录==============================
    public static final String GETISPLAY = "/app/isPay";
    /**
     * 用户登录
     */
    // public static final String APPLOGIN = "/app/login.do";
    public static final String APPLOGIN = "/app/loginBBH.do";
    /**
     * 用户身份选择
     */
    // public static final String SELECTUSER = "/app/selectUser.do";
    public static final String SELECTUSER = "/app/selectTs.do";
    /**
     * 扫描二维码登录
     */

    public static final String SCANLOGIN = "/app/qRCodeLogin.do";
    /**
     * 二次确认
     */
    public static final String CONFIRMCODELOGIN = "/app/qRCodeConfirmLogin.do";
    /**
     * 获取二维码
     */
    public static final String CETQRCODE = "/app/getQRCode.do";
    /**
     * 授权登陆
     */
    public static final String AUTHORIZATION = "/app/authorization.do";
    //=========================乐园================================
    /**
     * 搜索资源
     */
    public static final String SERACHRESOURCE = "/rakuen/resourceSearch.do";
    /**
     * 保存搜索
     */
    public static final String SAVESERACHRE = "/rakuen/saveSearch.do";
    /**
     * 获取banner图
     */
    public static final String GETBANNERLIST = "/rakuen/newGetBanner.do";
    /**
     * 获取推荐列表
     */
    public static final String GETRECOMMENDLIST = "/rakuen/getRecommendAblum.do";
    /**
     * 获取主题列表
     */
    public static final String GETHEMELIST = "/rakuen/getThemeList.do";
    /**
     * 根据主题ID获取对应专辑
     */
    public static final String GETHEMELISTBYID = "/rakuen/getAlbumByTheme.do";
    /**
     * 获取资讯
     */
    public static final String GETRECONSULT = "/rakuen/getInformation.do";
    /**
     * 获取全部推荐列表
     */
    public static final String GETALLRECOMMENDLIST = "/rakuen/getIndexAllRecommenda.do";

    /**
     * 获取某个专辑下的资源列表
     */
    public static final String USER_GETTHENELIST = "/rakuen/getCompilationsAllResource.do";
    /**
     * 搜索足迹
     */
    public static final String SENDSEARCHFOOT = "/rakuen/searchFootprint.do";
    /**
     * 获取某个资源具体信息
     */
    public static final String USER_GETSOURCEDETAIL = "/rakuen/getResourceDetail.do";
    /**
     * 收藏音乐专辑
     */
    public static final String SUBATTENTION = "/rakuen/saveResourceFavorites.do";
    /**
     * 获取收藏列表
     */
    public static final String GETATTENTIONLIST = "/rakuen/getResourceFavorites.do";
    /**
     * 保存播放记录
     */
    public static final String SAVEPLAYRECORDING = "/rakuen/savePlayTheRecording.do";
    /**
     * 获取播放记录
     */
    public static final String GETPLAYRECORDING = "/rakuen/getResourcePlayRecordedCommand.do";
    /**
     * 获取最新上架资源
     */
    public static final String GETNEWALUBMLIST = "/rakuen/newReleases.do";
    /**
     * 提交评论
     */
    public static final String SUBMENTCOMMENT = "/rakuen/subComment.do";
    /**
     * 获取评论列表
     */
    public static final String GETCOMMENTLIST = "/rakuen/getCommentList.do";
    /**
     * 获取某个专辑下的的列表
     */
    public static final String GEALBUMLIST = "/rakuen/getAlbumByTheme.do";
    /**
     * 点赞资源
     */
    public static final String RESSUBPRAISE = "/rakuen/subPraise.do";


}
