package net.hongzhang.discovery.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.hongzhang.baselibrary.mode.ResourceVo;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.discovery.R;

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
public class AlbumChooseAdapter extends RecyclerView.Adapter<AlbumChooseAdapter.ViewHolder>{
    private Context context;
    private List<ResourceVo> resourceVos;
    private onItemClickListener itemClickListener = null;
    public AlbumChooseAdapter(Context context, List<ResourceVo> resourceVos) {
        this.context = context;
        this.resourceVos = resourceVos;
    }
    public AlbumChooseAdapter(Context context) {
        this.context = context;
    }
    @Override
    public AlbumChooseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                context).inflate(R.layout.item_alubm_choose,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }
    @Override
    public void onBindViewHolder(final AlbumChooseAdapter.ViewHolder holder, final int position) {
       /* ResourceVo resourceVo  = resourceVos.get(position);
        ImageCache.imageLoader(TextUtil.encodeChineseUrl(resourceVo.getImageUrl()), holder.iv_image);
        holder.tv_title.setText("第"+(position+1)+"集");*/
        holder.ll_alumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener!=null){
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
        return 8;
    }




    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_image;
        private TextView tv_title;
        private LinearLayout ll_alumb;
        public ViewHolder(View view) {
            super(view);
            iv_image = (ImageView) view.findViewById(R.id.iv_image);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            ll_alumb = (LinearLayout)view.findViewById(R.id.ll_alumb);
            int itemWidth = (G.size.W-G.dp2px(context,100))/4;
            LinearLayout.LayoutParams  lps= new LinearLayout.LayoutParams(itemWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            ll_alumb.setLayoutParams(lps);
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
