package net.hunme.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.hunme.baselibrary.image.ImageCache;
import net.hunme.baselibrary.mode.ResourceVo;
import net.hunme.user.R;
import net.hunme.user.mode.CompilationsVo;

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
public class ResourceAdapter extends BaseAdapter {
    private Context context;
    private List<CompilationsVo> compilationsVos;
    private List<ResourceVo> resourceManagerVos;
    private int source;
    public ResourceAdapter(Context context, List<CompilationsVo> compilationsVos, List<ResourceVo> resourceManagerVos) {
        this.context = context;
        this.compilationsVos = compilationsVos;
        this.resourceManagerVos = resourceManagerVos;
        if (compilationsVos!=null && resourceManagerVos==null){
            source=0;
        }else if (resourceManagerVos!=null && compilationsVos==null){
            source=1;
        }
    }
    @Override
    public int getCount() {
        return  source==0?compilationsVos.size():resourceManagerVos.size();
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view==null){
            view= LayoutInflater.from(context).inflate(R.layout.item_resource_list,null);
            new ViewHolder(view);
        }
        holder= (ViewHolder) view.getTag();
        if (source==0){
            CompilationsVo compilationVo = compilationsVos.get(i);
            ImageCache.imageLoader(compilationVo.getImageUrl(),holder.iv_image);
            holder.tv_title.setText(compilationVo.getAlbumName());
        }else {
            ResourceVo resourceVo  = resourceManagerVos.get(i);
            ImageCache.imageLoader(resourceVo.getImageUrl(),holder.iv_image);
            holder.tv_title.setText(resourceVo.getResourceName());
        }
        return view;
    }

    class ViewHolder {
        public ImageView iv_image;
        private TextView tv_title;
        public ViewHolder(View view) {
            iv_image = (ImageView) view.findViewById(R.id.iv_image);
            tv_title = (TextView)view.findViewById(R.id.tv_title);
            view.setTag(this);
        }
    }
}
