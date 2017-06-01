package net.hongzhang.status.presenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.DetaiCodeUtil;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.baselibrary.widget.MyAlertDialog;
import net.hongzhang.status.R;
import net.hongzhang.user.util.BitmapCache;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者： wanghua
 * 时间： 2017/5/25
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class PublicStatusPresenter implements PublicStatusContract.Presenter, OkHttpListener {
    private PublicStatusContract.View view;
    private Activity context;

    public PublicStatusPresenter(Activity context, PublicStatusContract.View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void publishstatus(String dyContent, String dynamicType, List<String> itemList, String classId) {
        if (G.isEmteny(dyContent) && dynamicType.equals("3")) {
            G.showToast(context, "发布的内容不能为空");
            view.setSubTitleEnable(true);
            return;
        }
        if (dynamicType.equals("1") && itemList.size() == 0) {
            G.showToast(context, "发布的图片不能为空");
            view.setSubTitleEnable(true);
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("tsId", UserMessage.getInstance(context).getTsId());
        map.put("text", dyContent);
        map.put("classId", classId);
        map.put("dynamicType", dynamicType);
        Type type = new TypeToken<Result<String>>() {}.getType();
        if ((dynamicType.equals("1") || dynamicType.equals("2") && itemList.size() > 0)){
            List<File> files = new ArrayList<>();
            if (dynamicType.equals("2")){
                G.log("xxxxxxxxxxxxxxx"+itemList.get(0));
                files.add(new File(itemList.get(0)));
            }else {
                files = BitmapCache.getFileList(itemList);
            }
            OkHttps.sendPost(type, Apiurl.DYNAMIC, map, files, this);

        } else {
            OkHttps.sendPost(type, Apiurl.DYNAMIC, map, this);
        }
        view.showLoadingDialog();
    }
    @Override
    public void publishcaurse(String dyContent, List<String> itemList) {
        if (G.isEmteny(dyContent) || itemList.size() < 1) {
            G.showToast(context, "发布的内容不能为空");
            view.setSubTitleEnable(true);
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("tsId", UserMessage.getInstance(context).getTsId());
        map.put("content", dyContent);
        Type type = new TypeToken<Result<String>>() {
        }.getType();
        List<File> list = BitmapCache.getFileList(itemList);
        OkHttps.sendPost(type, Apiurl.SCHOOL_PUBLISHCAURSE, map, list, this);
        view.showLoadingDialog();
    }
    @Override
    public void getExitPrompt() {
        G.log("-xxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        View coupons_view = LayoutInflater.from(context).inflate(R.layout.alertdialog_message, null);
        final AlertDialog alertDialog = MyAlertDialog.getDialog(coupons_view, context, 1);
        Button b_notrigst = (Button) coupons_view.findViewById(R.id.pop_notrigst);
        Button b_mastrigst = (Button) coupons_view.findViewById(R.id.pop_mastrigst);
        TextView pop_title = (TextView) coupons_view.findViewById(R.id.tv_poptitle);
        b_mastrigst.setText("确认");
        pop_title.setText("是否放弃本次编辑？");
        b_notrigst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        b_mastrigst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                context.finish();
            }
        });
    }
    @Override
    public void onSuccess(String uri, Object date) {
        view.stopLoadingDialog();
        if (uri.equals(Apiurl.DYNAMIC) || uri.equals(Apiurl.SCHOOL_PUBLISHCAURSE)) {
            view.setSubTitleEnable(true);
            G.showToast(context, "发布成功!");
            G.KisTyep.isReleaseSuccess = true;
            context.finish();
        }
    }

    @Override
    public void onError(String uri, Result result) {
        view.stopLoadingDialog();
        view.setSubTitleEnable(true);
        G.KisTyep.isReleaseSuccess = true;
        DetaiCodeUtil.errorDetail(result, context);
    }
}
