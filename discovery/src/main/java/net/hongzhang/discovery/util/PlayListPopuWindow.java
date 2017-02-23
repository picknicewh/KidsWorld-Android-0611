package net.hongzhang.discovery.util;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.mode.ResourceVo;
import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.discovery.MainPlayActivity;
import net.hongzhang.discovery.MyMusicAdapter;
import net.hongzhang.discovery.R;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者： wh
 * 时间： 2016/11/11
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class PlayListPopuWindow extends PopupWindow implements OkHttpListener,View.OnClickListener {
    private MainPlayActivity context;
    public  static  List<ResourceVo> resourceVos;
    private  View contentView ;
    private   int height ;
    private int position;
    public  static MyMusicAdapter adapter;
    private    ListView lv_song;
    private  TextView tv_song_size;
    public static boolean isEdit =false;
    private ImageView iv_collect;
    private   static  int isCollect;
    private String themeId ;
    public  PlayListPopuWindow(MainPlayActivity context, List<ResourceVo> resourceVos, String themeId, int position){
        this.context =context;
        this.position = position;
        this.themeId = themeId;
        height = (int) (G.size.H *0.7);
        this.resourceVos = resourceVos;
        if (resourceVos!=null){
            isCollect = resourceVos.get(0).getIsFavorites();
        }
        init();
    }
    private void initView(){
         contentView = LayoutInflater.from(context).inflate(R.layout.pop_playlist,null);
         lv_song = (ListView) contentView.findViewById(R.id.lv_song_list);
         tv_song_size = (TextView)contentView.findViewById(R.id.tv_list_size);
         iv_collect = (ImageView)contentView.findViewById(R.id.iv_collect);
         final TextView tv_song_close = (TextView)contentView.findViewById(R.id.tv_list_close);
         iv_collect.setOnClickListener(this);
         if (isCollect==1){
            iv_collect.setImageResource(R.mipmap.star_dark_full);
         }else {
             iv_collect.setImageResource(R.mipmap.star_dark);
         }
         setLayoutParam(lv_song);
         tv_song_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
         setListViewData(resourceVos);
         lv_song.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                sendPosition(i);
                context.setIsPlay(true);
                dismiss();
            }
        });


    }
    /**
     * 改变service播放的位置
     * @param position
     */
    private void sendPosition(int position){
        Intent intent = new Intent(Constants.ACTION_POSITION);
        intent.putExtra("position",position);
        context.sendBroadcast(intent);
    }
    private void init(){
        initView();
        //设置SignPopupWindow弹出窗体的宽
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        //设置SignPopupWindow弹出窗体的高
        this.setHeight(height);
        //设置SignPopupWindow的View
        this.setContentView(contentView);
        //设置SignPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setTouchable(true);
        this.setOutsideTouchable(false);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        //设置SignPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //防止虚拟软键盘被弹出菜单遮住
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }
    private void setLayoutParam(ListView lv_song ){
        int lv_height = height-G.dp2px(context,50) *2;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,lv_height);
        lv_song.setLayoutParams(layoutParams);
     }
    @Override
    public void onSuccess(String uri, Object date) {
        Result<String> data  = (Result<String>) date;
        String  result = data.getData();
        Toast.makeText(context,result,Toast.LENGTH_SHORT).show();
    }
    private void setListViewData(List<ResourceVo> resourceVos){
        adapter = new MyMusicAdapter(context,resourceVos,position);
        lv_song.setAdapter(adapter);
        tv_song_size.setText("播放列表("+resourceVos.size()+")");
    }
    @Override
    public void onError(String uri, String error) {
        Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
    }
    /**
     * 提交收藏列表
     * @param  type 1收藏 2取消收藏
     */
    private void subAttention(int type,String  themeId){
        Map<String,Object> params = new HashMap<>();
        String tsId = UserMessage.getInstance(context).getTsId();
        if (G.isEmteny(tsId)){
            Toast.makeText(context,"请登录后在收藏！",Toast.LENGTH_SHORT).show();
            return;
        }
        params.put("tsId", tsId);
        params.put("albumId",themeId);
        params.put("type",type);
        Type mType = new TypeToken<Result<String>>(){}.getType();
        OkHttps.sendPost(mType, Apiurl.SUBATTENTION,params,this);
    }
    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.iv_collect){
            isEdit = true;
            if (isCollect==1){
                iv_collect.setImageResource(R.mipmap.star_dark);
                isCollect = 2;
            }else if (isCollect==2){
                iv_collect.setImageResource(R.mipmap.star_dark_full);
                isCollect = 1;
            }
            subAttention(isCollect,themeId);
        }
    }
}
