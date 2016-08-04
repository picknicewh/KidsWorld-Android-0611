package net.hunme.user.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.baselibrary.mode.Result;
import net.hunme.baselibrary.network.OkHttpListener;
import net.hunme.baselibrary.network.OkHttps;
import net.hunme.baselibrary.util.G;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.user.R;
import net.hunme.user.adapter.PhotoAdapter;
import net.hunme.user.mode.PhotoVo;
import net.hunme.user.util.MyAlertDialog;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/7/18
 * 描    述：用户相册
 * 版    本：1.0
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class UPhotoActivity extends BaseActivity implements View.OnClickListener, OkHttpListener {
    private ListView lv_photo;
    private PhotoAdapter adapter;
    private List<PhotoVo>photoList; //用户相册实体类 list
    private UserMessage um;
    /**
     * 获取我的相册
     */
    private String MYFlICKR="/appUser/myFlickr.do";
    /**
     * 新建相册
     */
    private String CREATEEFILCK="/appUser/createFlickr.do";
    /**
     * 弹框新建相册
     */
    private AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_photo);
        initView();
        initDate();
    }
    @Override
    protected void setToolBar() {
        setCententTitle("我的相册");
        setLiftImage(R.mipmap.ic_arrow_lift);
        setSubTitle("添加");
        setLiftOnClickClose();
        setSubTitleOnClickListener(this);
    }

    private void initView(){
        lv_photo= (ListView) findViewById(R.id.lv_photo);
    }

    private void initDate(){
        um=UserMessage.getInstance(this);
        photoList=new ArrayList<>();
        adapter=new PhotoAdapter(photoList,this);
        lv_photo.setAdapter(adapter);
        getMyPhoto();
        lv_photo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(UPhotoActivity.this,AlbumDetailsActivity.class);
                intent.putExtra("flickrId",photoList.get(i).getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        int viewID=view.getId();
        if(viewID==R.id.tv_subtitle){
            //获取View
            final View dialogView = LayoutInflater.from(this).inflate(R.layout.alertdialog_add_album, null);
            //获取弹框
            alertDialog =MyAlertDialog.getDialog(dialogView,this,1);
            Button alertCancel= (Button) dialogView.findViewById(R.id.b_cancel);
            Button alertConfirm= (Button) dialogView.findViewById(R.id.b_confirm);
            final EditText etAlbumName= (EditText) dialogView.findViewById(R.id.et_album_name);
            etAlbumName.setFocusable(true);
            etAlbumName.setFocusableInTouchMode(true);
            etAlbumName.requestFocus();
            //取消
            alertCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
            //确定
            alertConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String albumName=etAlbumName.getText().toString().trim();
                    if(G.isEmteny(albumName)){
                        G.showToast(UPhotoActivity.this,"相册名不能为空");
                        return;
                    }
                    createFlickr(albumName);
                }
            });
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getMyPhoto();
    }

    /**
     * 获取相册列表
     */
    private void getMyPhoto(){
        Map<String,Object> map=new HashMap<>();
        map.put("tsId",um.getTsId());
        Type type=new TypeToken<Result<List<PhotoVo>>>(){}.getType();
        OkHttps.sendPost(type,MYFlICKR,map,this);
    }

    /**
     * 创建相册
     * @param flickrName 相册名
     */
    private void createFlickr(String flickrName){
        Map<String,Object>map=new HashMap<>();
        map.put("tsId",um.getTsId());
        map.put("flickrName",flickrName);
        Type type=new TypeToken<Result<String>>(){}.getType();
        OkHttps.sendPost(type,CREATEEFILCK,map,this);
    }
    @Override
    public void onSuccess(String uri, Object date) {
        if(MYFlICKR.equals(uri)){
            Result<List<PhotoVo>> result= (Result<List<PhotoVo>>) date;
            photoList.clear();
            photoList.addAll(result.getData());
            adapter.notifyDataSetChanged();
        }else if(CREATEEFILCK.equals(uri)) {
            //创建相册成功 刷新一遍相册数据
            getMyPhoto();
            if(alertDialog!=null) alertDialog.dismiss();
            G.showToast(this,"相册新建成功");
        }
    }

    @Override
    public void onError(String uri, String error) {
        G.showToast(this,error);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(alertDialog!=null){
            alertDialog.cancel();
        }
    }
}
