package net.hunme.baselibrary.util;

/**
 * 作者： wh
 * 时间： 2016/10/19
 * 名称：所有广播的action
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class BroadcastConstant {
    /**
     *系统通知红点广播
     */
    public static  final String  SYSYDOS="net.hunme.user.activity.ShowSysDosReceiver";
    /**
     *学校通知红点广播
     */
    public static  final String  SCHOOLINFODOS="net.hunme.school.activity.ShowInfoDosReceiver";
    /**
     *请假红点广播
     */
    public static  final String  LEAVEASEKDOS="net.hunme.school.activity.ShowLeaveDosReceiver";
    /**
     *喂药红点广播
     */
    public static  final String  MEDICINEDOS="net.hunme.school.activity.ShowMedicineDosReceiver";
    /**
     *评论点赞动态的推送广播
     */
    public static  final String  COMMENTINFO="net.hunme.status.activity.CommnetDosReceiver";
    /**
     *动态的红点广播
     */
    public static  final String  STATUSDOS="net.hunme.status.showstatusdos";
    /**
     *主页动态的红点广播
     */
    public static  final String  MAINSTATUSDOS = " net.hunme.kidsworld.MyStatusDosShowReceiver";
    /**
     *主页学校的红点广播
     */
    public static  final String  MAINSCHOOLDOS = " net.hunme.kidsworld.MySchoolDosShowReceiver";
    /**
     *全屏播放视频隐藏底部tab广播
     */
    public static  final String  HIDEMAINTAB = " net.hunme.kidsworld.MyDosShowReceiver";

}
