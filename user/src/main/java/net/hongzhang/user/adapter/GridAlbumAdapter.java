package net.hongzhang.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import net.hongzhang.baselibrary.image.ImageCache;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.user.R;

import java.util.ArrayList;


/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/7/25
 * 描    述：
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class GridAlbumAdapter extends BaseAdapter {
    private ArrayList<String> imageItems;
    private Context context;
    private int maxContent;
    public GridAlbumAdapter(ArrayList<String> imageItems, Context context,int maxContent) {
        this.imageItems = imageItems;
        this.context = context;
        this.maxContent = maxContent;

    }

    @Override
    public int getCount() {
        if(imageItems.size()==maxContent){
            return  maxContent;
        }
        return imageItems.size()+1;
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
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder ;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_published_grida, null);
            new ViewHolder(convertView);
        }
        holder = (ViewHolder) convertView.getTag();
        if (position < imageItems.size()) {
            holder.clv_delete.setVisibility(View.VISIBLE);
            getBitmapData(holder.image,imageItems.get(position));
        } else {
            ImageCache.imageLoader("drawable://"+R.drawable.ic_unfocused,holder.image);
//            holder.image.setImageResource(R.mipmap.ic_unfocused);
            holder.clv_delete.setVisibility(View.GONE);
            holder.image.setVisibility(View.VISIBLE);

            if (position >= maxContent) {
                holder.image.setVisibility(View.GONE);
            }
            G.log("xxxxxxxxxxxxxxxx");
        }
        holder.clv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageItems.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    private  class ViewHolder {
        public ImageView image;
        public ImageView clv_delete;

        public ViewHolder(View view) {
            image = (ImageView) view.findViewById(R.id.item_grida_image);
            clv_delete= (ImageView) view.findViewById(R.id.clv_delete);
            clv_delete.setImageResource(R.mipmap.ic_delete_photo);
            view.setTag(this);
        }
    }

    private void getBitmapData(ImageView imageView ,String imagePath){
        ImageCache.imageLoader("file://"+imagePath,imageView);
    }

}
