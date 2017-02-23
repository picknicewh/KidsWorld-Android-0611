package net.hongzhang.discovery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.hongzhang.baselibrary.image.ImageCache;
import net.hongzhang.discovery.R;
import net.hongzhang.discovery.modle.RecommendVo;


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
public class SelectAlbumAdapter extends BaseAdapter {
    private Context context;
    private List<RecommendVo> recommendVos;
    public SelectAlbumAdapter(Context context, List<RecommendVo> recommendVos){
        this.context = context;
        this.recommendVos = recommendVos;
    }
    @Override
    public int getCount() {
        return recommendVos.size();
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
            view= LayoutInflater.from(context).inflate(R.layout.item_select_alubm,null);
            new ViewHolder(view);
        }
        holder= (ViewHolder) view.getTag();
        RecommendVo recommendVo = recommendVos.get(i);
        ImageCache.imageLoader(recommendVo.getImgUrl(),holder.iv_image);
        holder.tv_album_name.setText(recommendVo.getName());
      //  holder.tv_count.setText(String.valueOf(recommendVo.getIsrecommend()));
        return view;
    }

    class ViewHolder {
        public ImageView iv_image;
        private TextView tv_album_name;
        private TextView tv_count;
        public ViewHolder(View view) {
            iv_image = (ImageView) view.findViewById(R.id.iv_album_image);
            tv_album_name = (TextView)view.findViewById(R.id.tv_album_name);
            tv_count = (TextView)view.findViewById(R.id.tv_album_count);
            view.setTag(this);
        }
    }
}
