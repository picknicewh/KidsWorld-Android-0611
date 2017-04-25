package net.hongzhang.user.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import net.hongzhang.baselibrary.image.ImageCache;
import net.hongzhang.baselibrary.util.FileUtils;
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
    private boolean isRecord;

    public GridAlbumAdapter(ArrayList<String> imageItems, Context context, int maxContent) {
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
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_published_grida, null);
            new ViewHolder(convertView);
        }
        holder = (ViewHolder) convertView.getTag();
        if (position < imageItems.size()) {
            holder.clv_delete.setVisibility(View.VISIBLE);
            holder.iv_play_icon.setVisibility(isRecord ? View.VISIBLE : View.GONE);

            if (isRecord) {
                getBitmapData(holder.image, getVideoFirstFrame(imageItems.get(position)));
            } else {
                getBitmapData(holder.image, imageItems.get(position));
            }
        } else {
            holder.clv_delete.setVisibility(View.GONE);
            holder.image.setVisibility(View.VISIBLE);
            Glide.with(context).load(R.drawable.ic_unfocused).into(holder.image);
            if (position >= maxContent) {
                holder.image.setVisibility(View.GONE);
            }
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

    private class ViewHolder {
        public ImageView image;
        public ImageView clv_delete;
        public ImageView iv_play_icon;

        public ViewHolder(View view) {
            image = (ImageView) view.findViewById(R.id.item_grida_image);
            clv_delete = (ImageView) view.findViewById(R.id.clv_delete);
            iv_play_icon = (ImageView) view.findViewById(R.id.clv_delete);
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
    public void setVideo(boolean isRecord, int maxContent) {
        this.maxContent = maxContent;
        this.isRecord = isRecord;
        notifyDataSetChanged();
    }
    /**
     * 是不是视频，且是视频的第一帧
     * @param  videoPath 视频路径
     */
    private String getVideoFirstFrame(String videoPath) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(videoPath);
        Bitmap bitmap = media.getFrameAtTime();
        byte[] data = G.Bitmap2Bytes(bitmap);
        String path = FileUtils.getUploadVideoFile(context);
        FileUtils.savePhoto(path, data, false);
        media.release();
        return path;
    }
}
