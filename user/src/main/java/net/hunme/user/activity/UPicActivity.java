package net.hunme.user.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import net.hunme.baselibrary.activity.BaseActivity;
import net.hunme.user.R;
import net.hunme.user.adapter.ImageBucketAdapter;
import net.hunme.user.util.AlbumHelper;
import net.hunme.user.util.ImageBucket;

import java.io.Serializable;
import java.util.List;
/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/7/18
 * 描    述：用户本地所有相册
 * 版    本：1.0
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class UPicActivity extends BaseActivity {
    private List<ImageBucket> dataList;
    private GridView gridView;
    private ImageBucketAdapter adapter;// 自定义的适配器
    private AlbumHelper helper;
    public static final String EXTRA_IMAGE_LIST = "imagelist";
    public static Bitmap bimap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic);
        helper = AlbumHelper.getHelper();
        helper.init(getApplicationContext());
        initData();
        initView();
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_launcher);
        setLiftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setCententTitle("选择相册");
    }
    /**
     * 初始化数据
     */
    private void initData() {
        dataList = helper.getImagesBucketList(false);
        bimap= BitmapFactory.decodeResource(
                getResources(),
                R.mipmap.ic_unfocused);
    }

    /**
     * 初始化view视图
     */
    private void initView() {
        gridView = (GridView) findViewById(R.id.gridview);
        adapter = new ImageBucketAdapter(UPicActivity.this, dataList);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                /**
                 * 通知适配器，绑定的数据发生了改变，应当刷新视图
                 */
                // adapter.notifyDataSetChanged();
                Intent intent = new Intent(UPicActivity.this,
                        AlbumActivity.class);
                intent.putExtra(UPicActivity.EXTRA_IMAGE_LIST,
                        (Serializable) dataList.get(position).imageList);
                startActivity(intent);
                finish();
            }

        });
    }

}
