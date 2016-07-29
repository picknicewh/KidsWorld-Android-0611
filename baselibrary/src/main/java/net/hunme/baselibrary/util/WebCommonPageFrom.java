package net.hunme.baselibrary.util;


import android.app.Activity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.ImageView;
import android.widget.TextView;

import net.hunme.baselibrary.R;

/**
 * 作者： Administrator
 * 时间： 2016/7/29
 * 名称：
 * 版本说明：收藏页面和乐园公共的调用方法
 * 附加注释：
 * 主要接口：
 */
public class WebCommonPageFrom {
    private ImageView iv_left;
    private TextView tv_title;
    private ImageView iv_right;
    private Activity activity;


    private String page;
    public WebCommonPageFrom(ImageView iv_left,TextView tv_title,ImageView iv_right,Activity activity){
        this.iv_left = iv_left;
        this.tv_title = tv_title;
        this.iv_right = iv_right;
        this.activity = activity;
    }
    /**
     * 设置导航栏
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
                        page = Constant.HOME;
                        setPage(page);
                        break;
                    case Constant.CHILDSTORY:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("幼儿故事");
                        tv_title.setVisibility(View.VISIBLE);
                        iv_right.setImageResource(R.mipmap.ic_search);
                        iv_right.setVisibility(View.VISIBLE);
                        page = Constant.CHILDSTORY;
                        setPage(page);
                        break;
                    case Constant.CHILDCLASS:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("幼儿课堂");
                        tv_title.setVisibility(View.VISIBLE);
                        iv_right.setImageResource(R.mipmap.ic_search);
                        iv_right.setVisibility(View.VISIBLE);
                        page = Constant.CHILDCLASS;
                        setPage(page);
                        break;
                    case Constant.CONSULT:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("教育资讯");
                        tv_title.setVisibility(View.VISIBLE);
                        iv_right.setImageResource(R.mipmap.ic_search);
                        iv_right.setVisibility(View.VISIBLE);
                        page = Constant.CONSULT;
                        setPage(page);
                        break;
                    case Constant.SAFEED:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("安全教育");
                        tv_title.setVisibility(View.VISIBLE);
                        iv_right.setVisibility(View.GONE);
                        page = Constant.SAFEED;
                        setPage(page);
                        break;
                    case Constant.CONSULTDETAIL:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("教育资讯");
                        tv_title.setVisibility(View.VISIBLE);
                        iv_right.setVisibility(View.GONE);
                        page = Constant.CONSULTDETAIL;
                        setPage(page);
                        break;
                    case Constant.SEARCH:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("搜索");
                        tv_title.setVisibility(View.VISIBLE);
                        iv_right.setImageResource(R.mipmap.ic_search);
                        iv_right.setVisibility(View.VISIBLE);
                        page = Constant.SEARCH;
                        setPage(page);
                        break;
                    case Constant.VEDIO:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        iv_right.setVisibility(View.GONE);
                        tv_title.setVisibility(View.GONE);
                        page = Constant.VEDIO;
                        setPage(page);
                        break;
                    case Constant.MEDIAPLAY:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("幼儿听听");
                        tv_title.setVisibility(View.VISIBLE);
                        iv_right.setImageResource(R.mipmap.ic_search);
                        iv_right.setVisibility(View.VISIBLE);
                        page = Constant.MEDIAPLAY;
                        setPage(page);
                        break;
                    case Constant.MEDIAPLAYDEATIL:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("幼儿听听");
                        tv_title.setVisibility(View.VISIBLE);
                        iv_right.setVisibility(View.GONE);
                        page = Constant.MEDIAPLAYDEATIL;
                        setPage(page);
                        break;
                    case Constant.MEDIAPLAYING:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        iv_right.setVisibility(View.GONE);
                        tv_title.setVisibility(View.GONE);
                        page = Constant.MEDIAPLAYING;
                        setPage(page);
                        break;
                    case Constant.SEARCH_CAUSRE:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("搜索课程");
                        tv_title.setVisibility(View.VISIBLE);
                        iv_right.setVisibility(View.GONE);
                        page = Constant.SEARCH_CAUSRE;
                        setPage(page);
                        break;
                    case Constant.SEARCH_MUSIC:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("搜索音乐");
                        tv_title.setVisibility(View.VISIBLE);
                        iv_right.setVisibility(View.GONE);
                        page = Constant.SEARCH_MUSIC;
                        setPage(page);
                        break;
                    case Constant.SEARCH_CON:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("搜索资讯");
                        tv_title.setVisibility(View.VISIBLE);
                        iv_right.setVisibility(View.GONE);
                        page = Constant.SEARCH_CON;
                        setPage(page);
                        break;
                    case Constant.PLAY_HISTORY:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("播放记录");
                        tv_title.setVisibility(View.VISIBLE);
                        iv_right.setVisibility(View.GONE);
                        page = Constant.PLAY_HISTORY;
                        setPage(page);
                    case Constant.COLLECT:
                        iv_left.setImageResource(R.mipmap.ic_arrow_lift);
                        tv_title.setText("收藏");
                        page = Constant.COLLECT;
                        setPage(page);
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
    public void setPage(String page) {
        this.page = page;
    }

    public String getPage() {
        return page;
    }

}
