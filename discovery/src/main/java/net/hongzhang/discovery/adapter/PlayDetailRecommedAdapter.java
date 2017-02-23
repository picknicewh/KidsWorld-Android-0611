package net.hongzhang.discovery.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.hongzhang.baselibrary.image.ImageCache;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.discovery.R;
import net.hongzhang.discovery.modle.CompilationVo;
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
public class PlayDetailRecommedAdapter extends RecyclerView.Adapter<PlayDetailRecommedAdapter.ViewHolder>{
    private Context context;
    private List<CompilationVo> compilationVos;
    private onItemClickListener itemClickListener = null;
    public PlayDetailRecommedAdapter(Context context,List<CompilationVo> compilationVos) {
        this.context = context;
        this.compilationVos = compilationVos;
    }
    @Override
    public PlayDetailRecommedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                context).inflate(R.layout.item_video_detail_recommed,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }
    @Override
    public void onBindViewHolder(final PlayDetailRecommedAdapter.ViewHolder holder, final int position) {
        CompilationVo compilationVo  = compilationVos.get(position);
        ImageCache.imageLoader(TextUtil.encodeChineseUrl(compilationVo.getImageUrl()), holder.iv_image);
        holder.tv_title.setText(compilationVo.getAlbumName());
        holder.tv_play_count.setText("已有"+String.valueOf(compilationVo.getPvcount())+"人播放");
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
        return compilationVos.size();
    }




    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_image;
        private TextView tv_title;
        private TextView tv_play_count;
        private LinearLayout ll_alumb;
        public ViewHolder(View view) {
            super(view);
            iv_image = (ImageView) view.findViewById(R.id.iv_image);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_play_count = (TextView) view.findViewById(R.id.tv_play_count);
            ll_alumb = (LinearLayout)view.findViewById(R.id.ll_alumb);
            int itemWidth = (G.size.W-G.dp2px(context,40))/2;
            LinearLayout.LayoutParams  lps= new LinearLayout.LayoutParams(itemWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            int margin = G.dp2px(context,5);
            lps.setMargins(margin,margin,margin,margin);
            lps.gravity = Gravity.CENTER;
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
