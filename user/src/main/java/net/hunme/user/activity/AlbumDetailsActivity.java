package net.hunme.user.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.google.gson.reflect.TypeToken;

import net.hunme.baselibrary.activity.PermissionsActivity;
import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.baselibrary.mode.Result;
import net.hunme.baselibrary.network.OkHttpListener;
import net.hunme.baselibrary.network.OkHttps;
import net.hunme.baselibrary.util.G;
import net.hunme.baselibrary.util.PermissionsChecker;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.user.R;
import net.hunme.user.adapter.AlbumDetailsAdapter;
import net.hunme.user.mode.Photodetail;
import net.hunme.user.util.PermissionUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlbumDetailsActivity extends BaseActivity implements OkHttpListener {
    private GridView gv_photo;
    private ArrayList<String>itemList;
    private AlbumDetailsAdapter adapter;
    private final String FILCKR="/appUser/flickr.do";
    public static String flickrId;//相册ID
    // 访问相册所需的全部权限
    private final String[] PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE, //读写权限
            Manifest.permission.CAMERA
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_details);
        initView();
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
        setCententTitle("相册详情");
        setSubTitle("添加");
        setSubTitleOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(new PermissionsChecker(AlbumDetailsActivity.this).lacksPermissions(PERMISSIONS)){
                    PermissionsActivity.startActivityForResult(AlbumDetailsActivity.this, PermissionUtils.REQUEST_CODE, PERMISSIONS);
                    return;
                }
                Intent intent=new Intent(AlbumDetailsActivity.this,UploadPhotoActivity.class);
                intent.putExtra("flickrId",flickrId);
                startActivity(intent);
            }
        });
    }

    private void initView(){
        gv_photo=$(R.id.gv_photo);
        itemList=new ArrayList<>();
        adapter=new AlbumDetailsAdapter(this,itemList);
        gv_photo.setAdapter(adapter);
        flickrId=getIntent().getStringExtra("flickrId");
        getPhotoMessage(flickrId);
    }

    private void getPhotoMessage(String flickrId){
        Map<String,Object> map=new HashMap<>();
        map.put("tsId", UserMessage.getInstance(this).getTsId());
        map.put("flickrId",flickrId);
        map.put("pageSize",10000);
        map.put("pageNumber",1);
        Type type=new TypeToken<Result<List<Photodetail>>>(){}.getType();
        OkHttps.sendPost(type,FILCKR,map,this,2,FILCKR);
    }

    @Override
    public void onSuccess(String uri, Object date) {
        if(FILCKR.equals(uri)){
            Result<List<Photodetail>> result= (Result<List<Photodetail>>) date;
            itemList.clear();
            for (Photodetail p:result.getData()){
                itemList.add(p.getImgurl());
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onError(String uri, String error) {
        G.showToast(this,error);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(UploadPhotoActivity.isUploadSuccess){
            getPhotoMessage(flickrId);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PermissionUtils.REQUEST_CODE:
                //检测到没有授取权限 关闭页面
                if(resultCode == PermissionsActivity.PERMISSIONS_DENIED){
                   G.showToast(this,"权限没有授取，本次操作取消，请到权限中心授权");
                }
                break;
        }
    }
}
