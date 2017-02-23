package net.hongzhang.baselibrary.util;

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
    public static  final String  SYSYDOS="net.hongzhang.user.activity.ShowSysDosReceiver";
    /**
     *学校通知红点广播
     */
    public static  final String  SCHOOLINFODOS="net.hongzhang.school.activity.ShowInfoDosReceiver";
    /**
     *请假红点广播
     */
    public static  final String  LEAVEASEKDOS="net.hongzhang.school.activity.ShowLeaveDosReceiver";
    /**
     *喂药红点广播
     */
    public static  final String  MEDICINEDOS="net.hongzhang.school.activity.ShowMedicineDosReceiver";
    /**
     *评论点赞动态的推送广播
     */
    public static  final String  COMMENTINFO="net.hongzhang.status.activity.CommnetDosReceiver";
    /**
     *动态的红点广播
     */
    public static  final String  STATUSDOS="net.hongzhang.status.showstatusdos";
    /**
     *主页动态的红点广播
     */
    public static  final String  MAINSTATUSDOS = " net.hongzhang.bbhwo.MyStatusDosShowReceiver";
    /**
     *刷新主页
     */
    public static  final String  RUSHMIANA = " net.hongzhang.bbhow.RUSHMAIN";
    /**
     *主页学校的红点广播
     */
    public static  final String  MAINSCHOOLDOS = " net.hongzhang.bbhow.MySchoolDosShowReceiver";
    /**
     *全屏播放视频隐藏底部tab广播
     */
    public static  final String  HIDEMAINTAB = " net.hongzhang.bbhow.MyDosShowReceiver";

}
