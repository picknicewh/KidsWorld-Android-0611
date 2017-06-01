package net.hongzhang.school.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import net.hongzhang.baselibrary.image.ImageCache;
import net.hongzhang.school.R;
import net.hongzhang.school.activity.SubmitTaskActivityS;

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
public class PhotoVideoAlbumAdapter extends BaseAdapter {
    private ArrayList<String> imageItems;
    private Activity context;
    private int maxContent;

    private boolean hasVideo = false;

    public PhotoVideoAlbumAdapter(ArrayList<String> imageItems, Activity context, int maxContent) {
        this.imageItems = imageItems;
        this.context = context;
        this.maxContent = maxContent;
    }

    @Override
    public int getCount() {
        if (imageItems.size() == maxContent) {
            return maxContent;
        }
        return imageItems.size() + 1;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_published_grida, null);
            new ViewHolder(convertView);
        }
        holder = (ViewHolder) convertView.getTag();
        if (hasVideo && position == imageItems.size() - 1) {
            holder.iv_play.setVisibility(View.VISIBLE);
        } else {
            holder.iv_play.setVisibility(View.GONE);
        }
        if (position < imageItems.size()) {
            holder.clv_delete.setVisibility(View.VISIBLE);
            getBitmapData(holder.image, imageItems.get(position));

        } else {
            holder.clv_delete.setVisibility(View.GONE);
            holder.image.setVisibility(View.VISIBLE);
            Glide.with(context).load(R.mipmap.ic_camera_s).into(holder.image);
            if (position >= maxContent) {
                holder.image.setVisibility(View.GONE);
            }
        }
        holder.clv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof SubmitTaskActivityS) {
                    //删除后
                    SubmitTaskActivityS taskActivityS = (SubmitTaskActivityS) context;
                    if (position == imageItems.size() - 1) {
                        hasVideo = false;
                        taskActivityS.setHasVideo(false);
                    }
                }
                imageItems.remove(position);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    private class ViewHolder {
        public ImageView image;
        public ImageView clv_delete;
        public ImageView iv_play;

        public ViewHolder(View view) {
            image = (ImageView) view.findViewById(R.id.item_grida_image);
            clv_delete = (ImageView) view.findViewById(R.id.clv_delete);
            iv_play = (ImageView) view.findViewById(R.id.iv_play_icon);
            clv_delete.setImageResource(R.mipmap.ic_delete_photo);
            view.setTag(this);
        }
    }

    private void getBitmapData(ImageView imageView, String imagePath) {
        ImageCache.imageLoader("file://" + imagePath, imageView);
    }
    /**
     * 设置视频
     */
    public void setHasVideo(boolean hasVideo) {
        this.hasVideo = hasVideo;
        notifyDataSetChanged();
    }
}
