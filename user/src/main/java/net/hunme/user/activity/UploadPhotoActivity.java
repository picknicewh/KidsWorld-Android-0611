package net.hunme.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.pizidea.imagepicker.AndroidImagePicker;
import com.pizidea.imagepicker.bean.ImageItem;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.baselibrary.mode.Result;
import net.hunme.baselibrary.network.OkHttpListener;
import net.hunme.baselibrary.network.OkHttps;
import net.hunme.baselibrary.util.G;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.user.R;
import net.hunme.user.adapter.GridAlbumAdapter;
import net.hunme.user.util.BitmapCache;

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
    private List<String> itemList;
    private String UPLOADPHOTO="/appUser/uploadPhoto.do";
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
    }

    private void initDate(){
        G.initDisplaySize(this);
        itemList=new ArrayList<>();
        mAdapter=new GridAlbumAdapter(itemList,this);
        gv_photo.setAdapter(mAdapter);
        gv_photo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i ==itemList.size()) {
                    getPhotos();
                }
            }
        });
        getPhotos();//立即跳转到图片选择页面
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
     * 跳转到选择本地图片页面
     */
    private void getPhotos(){
        AndroidImagePicker.getInstance().pickMulti(UploadPhotoActivity.this, true, new AndroidImagePicker.OnImagePickCompleteListener() {
            @Override
            public void onImagePickComplete(List<ImageItem> items) {
                if(items != null && items.size() > 0){
                    for(ImageItem item:items){
                        G.log("选择了===="+item.path);
                        if(itemList.size()<9){
                            itemList.add(item.path);
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
        return;
    }

    /**
     * 上传图片到目标相册
     */
    private void uploadPhoto(List<File> fileList){
        Map<String,Object>map=new HashMap<>();
        map.put("tsId", UserMessage.getInstance(this).getTsId());
        map.put("flickrId",AlbumDetailsActivity.flickrId);
        Type type=new TypeToken<Result<String>>(){}.getType();
        OkHttps.sendPost(type,UPLOADPHOTO,map,fileList,this);
    }

    @Override
    public void onSuccess(String uri, Object date) {
        G.showToast(this,"图片上传成功");
        isUploadSuccess=true;
        finish();
    }

    @Override
    public void onError(String uri, String error) {
        G.showToast(this,error);
        isUploadSuccess=false;
    }
}
