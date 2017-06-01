package net.hongzhang.school.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import net.hongzhang.baselibrary.image.ImageCache;
import net.hongzhang.baselibrary.takevideo.PreviewVideoActivity;
import net.hongzhang.baselibrary.takevideo.VideoUtil;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.school.R;
import net.hongzhang.user.util.PublishPhotoUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： wanghua
 * 时间： 2017/3/31
 * 名称：图片列表显示
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class TaskPhotoListAdapter extends RecyclerView.Adapter<TaskPhotoListAdapter.ViewHolder> {
    private Activity context;
    private List<String> imageList;

    public TaskPhotoListAdapter(Activity context, List<String> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @Override
    public TaskPhotoListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                context).inflate(R.layout.item_task_photo_list, parent, false);
        TaskPhotoListAdapter.ViewHolder holder = new TaskPhotoListAdapter.ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(final TaskPhotoListAdapter.ViewHolder holder, final int position) {
        RelativeLayout.LayoutParams lp;
        int margin = G.dp2px(context, 5);
        if (imageList.size() == 1) {
            lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, G.dp2px(context, 200));
        } else if (imageList.size() == 2) {
            lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, G.dp2px(context, 200));
            lp.setMargins(0, 0, margin, margin);
        } else {
            lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, G.dp2px(context, 100));
            lp.setMargins(0, 0, margin, margin);
        }
        holder.iv_photo.setLayoutParams(lp);
        final String path = imageList.get(imageList.size() - 1);
        if (position == imageList.size() - 1 && path.contains(".mp4") || path.contains(".mov")) {
            holder.iv_play.setVisibility(View.VISIBLE);
            ImageCache.imageLoader(VideoUtil.getFirstFrame(imageList.get(position)), holder.iv_photo);
        } else {
            holder.iv_play.setVisibility(View.GONE);
            ImageCache.imageLoader(imageList.get(position), holder.iv_photo);
        }
        holder.iv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position == imageList.size() - 1 && path.contains(".mp4") || path.contains(".mov")) {
                    Intent i = new Intent(context, PreviewVideoActivity.class);
                    i.putExtra("path", imageList.get(position));
                    context.startActivityForResult(i,1);
                } else {
                    PublishPhotoUtil.imageBrowernet(position, getimagepath(imageList), context);
                }

            }
        });
    }
    /**
     * 把缩略图转换成为原图
     */
    private ArrayList<String> getimagepath(List<String> itemList) {
        ArrayList<String> imagepath = new ArrayList<>();
        for (int i = 0; i < itemList.size(); i++) {
            String pathurl = itemList.get(i);
            pathurl = pathurl.replace("/s", "");
            imagepath.add(pathurl);
        }
        return imagepath;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_photo;
        private ImageView iv_play;

        public ViewHolder(View view) {
            super(view);
            iv_photo = (ImageView) view.findViewById(R.id.iv_photo);
            iv_play = (ImageView) view.findViewById(R.id.iv_play);
            view.setTag(this);
        }
    }

}