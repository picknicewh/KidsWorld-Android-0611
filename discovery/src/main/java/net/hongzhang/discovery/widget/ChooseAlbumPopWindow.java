package net.hongzhang.discovery.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import net.hongzhang.baselibrary.util.G;
import net.hongzhang.discovery.R;
import net.hongzhang.discovery.adapter.AlbumChooseAdapter;


/**
 * 作者： wh
 * 时间： 2016/12/9
 * 名称：收费弹框
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class ChooseAlbumPopWindow extends PopupWindow implements AlbumChooseAdapter.onItemClickListener {
    private View contentView;
    private Context context;
    private AlbumChooseAdapter adapter;
    private RecyclerView recyclerView;

    public ChooseAlbumPopWindow(Context context) {
        this.context = context;
        init();

    }
    public void initView() {
        contentView = LayoutInflater.from(context).inflate(R.layout.pop_album_choose, null);
        recyclerView = (RecyclerView) contentView.findViewById(R.id.rv_album_choose);
        ImageView imageView = (ImageView) contentView.findViewById(R.id.iv_delete);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        adapter = new AlbumChooseAdapter(context);
        GridLayoutManager manager = new GridLayoutManager(context,4);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }
    private void init() {
        initView();
        //设置SignPopupWindow的View
        this.setContentView(contentView);
        //设置SignPopupWindow弹出窗体的高
        int width = G.size.W-G.dp2px(context,100);
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        //设置SignPopupWindow弹出窗体的宽
        this.setWidth(width);
        //设置SignPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setTouchable(true);
        this.setOutsideTouchable(true);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        //设置SignPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //防止虚拟软键盘被弹出菜单遮住
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public void OnItemClick(View view, int position) {

    }
}
