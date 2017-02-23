package net.hongzhang.discovery.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import net.hongzhang.baselibrary.image.ImageCache;
import net.hongzhang.baselibrary.mode.ResourceVo;
import net.hongzhang.discovery.R;
import net.hongzhang.discovery.util.TextUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class VideoAlbumDetailAdapter extends RecyclerView.Adapter<VideoAlbumDetailAdapter.ViewHolder>{
    private Activity context;
    private List<ResourceVo> resourceVos;
    private onItemClickListener itemClickListener = null;
    private int currentPosition=0;
    private Map<Integer,Integer> clicks;

    public VideoAlbumDetailAdapter(Activity context, List<ResourceVo> resourceVos) {
        this.context = context;
        this.resourceVos = resourceVos;
        setClicks();
    }
    private void setClicks(){
        clicks = new HashMap<>();
        for (int i = 0 ; i<resourceVos.size();i++){
            if (i==currentPosition){
                clicks.put(i,1);
            }else {
                clicks.put(i,0);
            }
        }
    }
    @Override
    public VideoAlbumDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                context).inflate(R.layout.item_video_alubm_detail,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }
    @Override
    public void onBindViewHolder(final VideoAlbumDetailAdapter.ViewHolder holder, final int position) {
        ResourceVo resourceVo  = resourceVos.get(position);
        ImageCache.imageLoader(TextUtil.encodeChineseUrl(resourceVo.getImageUrl()), holder.iv_image);
        if (clicks.get(position)==1){
            holder.ll_alumb.setBackgroundResource(R.drawable.item_selected_bg);
        }else {
            holder.ll_alumb.setBackgroundColor(context.getResources().getColor(R.color.transparent));
        }
        holder.ll_alumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener!=null){
                    itemClickListener.OnItemClick(view, position);
                }
            }
        });

    }
    public  void setCurrentPosition(int position){
        clicks.put(currentPosition,0);
        currentPosition = position;
        clicks.put(position,1);
        notifyDataSetChanged();
   }
    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return resourceVos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_image;
        private LinearLayout ll_alumb;
        public ViewHolder(View view) {
            super(view);
            iv_image = (ImageView) view.findViewById(R.id.iv_image);
            ll_alumb = (LinearLayout)view.findViewById(R.id.ll_alumb);
            view.setTag(this);
        }
    }
    public  void setOnItemClickListener(onItemClickListener itemClickListener){
     this.itemClickListener = itemClickListener;
    }
    public  interface onItemClickListener{
        void OnItemClick(View view, int position);
    }
}
