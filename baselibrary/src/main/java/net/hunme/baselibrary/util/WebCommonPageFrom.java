package net.hunme.baselibrary.util;


import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.ImageView;
import android.widget.TextView;

import net.hunme.baselibrary.R;
import net.hunme.baselibrary.cordova.HMDroidGap;

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
    private Activity activity;
    public WebCommonPageFrom(ImageView iv_left,TextView tv_title,ImageView iv_right,Activity activity){
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
                        iv_left.setImageResource(R.mipmap.ic_history);
                        tv_title.setText("乐园");
                        tv_title.setVisibility(View.VISIBLE);
                        iv_right.setImageResource(R.mipmap.ic_search);
                        iv_right.setVisibility(View.VISIBLE);
                        break;
                    case Constant.CHILDSTORY:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("幼儿故事");
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
                        iv_right.setVisibility(View.GONE);
                        break;
                    case Constant.CONSULTDETAIL:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("教育资讯");
                        tv_title.setVisibility(View.VISIBLE);
                        iv_right.setVisibility(View.GONE);
                        break;
                    case Constant.SEARCH:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("搜索");
                        tv_title.setVisibility(View.VISIBLE);
                        iv_right.setImageResource(R.mipmap.ic_search);
                        iv_right.setVisibility(View.VISIBLE);
                        break;
                    case Constant.VEDIO:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("幼儿课堂");
                        tv_title.setVisibility(View.VISIBLE);
                        iv_right.setImageResource(R.mipmap.ic_search);
                        iv_right.setVisibility(View.VISIBLE);
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
                        tv_title.setText("幼儿听听");
                        tv_title.setVisibility(View.VISIBLE);
                        iv_right.setVisibility(View.GONE);
                        break;
                    case Constant.MEDIAPLAYING:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("幼儿听听");
                        tv_title.setVisibility(View.VISIBLE);
                        iv_right.setVisibility(View.GONE);
                        break;
                    case Constant.SEARCH_CAUSRE:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("搜索课程");
                        tv_title.setVisibility(View.VISIBLE);
                        iv_right.setVisibility(View.GONE);
                        break;
                    case Constant.SEARCH_MUSIC:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("搜索音乐");
                        tv_title.setVisibility(View.VISIBLE);
                        iv_right.setVisibility(View.GONE);
                        break;
                    case Constant.SEARCH_CON:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("搜索资讯");
                        tv_title.setVisibility(View.VISIBLE);
                        iv_right.setVisibility(View.GONE);
                        break;
                    case Constant.PLAY_HISTORY:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("播放记录");
                        tv_title.setVisibility(View.VISIBLE);
                        iv_right.setVisibility(View.GONE);
                        break;
                    case Constant.COLLECT:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("我的收藏");
                        tv_title.setVisibility(View.VISIBLE);
                        iv_right.setVisibility(View.GONE);
                        break;
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
                iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                tv_title.setText(title);
                tv_title.setVisibility(View.VISIBLE);
                iv_right.setVisibility(View.GONE);
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
}
