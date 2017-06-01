package net.hongzhang.bbhow.share;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.DetaiCodeUtil;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.bbhow.R;
import net.hongzhang.user.util.BitmapCache;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者： wanghua
 * 时间： 2017/3/27
 * 名称：分享的弹窗
 * 版本说明：
 * 附加注释：
 * 主要接口：发布动态
 */
public class SharePopWindow extends PopupWindow implements View.OnClickListener, OkHttpListener {
    private RecyclerView rvPicture;
    private Activity context;
    private View contentView;
    /**
     * 发表动态类型
     */
    private String dynamicType;
    /**
     *  图片或视频的地址
     */
    private List<String> pathList;
    /**
     * 发布动态班级空间id
     */
    private String classId;
    /**
     * 动态内容
     */
    private String content;

    public SharePopWindow(Activity context, String dynamicType, List<String> pathList, String classId, String content) {
        this.context = context;
        this.dynamicType = dynamicType;
        this.pathList = pathList;
        this.classId = classId;
        this.content = content;
        this.context = context;
        init();
    }

    private void initView() {
        contentView = LayoutInflater.from(context).inflate(R.layout.pop_share, null);
        rvPicture = (RecyclerView) contentView.findViewById(R.id.rv_picture);
        TextView tvContent = (TextView) contentView.findViewById(R.id.tv_content);
        TextView tvConcel = (TextView) contentView.findViewById(R.id.tv_concel);
        TextView tvConform = (TextView) contentView.findViewById(R.id.tv_conform);
        if (pathList!=null &&pathList.size()>0){
            PopPictureAdapter adapter = new PopPictureAdapter(context, pathList);
            LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            rvPicture.setAdapter(adapter);
            rvPicture.setLayoutManager(manager);
        }
        tvContent.setText(content);
        tvConform.setOnClickListener(this);
        tvConcel.setOnClickListener(this);
    }

    /**
     * 发布动态
     * @param  dyContent 动态内容
     * @param  dynamicType 动态类型 1=图片 2=视屏 3=纯文字
     * @param  pathList 图片或视频的地址
     * @param  classId 发布动态班级空间id
     */
    private void publishstatus(String dyContent, String dynamicType, List<String> pathList, String classId) {
        if (G.isEmteny(dyContent) && dynamicType.equals("3")) {
            G.showToast(context, "发布的内容不能为空");
            return;
        }
        if (dynamicType.equals("1") && pathList.size() == 0) {
            G.showToast(context, "发布的图片不能为空");
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("tsId", UserMessage.getInstance(context).getTsId());
        map.put("text", dyContent);
        map.put("classId", classId);
        Type type = new TypeToken<Result<String>>() {
        }.getType();
        if (pathList.size() > 0) {
            List<File> list = BitmapCache.getFileList(pathList);
            map.put("dynamicType", dynamicType);
            OkHttps.sendPost(type, Apiurl.DYNAMIC, map, list, this);
        } else {
            map.put("dynamicType", dynamicType);
            OkHttps.sendPost(type, Apiurl.DYNAMIC, map, this);
        }
    }
    /**
     * 初始化popwindow各项参数
     */
    private void init() {
        initView();
        //设置SignPopupWindow的View
        this.setContentView(contentView);
        //设置SignPopupWindow弹出窗体的高
        this.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        //设置SignPopupWindow弹出窗体的宽
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        //设置SignPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setTouchable(true);
        this.setOutsideTouchable(true);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SignPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //防止虚拟软键盘被弹出菜单遮住
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }
    @Override
    public void onClick(View view) {
        int viewID = view.getId();
        if (viewID == R.id.tv_concel) {
            dismiss();
            context.finish();
        } else if (viewID == R.id.tv_conform) {
            publishstatus(content, dynamicType, pathList, classId);
        }
    }
    @Override
    public void onSuccess(String uri, Object date) {
        dismiss();
        G.showToast(context, "发布成功!");
        G.KisTyep.isReleaseSuccess = true;
        context.finish();
    }
    @Override
    public void onError(String uri, Result error) {
        dismiss();
        DetaiCodeUtil.errorDetail(error,context);
        G.KisTyep.isReleaseSuccess = false;
    }

}
