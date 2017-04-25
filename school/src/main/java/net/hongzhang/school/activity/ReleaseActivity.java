package net.hongzhang.school.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.school.R;
import net.hongzhang.user.adapter.GridAlbumAdapter;

import java.util.ArrayList;

/**
 * 发布活动
 */
public class ReleaseActivity extends BaseActivity {
    private GridView gvPicture;
    private ArrayList<String> imgList;
    private GridAlbumAdapter albumAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);
        gvPicture = (GridView) findViewById(R.id.gv_picture);
        imgList = new ArrayList<>();
        albumAdapter = new GridAlbumAdapter(imgList, this, 9);
        gvPicture.setAdapter(albumAdapter);

    }


    @Override
    protected void setToolBar() {
        setLiftOnClickClose();
        setCententTitle("发布活动");
        setSubTitle("发送");
        setSubTitleOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }
}
