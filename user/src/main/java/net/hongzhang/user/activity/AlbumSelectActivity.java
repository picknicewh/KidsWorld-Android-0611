package net.hongzhang.user.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.user.R;
import net.hongzhang.user.adapter.AlbumSelectAdapter;

import java.util.List;
/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/7/18
 * 描    述：用户上传图片目标相册
 * 版    本：1.0
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class AlbumSelectActivity extends BaseActivity {
    private ListView lv_album; //用户相册列表
    private List<String>albumNameList; //用户相册名
    private AlbumSelectAdapter adapter; //适配器
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_select);
        lv_album=$(R.id.lv_album);
        initDate();
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
        setCententTitle("选择相册");
    }

    private void initDate(){
        albumNameList=getIntent().getStringArrayListExtra("namelist");
        adapter=new AlbumSelectAdapter(albumNameList,this);
        lv_album.setAdapter(adapter);
        adapter.setSelectPosition(UploadPhotoActivity.position);
        lv_album.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.changItemImage(view,i);
                UploadPhotoActivity.position=i;
                finish();
            }
        });

    }
}
