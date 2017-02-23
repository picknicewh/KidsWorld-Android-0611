package net.hongzhang.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.hongzhang.user.R;

import java.util.List;

/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/7/19
 * 描    述：用户上传图片目标相册适配器
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class AlbumSelectAdapter extends BaseAdapter {
    private List<String>albumNameList;
    private Context context;
    private View lastView;
    private int selectPosition;

    public AlbumSelectAdapter(List<String> albumNameList, Context context) {
        this.albumNameList = albumNameList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return albumNameList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHold viewHold;
        if(null==view){
            view= LayoutInflater.from(context).inflate(R.layout.item_album_select,null);
            new ViewHold(view);
        }
        viewHold= (ViewHold) view.getTag();
        viewHold.albumName.setText(albumNameList.get(i));
        if(i==selectPosition){
            changItemImage(view,selectPosition);
        }
        return view;
    }

    class ViewHold {
        TextView albumName;
        ImageView albumSelect;

        public ViewHold(View view) {
            albumName= (TextView) view.findViewById(R.id.tv_album_name);
            albumSelect= (ImageView) view.findViewById(R.id.iv_album_select);
            view.setTag(this);
        }
    }

    public void changItemImage(View view,int position){
        if(lastView!=null&&selectPosition!=position){
            ViewHold hold= (ViewHold) lastView.getTag();
            if(hold.albumSelect.getVisibility()==View.VISIBLE){
                hold.albumSelect.setVisibility(View.INVISIBLE);
            }
        }
        this.lastView=view;
        this.selectPosition=position;
        ViewHold hold= (ViewHold) view.getTag();
        if(hold.albumSelect.getVisibility()==View.INVISIBLE) hold.albumSelect.setVisibility(View.VISIBLE);
    }

    public void setSelectPosition(int position){
        this.selectPosition=position;
    }

}
