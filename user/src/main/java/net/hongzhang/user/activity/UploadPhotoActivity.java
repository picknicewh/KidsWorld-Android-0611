package net.hongzhang.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.baselibrary.widget.LoadingDialog;
import net.hongzhang.user.R;
import net.hongzhang.user.adapter.GridAlbumAdapter;
import net.hongzhang.user.util.BitmapCache;
import net.hongzhang.user.util.PublishPhotoUtil;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/7/18
 * 描    述：上传图片
 * 版    本：1.0
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class UploadPhotoActivity extends BaseActivity implements View.OnClickListener, OkHttpListener {
    private GridView gv_photo;
    private LinearLayout ll_selcet_photoname;
    private TextView tv_album_name;
    private ArrayList<String> albumNameList;
    public static int position=0;
    private static final int Album_NAME_SELECT=1111;
    private GridAlbumAdapter mAdapter;
    private ArrayList<String> itemList;
    private LoadingDialog dialog;
    /**
     * 上传图片的状态
     * 如果为true 表示图片上传成功 返回到相册详情的时候 需要重新获取相片 并刷新
     * 反之 不需要刷新
     */
    public static boolean isUploadSuccess=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);
        initView();
        initDate();
    }

    private void initView(){
        gv_photo=$(R.id.gv_photo);
        ll_selcet_photoname=$(R.id.ll_selcet_photoname);
        tv_album_name=$(R.id.tv_album_name);
        ll_selcet_photoname.setOnClickListener(this);
        dialog = new LoadingDialog(this,R.style.LoadingDialogTheme);
    }

    private void initDate(){
        G.initDisplaySize(this);
        itemList=new ArrayList<>();
        PublishPhotoUtil.goSelectImager(itemList,this,gv_photo,9);
        PublishPhotoUtil.showPhoto(this,itemList,gv_photo,9);
     /*   mAdapter=new GridAlbumAdapter(itemList,this,9);
        gv_photo.setAdapter(mAdapter);
        gv_photo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i ==itemList.size()) {
                    getPhotos();
                }
            }
        });
        getPhotos();//立即跳转到图片选择页面*/
        albumNameList=new ArrayList<>();
        albumNameList.add("默认相册");
        albumNameList.add("相册一");
        albumNameList.add("相册二");
        tv_album_name.setText(albumNameList.get(position));
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setCententTitle("上传照片");
        setSubTitle("完成");
        setLiftOnClickClose();
        setSubTitleOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int viewId=view.getId();
        if(viewId==R.id.tv_subtitle){
            //上传图片
            if(itemList.size()<1){
                G.showToast(this,"上传图片不能为空");
                return;
            }
            List<File>list= BitmapCache.getFileList(itemList);
            uploadPhoto(list);
        }else if(viewId==R.id.ll_selcet_photoname){
            //选择相册上传  现在没有了
            Intent intent=new Intent(this, AlbumSelectActivity.class);
            intent.putStringArrayListExtra("namelist",albumNameList);
            startActivityForResult(intent,Album_NAME_SELECT);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Album_NAME_SELECT:
                tv_album_name.setText(albumNameList.get(position));
                break;
        }
    }

    /**
     * 上传图片到目标相册
     */
    private void uploadPhoto(List<File> fileList){
        Map<String,Object>map=new HashMap<>();
        map.put("tsId", UserMessage.getInstance(this).getTsId());
        map.put("flickrId",AlbumDetailsActivity.flickrId);
        Type type=new TypeToken<Result<String>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.UPLOADPHOTO,map,fileList,this);
        dialog.show();
    }

    @Override
    public void onSuccess(String uri, Object date) {
        itemList.clear();
        dialog.dismiss();
        G.showToast(this,"图片上传成功");
        isUploadSuccess=true;
        finish();
    }

    @Override
    public void onError(String uri, String error) {
        dialog.dismiss();
        G.showToast(this,error);
        isUploadSuccess=false;
    }
}
