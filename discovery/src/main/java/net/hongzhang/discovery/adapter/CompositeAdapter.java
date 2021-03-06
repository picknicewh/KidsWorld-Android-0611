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
import net.hongzhang.discovery.modle.CompilationVo;
import net.hongzhang.discovery.util.TextUtil;

import java.util.List;

/**
 * 作者： wanghua
 * 时间： 2017/3/14
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */


public class CompositeAdapter extends RecyclerView.Adapter<CompositeAdapter.ViewHolder> {
    private Context context;
    private List<CompilationVo> compilationVos;
    private List<ResourceVo> resourceVoList;
    private int com_count;
    private int res_count;
    private CompilationAdapter.onItemClickListener itemClickListener = null;

    public CompositeAdapter(Context context, List<CompilationVo> compilationVos, List<ResourceVo> resourceVoList) {
        this.context = context;
        this.compilationVos = compilationVos;
        this.resourceVoList = resourceVoList;
        com_count = compilationVos.size();
        res_count = resourceVoList.size();
    }

    @Override
    public CompositeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                context).inflate(R.layout.item_resource_search_list, parent, false);
        CompositeAdapter.ViewHolder holder = new CompositeAdapter.ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(final CompositeAdapter.ViewHolder holder, final int position) {
        if (position < com_count - 1) {
            CompilationVo compilationVo = compilationVos.get(position);
            ImageCache.imageLoader(TextUtil.encodeChineseUrl(compilationVo.getImageUrl()), holder.iv_image);
            holder.tv_title.setText(compilationVo.getAlbumName());
        } else {
            ResourceVo resourceVo = resourceVoList.get(position);
            ImageCache.imageLoader(TextUtil.encodeChineseUrl(resourceVo.getImageUrl()), holder.iv_image);
            holder.tv_title.setText(resourceVo.getResourceName());
        }
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
        return res_count + com_count;
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

    public void setOnItemClickListener(CompilationAdapter.onItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface onItemClickListener {
        void OnItemClick(View view, int position);
    }
}
