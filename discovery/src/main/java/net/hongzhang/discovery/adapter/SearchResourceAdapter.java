package net.hongzhang.discovery.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.hongzhang.baselibrary.image.ImageCache;
import net.hongzhang.baselibrary.mode.ResourceVo;
import net.hongzhang.discovery.R;
import net.hongzhang.discovery.util.TextUtil;

import java.util.List;

/**
 * ================================================
 * 作    者：wh
 * 时    间：2016/11/28
 * 描    述：
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class SearchResourceAdapter extends RecyclerView.Adapter<SearchResourceAdapter.ViewHolder> {
    private Context context;
    private List<ResourceVo> resourceVoList;
    private onItemClickListener itemClickListener = null;

    public SearchResourceAdapter(Context context, List<ResourceVo> resourceVoList) {
        this.context = context;
        this.resourceVoList = resourceVoList;
    }

    @Override
    public SearchResourceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                context).inflate(R.layout.item_resource_search_list, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(final SearchResourceAdapter.ViewHolder holder, final int position) {
        ResourceVo resourceVo = resourceVoList.get(position);
        ImageCache.imageLoader(TextUtil.encodeChineseUrl(resourceVo.getImageUrl()), holder.iv_image);
        holder.tv_title.setText(resourceVo.getResourceName());
        holder.ll_alumb.setOnClickListener(new View.OnClickListener() {
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
        return resourceVoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_image;
        private TextView tv_title;
        private LinearLayout ll_alumb;

        public ViewHolder(View view) {
            super(view);
            iv_image = (ImageView) view.findViewById(R.id.iv_image);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            ll_alumb = (LinearLayout) view.findViewById(R.id.ll_alumb);
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
