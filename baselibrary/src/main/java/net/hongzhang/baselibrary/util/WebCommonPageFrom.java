package net.hongzhang.baselibrary.util;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.hongzhang.baselibrary.R;
import net.hongzhang.baselibrary.cordova.HMDroidGap;
import net.hongzhang.baselibrary.image.ImageCache;

/**
 * 作者： Administrator
 * 时间： 2016/7/29
 * 名称：
 * 版本说明：收藏页面和乐园公共的调用方法
 * 附加注释：
 * 主要接口：
 */
public class WebCommonPageFrom {

    /**
     * 左边图片
     */
    private ImageView iv_left;
    /**
     * 中间文字
     */
    private TextView tv_title;
    /**
     * 右边图片
     */
    private ImageView iv_right;
    /**
     * 首页搜索
     */
    private Button et_search;
    private Activity activity;
    public static String dynamicId;
    public WebCommonPageFrom(ImageView iv_left, TextView tv_title, ImageView iv_right, Button et_search, Activity activity){
        this.iv_left = iv_left;
        this.tv_title = tv_title;
        this.iv_right = iv_right;
        this.activity = activity;
        this.et_search = et_search;
    }
    public WebCommonPageFrom(ImageView iv_left, TextView tv_title, ImageView iv_right,Activity activity){
        this.iv_left = iv_left;
        this.tv_title = tv_title;
        this.iv_right = iv_right;
        this.activity = activity;
    }
    /**
     * 设置导航栏
     * @param view 不同的界面
     */
    @JavascriptInterface
    public void  setToolBar(final String view) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (view) {
                    case Constant.HOME:
                       // iv_left.setImageResource(R.mipmap.ic_history);
                        ImageCache.imageLoader(UserMessage.getInstance(activity).getHoldImgUrl(),iv_left);
                      /*  tv_title.setText("乐园");
                        tv_title.setVisibility(View.VISIBLE);*/
                        iv_right.setImageResource(R.mipmap.ic_history);
                        iv_right.setVisibility(View.VISIBLE);
                        tv_title.setVisibility(View.GONE);
                        break;
                    case Constant.CHILDSTORY:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("幼儿听听");
                        tv_title.setVisibility(View.VISIBLE);
                        iv_right.setImageResource(R.mipmap.ic_search);
                        iv_right.setVisibility(View.VISIBLE);
                        break;
                    case Constant.CHILDCLASS:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("幼儿课堂");
                        tv_title.setVisibility(View.VISIBLE);
                        iv_right.setImageResource(R.mipmap.ic_search);
                        iv_right.setVisibility(View.VISIBLE);
                        break;
                    case Constant.CONSULT:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("教育资讯");
                        tv_title.setVisibility(View.VISIBLE);
                        iv_right.setImageResource(R.mipmap.ic_search);
                        iv_right.setVisibility(View.VISIBLE);
                        break;
                    case Constant.SAFEED:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("安全教育");
                        tv_title.setVisibility(View.VISIBLE);
                        iv_right.setImageResource(R.mipmap.ic_empty);
                        //iv_right.setVisibility(View.GONE);
                        break;
                    case Constant.CONSULTDETAIL:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                   //     tv_title.setText("教育资讯");
                        tv_title.setVisibility(View.GONE);
                        iv_right.setVisibility(View.GONE);
                        break;
                    case Constant.SEARCH:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("搜索");
                        tv_title.setVisibility(View.VISIBLE);
                        iv_right.setImageResource(R.mipmap.ic_empty);
                        iv_right.setVisibility(View.VISIBLE);
                        break;
                    case Constant.VEDIO:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                       // tv_title.setText("幼儿课堂");
                        tv_title.setVisibility(View.GONE);
                        iv_right.setVisibility(View.GONE);
                        break;
                    case Constant.MEDIAPLAY:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("幼儿听听");
                        tv_title.setVisibility(View.VISIBLE);
                        iv_right.setImageResource(R.mipmap.ic_search);
                        iv_right.setVisibility(View.VISIBLE);
                        break;
                    case Constant.MEDIAPLAYDEATIL:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                      //  tv_title.setText("幼儿听听");
                        tv_title.setVisibility(View.GONE);
                        iv_right.setVisibility(View.GONE);
                        break;
                    case Constant.MEDIAPLAYING:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                       // tv_title.setText("幼儿听听");
                        tv_title.setVisibility(View.GONE);
                        iv_right.setVisibility(View.GONE);
                        break;
                    case Constant.SEARCH_CAUSRE:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("搜索课程");
                        tv_title.setVisibility(View.VISIBLE);
                        iv_right.setImageResource(R.mipmap.ic_empty);
                        break;
                    case Constant.SEARCH_MUSIC:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("搜索听听");
                        tv_title.setVisibility(View.VISIBLE);
                       // iv_right.setVisibility(View.GONE);
                        iv_right.setImageResource(R.mipmap.ic_empty);
                        break;
                    case Constant.SEARCH_CON:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("搜索资讯");
                        tv_title.setVisibility(View.VISIBLE);
                        iv_right.setImageResource(R.mipmap.ic_empty);
                        break;
                    case Constant.PLAY_HISTORY:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("播放记录");
                        tv_title.setVisibility(View.VISIBLE);
                        //iv_right.setVisibility(View.GONE);
                        iv_right.setImageResource(R.mipmap.ic_empty);
                        break;
                    case Constant.COLLECT:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("我的收藏");
                        tv_title.setVisibility(View.VISIBLE);
                      //  iv_right.setVisibility(View.GONE);
                        iv_right.setImageResource(R.mipmap.ic_empty);
                        break;
                }
                if (view.equals(Constant.HOME)){
                    int size = G.dp2px(activity,40);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size,size);
                    layoutParams.gravity = Gravity.CENTER_VERTICAL;
                    layoutParams.setMargins(G.dp2px(activity,10),0,0,0);
                    iv_left.setLayoutParams(layoutParams);
                    if (et_search!=null){
                        et_search.setVisibility(View.VISIBLE);
                    }
                }else {
                    LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams2.gravity = Gravity.CENTER_VERTICAL;
                    if (activity instanceof  HMDroidGap ||
                            activity instanceof  HMDroidGap&&view.equals(Constant.CONSULTDETAIL)
                            ||activity instanceof  HMDroidGap&&view.equals(Constant.VEDIO)||
                             activity instanceof  HMDroidGap&&view.equals(Constant.MEDIAPLAYING)){
                        layoutParams2.setMargins(G.dp2px(activity,0),0,0,0);
                    }
                    else {
                        layoutParams2.setMargins(G.dp2px(activity,10),0,0,0);
                    }
                    iv_left.setLayoutParams(layoutParams2);

                    if (et_search!=null){
                        et_search.setVisibility(View.GONE);
                    }
                }
            }
        });
    }
    /**
     *  设置导航栏标题
    */
    @JavascriptInterface
    public  void  setClassCauseTitle(final String title){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams2.gravity = Gravity.CENTER_VERTICAL;
                layoutParams2.setMargins(G.dp2px(activity,10),0,0,0);
                iv_left.setLayoutParams(layoutParams2);
                iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                tv_title.setText(title);
                tv_title.setVisibility(View.VISIBLE);
                iv_right.setImageResource(R.mipmap.ic_empty);
                if (et_search!=null){
                    et_search.setVisibility(View.GONE);
                }
              //  iv_right.setVisibility(View.GONE);
            }
        });
    }

    @JavascriptInterface
    public void setWebLoading(String url){
        Intent intent=new Intent(activity,HMDroidGap.class);
        intent.putExtra("loadUrl",url);
        intent.putExtra("title","我的收藏");
        activity.startActivity(intent);
    }
    @JavascriptInterface
    public void sendBroadcast(boolean isVisible){
        Intent intent = new Intent(BroadcastConstant.HIDEMAINTAB);
        intent.putExtra("isVisible",isVisible);
        activity.sendBroadcast(intent);
    }

    @JavascriptInterface
    public void startStatusDetils(String dynamicId){
        if(G.isEmteny(dynamicId)){
            return;
        }
        this.dynamicId=dynamicId;
        ComponentName componetName = new ComponentName("net.hongzhang.bbhow","net.hongzhang.status.activity.StatusDetilsActivity");
        Intent intent = new Intent();
        intent.setComponent(componetName);
        intent.putExtra("dynamicId",dynamicId);
        activity.startActivity(intent);
    }
    /**
     * 前往主页面
     * @param
     */
    @JavascriptInterface
    public  void goDetailActivity(String targetId){
        ComponentName componetName = new ComponentName("net.hongzhang.bbhow","net.hongzhang.message.activity.PersonDetailActivity");
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setComponent(componetName);
        intent.putExtra("targetId",targetId);
        activity.startActivity(intent);
    }
    @JavascriptInterface
    public void startMusicActivity(String themeId,String resourceId) {
        Intent intent = new Intent();
        ComponentName componetName = new ComponentName("net.hongzhang.bbhow","net.hongzhang.discovery.MainPlayActivity");
        intent.setComponent(componetName);
        intent.putExtra("themeId",themeId);
        intent.putExtra("resourceId",resourceId);
        activity.startActivity(intent);
        Log.i("aaaaaa","===========startMusicActivity=========");
    }
    @JavascriptInterface
    public void startVideoActivity(String themeId,String resourceId) {
        Intent intent = new Intent();
        ComponentName componetName = new ComponentName("net.hongzhang.bbhow","net.hongzhang.discovery.activity.FullPlayVideoActivity");
        intent.setComponent(componetName);
        intent.putExtra("albumId",themeId);
        intent.putExtra("resourceId",resourceId);
        activity.startActivity(intent);
        Log.i("aaaaaa","===========startVideoActivity=========");
    }
    @JavascriptInterface
    public void back(){
        activity.finish();
    }
}
