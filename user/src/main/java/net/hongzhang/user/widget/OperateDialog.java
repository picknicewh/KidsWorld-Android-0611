package net.hongzhang.user.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import net.hongzhang.baselibrary.activity.UpdateMessageActivity;
import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.widget.MyAlertDialog;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.login.LoginActivity;
import net.hongzhang.user.R;
import net.hongzhang.user.activity.USettingActivity;
import net.hongzhang.user.adapter.PhotoAdapter;
import net.hongzhang.user.mode.PhotoVo;
import net.hongzhang.user.util.CacheHelp;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;

/**
 * 作者： wh
 * 时间： 2016/9/27
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class OperateDialog implements View.OnClickListener, OkHttpListener {
    private Activity context;
    /**
     * 退出对话框
     */
    private AlertDialog exitdialog;
    /**
     * 修改密码对话框
     */
    private AlertDialog passwordialog;
    /**
     * 添加相册对话框
     */
    private AlertDialog albumdialog;
    /**
     * 退出取消按钮
     */
    private Button ex_b_cancal;
    /**
     * 退出确定按钮
     */
    private Button ex_b_confrom;
    /**
     * 修改密码取消按钮
     */
    private Button pa_b_cancal;
    /**
     * 修改密码确定按钮
     */
    private Button pa_b_confrom;
    /**
     * 添加相册取消按钮
     */
    private Button ab_b_cancal;
    /**
     * 添加相册确定按钮
     */
    private Button ab_b_confrom;
    /**
     * 编辑密码框
     */
    private EditText pa_et_password;
    /**
     * 编辑相册名框
     */
    private EditText ab_et_name;
    /**
     * 处理方式 flag =1 处理退出程序 flag= 0 消除缓存
     */
    private int flag;
    private PhotoAdapter adapter;
    private List<PhotoVo> photoList; //用户相册实体类 list

    public OperateDialog(Activity context, int flag) {
        this.context = context;
        this.flag = flag;
    }

    public OperateDialog(Activity context) {
        this.context = context;
    }

    //
    public OperateDialog(Activity context, PhotoAdapter adapter, List<PhotoVo> photoList) {
        this.context = context;
        this.adapter = adapter;
        this.photoList = photoList;
    }

    /**
     * 初始化修改密码的对话框
     */
    public void initPasswordView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_modifyphone, null);
        passwordialog = MyAlertDialog.getDialog(view, context, 1);
        pa_b_confrom = (Button) view.findViewById(R.id.bt_dg_conform);
        pa_b_cancal = (Button) view.findViewById(R.id.bt_dg_cancel);
        pa_et_password = (EditText) view.findViewById(R.id.et_dg_password);
        pa_b_cancal.setOnClickListener(this);
        pa_b_confrom.setOnClickListener(this);
    }

    /**
     * 初始化退出程序或者清除缓存的view
     */
    public void initexitView() {
        View coupons_view = LayoutInflater.from(context).inflate(R.layout.alertdialog_message, null);
        exitdialog = MyAlertDialog.getDialog(coupons_view, context, 1);
        ex_b_cancal = (Button) coupons_view.findViewById(R.id.pop_notrigst);
        ex_b_confrom = (Button) coupons_view.findViewById(R.id.pop_mastrigst);
        ex_b_cancal.setOnClickListener(this);
        ex_b_confrom.setOnClickListener(this);
        TextView tv_title = (TextView) coupons_view.findViewById(R.id.tv_poptitle);
        if (flag == 1) {
            ex_b_confrom.setText("确认退出");
            tv_title.setText("是否退出当前账号？");
        } else {
            ex_b_confrom.setText("确认");
            tv_title.setText("确认清除缓存的数据和图片吗？");
        }
    }

    public void initAlbumview() {
        //获取View
        final View dialogView = LayoutInflater.from(context).inflate(R.layout.alertdialog_add_album, null);
        //获取弹框
        albumdialog = MyAlertDialog.getDialog(dialogView, context, 1);
        ab_b_cancal = (Button) dialogView.findViewById(R.id.b_cancel);
        ab_b_confrom = (Button) dialogView.findViewById(R.id.b_confirm);
        ab_et_name = (EditText) dialogView.findViewById(R.id.et_album_name);
        ab_b_cancal.setOnClickListener(this);
        ab_b_confrom.setOnClickListener(this);
        ab_et_name.setFocusable(true);
        ab_et_name.setFocusableInTouchMode(true);
        ab_et_name.requestFocus();
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.pop_notrigst) {
            exitdialog.dismiss();
        } else if (viewId == R.id.pop_mastrigst) {
            if (flag == 1) {
                //退出账号
                if (RongIM.getInstance() != null) {
                    RongIM.getInstance().disconnect();
                }
                MobclickAgent.onProfileSignOff();
                UserMessage.getInstance(context).clean();
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
                context.finish();
            } else {
                CacheHelp.deleteFolderFile(Environment.getExternalStorageDirectory().toString() + "/ChatFile", true);
                USettingActivity.tv_cache.setText("暂无缓存");
            }
            exitdialog.dismiss();
        } else if (viewId == R.id.bt_dg_conform) {
            Intent intent = new Intent();
            String password = pa_et_password.getText().toString();
            if (TextUtils.isEmpty(password)) {
                G.showToast(context, "密码不能为空");
                return;
            } else if (!password.equals(UserMessage.getInstance(context).getPassword())) {
                G.showToast(context, "输入密码不正确");
                return;
            }
            intent.putExtra("phone", password);
            intent.setClass(context, UpdateMessageActivity.class);
            context.startActivity(intent);
            passwordialog.dismiss();
        } else if (viewId == R.id.bt_dg_cancel) {
            passwordialog.dismiss();
        } else if (viewId == R.id.b_cancel) {
            albumdialog.dismiss();
        } else if (viewId == R.id.b_confirm) {
            String albumName = ab_et_name.getText().toString().trim();
            if (G.isEmteny(albumName)) {
                G.showToast(context, "相册名不能为空");
                return;
            }
            createFlickr(albumName);
            albumdialog.dismiss();
        }
    }

    /**
     * 创建相册
     *
     * @param flickrName 相册名
     */
    private void createFlickr(String flickrName) {
        Map<String, Object> map = new HashMap<>();
        map.put("tsId", UserMessage.getInstance(context).getTsId());
        map.put("flickrName", flickrName);
        Type type = new TypeToken<Result<String>>() {
        }.getType();
        OkHttps.sendPost(type, Apiurl.CREATEEFILCK, map, this);
    }

    /**
     * 获取相册列表
     */
    private void getMyPhoto() {
        Map<String, Object> map = new HashMap<>();
        map.put("tsId", UserMessage.getInstance(context).getTsId());
        Type type = new TypeToken<Result<List<PhotoVo>>>() {
        }.getType();
        OkHttps.sendPost(type, Apiurl.MYFlICKR, map, this, 2, "MYFlICKR");
    }


    @Override
    public void onSuccess(String uri, Object date) {
        if (Apiurl.CREATEEFILCK.equals(uri)) {
            //创建相册成功 刷新一遍相册数据
            getMyPhoto();
            if (albumdialog != null) albumdialog.dismiss();
            G.showToast(context, "相册新建成功");
        } else if (Apiurl.MYFlICKR.equals(uri)) {
            Result<List<PhotoVo>> result = (Result<List<PhotoVo>>) date;
            photoList.clear();
            photoList.addAll(result.getData());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onError(String uri, String error) {
        G.showToast(context, error);
    }
}
