package net.hunme.user.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.baselibrary.util.G;
import net.hunme.user.R;
import net.hunme.user.adapter.AlbumAdapter;
import net.hunme.user.adapter.AlbumGridViewAdapter;
import net.hunme.user.mode.ImageItemVo;
import net.hunme.user.util.AlbumHelper;
import net.hunme.user.util.Bimp;
import net.hunme.user.util.ImageBucket;

import java.util.ArrayList;
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
    private List<ImageBucket>contentList;
    private AlbumGridViewAdapter adapter;
    private AlbumHelper helper;
    private TextView tv_select_album;
    public static Bitmap bimap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        initView();
        initDate();
    }

    private void initView(){
        myGridView=$(R.id.myGrid);
        tv_select_album=$(R.id.tv_select_album);
        G.initDisplaySize(this);
        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.notifyDataSetChanged();
            }
        });
        tv_select_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopAllAlbum();
            }
        });
        bimap= BitmapFactory.decodeResource(getResources(),R.mipmap.ic_unfocused);
    }

    private void initDate(){
        helper = AlbumHelper.getHelper();
        helper.init(getApplicationContext());
        contentList= helper.getImagesBucketList(false);//本地所有相册对象
        dateList=new ArrayList<>();
        for(int i=0;i<contentList.size();i++){
            dateList.addAll(contentList.get(i).imageList);
        }
//        dateList = (List<ImageItemVo>) getIntent().getSerializableExtra(EXTRA_IMAGE_LIST);
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
//                Intent intent = new Intent(AlbumActivity.this,
//                        UploadPhotoActivity.class);
//                startActivity(intent);
                finish();
            }
        });
        setLiftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 显示系统相册
     */
    private void showPopAllAlbum(){
        PopupWindow window=new PopupWindow();
        window.setHeight(G.size.H*2/3);
        window.setWidth(G.size.W);
        window.setFocusable(true);
        window.setTouchable(true);
        window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        window.setContentView(getPopWindowView(window));
        window.showAsDropDown(tv_select_album, 0, 0, Gravity.BOTTOM);
    }

    /**
     * 加载系统相册
     * @param window 弹框
     * @return 装载系统相册的view
     */
    private View getPopWindowView(final PopupWindow window){
        ListView listView=new ListView(this);
        final AlbumAdapter Aadapter=new AlbumAdapter(this,contentList);
        listView.setAdapter(Aadapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dateList.clear();
                dateList.addAll(contentList.get(i).imageList);
                adapter.notifyDataSetChanged();
                window.dismiss();
            }
        });
        return listView;
    }

}
