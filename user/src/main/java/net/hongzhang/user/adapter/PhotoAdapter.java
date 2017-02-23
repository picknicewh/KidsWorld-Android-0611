package net.hongzhang.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.hongzhang.baselibrary.image.ImageCache;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.user.R;
import net.hongzhang.user.mode.PhotoVo;

import java.util.List;

/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/7/18
 * 描    述：我的相册适配器
 * 版    本：1.0
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class PhotoAdapter extends BaseAdapter {
    private List<PhotoVo>photoList;
    private Context context;

    public PhotoAdapter(List<PhotoVo> photoList, Context context) {
        this.photoList = photoList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return photoList.size();
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
        ViewHold viewHold = null;
        if(null==view){
            view= LayoutInflater.from(context).inflate(R.layout.item_photo,null);
            new ViewHold(view);
        }
        viewHold= (ViewHold) view.getTag();
        if(null!=photoList){
            if(null==photoList.get(i).getUrl()){

                viewHold.photo.setImageResource(R.mipmap.ic_album_cover);
            }else{
                ImageCache.imageLoader(photoList.get(i).getUrl(),viewHold.photo);
            }
            viewHold.photoName.setText(photoList.get(i).getName());
            String photoCount=photoList.get(i).getCount();
            if(G.isEmteny(photoCount)){
                photoCount="0";
            }
            viewHold.photoNumber.setText("共"+photoCount+"张");
        }
        return view;
    }

    class ViewHold{
        ImageView photo;
        TextView photoName;
        TextView photoNumber;

        public ViewHold(View view) {
            photo= (ImageView) view.findViewById(R.id.iv_photo_itme);
            photoName= (TextView) view.findViewById(R.id.tv_photoname_item);
            photoNumber= (TextView) view.findViewById(R.id.tv_photonumber_item);
            view.setTag(this);
        }
    }
}
