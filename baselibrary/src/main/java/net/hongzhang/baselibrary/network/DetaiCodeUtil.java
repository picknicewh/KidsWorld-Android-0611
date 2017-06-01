package net.hongzhang.baselibrary.network;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.widget.UpdateConformDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： wanghua
 * 时间： 2017/4/12
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class DetaiCodeUtil {
    /**
     * 0	成功	9
     -9	系统错误	9	服务端出现问题
     -8	返回编码错误	9	服务器问题
     -7	非法访问	9	不要这么提示，让用户重新登录
     -6	服务器维护	9	暂停访问，服务器维护中
     -5	登录过期	9	请重新登录
     -99	请升级客户端	9	禁止继续访问所有接口，强制升级
     90001	账号或密码错误	9
     90002	未获取二维码	9	访问顺序出错
     90003	二维码失效	9
     90004	账号已冻结	9	账号状态异常
     90005	角色错误	9	为找到该角色
     90006	确认超时	9	二维码或其余操作超时
     90007	权限不匹配	9	没有权限进行此操作
     90008	参数不完整	9	缺少参数或参数值为空
     90009	操作错误	9	如未移交群主便退出
     90010	请勿重复操作	9
     90011	未获取验证码	9	没有获取验证码
     90012	密码错误	9
     90013	手机号码错误	9
     90014	手机号码已注册	9
     90015	未找到短信通道	9
     90016	格式错误	9
     90017	无需更新	9
     90018	短信通道异常	9
     90019	只限制教师端登录	9
     90020	已由另一位家长提交	9	该家长不可在此时间段提交，此时间段已有人提交
     90021	该时间段食谱已提交	9
     90022	该时间段已提交请假	9	时间重复，请用户重新选择时间段
     90023	未找到点赞信息	9
     */
    public static  final String CODE_SUCCESS  = "0";
    public static  final String CODE_FAILTURE  = "-9";
    public static  final String CODE_SERVICE_FAILTURE  =" -8";
    public static  final  String CODE_ILLEGAL_ACCESS = "-7";
    public static  final String CODE_SERVER_MAINTENTENANCE = "-6";
    public static  final String CODE_LOGIN_EXPIRED  ="-5";
    public static  final String CODE_UPGRADE   = "-99";
    public static  final String CODE_SKIP  = "90032";
    public static  void DetaiCode(String code, Context context){
            switch (code){
                case CODE_SUCCESS:
                case CODE_FAILTURE:
                case CODE_SERVICE_FAILTURE:
                case CODE_SERVER_MAINTENTENANCE:break;
                case CODE_ILLEGAL_ACCESS:
                case  CODE_LOGIN_EXPIRED:goLoginActivity(context);break;
                case CODE_UPGRADE:
                     dialogs.clear();
                     UpdateConformDialog conformDialog =new UpdateConformDialog(context);
                     dialogs.add(conformDialog);
                     dialogs.get(0).show();
                    break;
                case CODE_SKIP:
                   G.showToast(context,"skip其他操作");
                    break;

        }
    }
    private static List<UpdateConformDialog> dialogs = new ArrayList<>();
    /**
     * 前往登陆页面
     * @param context
     */
    private static void goLoginActivity(Context context) {
        ComponentName componetName = new ComponentName("net.hongzhang.bbhow",
                "net.hongzhang.login.activity.LoginActivity");
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setComponent(componetName);
        context.startActivity(intent);
    }
    public static void errorDetail(Result error,Context context){
            Result<String> data = error;
            DetaiCode(data.getCode(),context);
            G.showToast(context, data.getData());
    }
}
