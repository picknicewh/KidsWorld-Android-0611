package net.hongzhang.user.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.hongzhang.baselibrary.image.ImageCache;
import net.hongzhang.baselibrary.mode.ResourceVo;
import net.hongzhang.user.R;
import net.hongzhang.user.mode.CompilationsVo;

import java.util.List;

/**
 * ================================================
 * 作    者：wh
 * 时    间：2016/11/28
 * 描    述：我的收藏和播放记录列表的适配器，根据获取的列表数据的判断来源
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class ResourceAdapter extends RecyclerView.Adapter<ResourceAdapter.ViewHolder> {
    /**
     * 文本
     */
    private Context context;
    /**
     * 专辑类列表
     */
    private List<CompilationsVo> compilationsVos;
    /**
     * 资源列表
     */
    private List<ResourceVo> resourceManagerVos;
    /**
     * 来源
     * source =0 我的收藏
     * source =1 播放记录
     */
    private int source;
    private  onItemClickListener itemClickListener;
    public ResourceAdapter(Context context, List<CompilationsVo> compilationsVos, List<ResourceVo> resourceManagerVos) {
        this.context = context;
        this.compilationsVos = compilationsVos;
        this.resourceManagerVos = resourceManagerVos;
        if (compilationsVos != null && resourceManagerVos == null) {
            source = 0;
        } else if (resourceManagerVos != null && compilationsVos == null) {
            source = 1;
        }
    }

    /*  @Override
      public int getCount() {
          return  source==0?compilationsVos.size():resourceManagerVos.size();
      }

      @Override
      public Object getItem(int i) {
          return i;
      }
  */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_resource_list, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (source == 0) {
            CompilationsVo compilationVo = compilationsVos.get(position);
            ImageCache.imageLoader(compilationVo.getImageUrl(), holder.iv_image);
            holder.tv_title.setText(compilationVo.getAlbumName());
        } else {
            ResourceVo resourceVo = resourceManagerVos.get(position);
            ImageCache.imageLoader(resourceVo.getImageUrl(), holder.iv_image);
            holder.tv_title.setText(resourceVo.getResourceName());
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener!=null){
                    itemClickListener.OnItemClick(view,position);
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
        return source == 0 ? compilationsVos.size() : resourceManagerVos.size();
    }

    /*@Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_resource_list, null);
            new ViewHolder(view);
        }
        holder = (ViewHolder) view.getTag();
        if (source == 0) {
            CompilationsVo compilationVo = compilationsVos.get(i);
            ImageCache.imageLoader(compilationVo.getImageUrl(), holder.iv_image);
            holder.tv_title.setText(compilationVo.getAlbumName());
        } else {
            ResourceVo resourceVo = resourceManagerVos.get(i);
            ImageCache.imageLoader(resourceVo.getImageUrl(), holder.iv_image);
            holder.tv_title.setText(resourceVo.getResourceName());
        }
        return view;
    }
*/
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_image;
        private TextView tv_title;

        public ViewHolder(View view) {
            super(view);
            iv_image = (ImageView) view.findViewById(R.id.iv_image);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
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
