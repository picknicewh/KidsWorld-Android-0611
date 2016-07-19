package net.hunme.user.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import net.hunme.baselibrary.activity.BaseActivity;
import net.hunme.user.R;
import net.hunme.user.adapter.AlbumGridViewAdapter;
import net.hunme.user.mode.ImageItemVo;
import net.hunme.user.util.AlbumHelper;
import net.hunme.user.util.Bimp;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/7/18
 * 描    述：用户本地所有相册中所有图片显示
 * 版    本：1.0
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class AlbumActivity extends BaseActivity {
    public static final String EXTRA_IMAGE_LIST = "imagelist";
    private GridView myGridView;
    private List<ImageItemVo>dateList;
    private AlbumGridViewAdapter adapter;
    private AlbumHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        initView();
        initDate();
    }

    private void initView(){
        myGridView=$(R.id.myGrid);
        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void initDate(){
        helper = AlbumHelper.getHelper();
        helper.init(getApplicationContext());
        dateList = (List<ImageItemVo>) getIntent().getSerializableExtra(EXTRA_IMAGE_LIST);
        myGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter=new AlbumGridViewAdapter(dateList,this);
        myGridView.setAdapter(adapter);
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_launcher);
        setCententTitle("选择图片");
        setSubTitle("确定");
        setSubTitleOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collection<String> c = adapter.map.values();
                Iterator<String> it = c.iterator();
                for (; it.hasNext();) {
                    if (Bimp.bmp.size() < 9) {
                        Bimp.tempSelectBitmap.add(it.next());
                    }
                }
                Intent intent = new Intent(AlbumActivity.this,
                        UploadPhotoActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
