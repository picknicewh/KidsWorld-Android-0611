package net.hunme.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.pizidea.imagepicker.ImgLoader;
import com.pizidea.imagepicker.UilImgLoader;

import net.hunme.baselibrary.image.ImageCache;
import net.hunme.baselibrary.util.G;
import net.hunme.user.R;

import java.util.List;

/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/7/28
 * 描    述：
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class AlbumDetailsAdapter extends BaseAdapter {
    private Context context;
    private List<String>imagePath;
    private ImgLoader presenter;
    public AlbumDetailsAdapter(Context context, List<String> imagePath) {
        this.context = context;
        this.imagePath = imagePath;
        presenter = new UilImgLoader();
    }

    @Override
    public int getCount() {
//        return imagePath.size();
        return 30;
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
        ViewHolder holder;
        if(view==null){
            view= LayoutInflater.from(context).inflate(R.layout.item_album_details,null);
            new ViewHolder(view);
        }
        holder= (ViewHolder) view.getTag();
        ImageCache.imageLoader(imagePath.get(i),holder.image);
//        getBitmapData(holder.image,imagePath.get(i));
        return view;
    }

    class ViewHolder {
        public ImageView image;

        public ViewHolder(View view) {
            image = (ImageView) view.findViewById(R.id.iv_image);
            view.setTag(this);
        }
    }

    private void getBitmapData(ImageView imageView ,String imagePath){
        presenter.onPresentImage(imageView,imagePath, G.px2dp(context,800));
    }
}
