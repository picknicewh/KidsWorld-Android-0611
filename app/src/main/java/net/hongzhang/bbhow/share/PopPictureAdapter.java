package net.hongzhang.bbhow.share;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import net.hongzhang.baselibrary.image.ImageCache;
import net.hongzhang.bbhow.R;

import java.util.List;

/**
 * ================================================
 * 作    者：wh
 * 时    间：2017/03/27
 * 描    述：
 * 版    本：分享弹框中图片或
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class PopPictureAdapter extends RecyclerView.Adapter<PopPictureAdapter.ViewHolder> {
    private Context context;
    private List<String> list;
    private onItemClickListener itemClickListener = null;

    public PopPictureAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public PopPictureAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_pop_picture, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(final PopPictureAdapter.ViewHolder holder, final int position) {
        String path = list.get(position);
        ImageCache.imageLoader(path,holder.iv_image);
        holder.ll_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener != null) {
                    itemClickListener.OnItemClick(view, position);
                }
            }
        });
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_image;
        private LinearLayout ll_pic;

        public ViewHolder(View view) {
            super(view);
            iv_image = (ImageView) view.findViewById(R.id.iv_image);
            ll_pic = (LinearLayout) view.findViewById(R.id.ll_pic);
            view.setTag(this);
        }
    }
    public void setOnItemClickListener(onItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    public interface onItemClickListener {
        void OnItemClick(View view, int position);
    }
}
