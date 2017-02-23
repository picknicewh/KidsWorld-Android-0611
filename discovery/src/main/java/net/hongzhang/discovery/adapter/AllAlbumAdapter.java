package net.hongzhang.discovery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import net.hongzhang.baselibrary.image.ImageCache;
import net.hongzhang.discovery.R;
import net.hongzhang.discovery.modle.RecommendVo;

import java.util.List;

/**
 * Created by wanghua on 2016/12/21.
 */
public class AllAlbumAdapter extends BaseAdapter {
    private Context context;
    private List<RecommendVo> recommendVos;

    public AllAlbumAdapter(Context context, List<RecommendVo> recommendVos) {
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
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_all_album, null);
            new ViewHolder(view);
        }
        holder = (ViewHolder) view.getTag();
        RecommendVo recommendVo = recommendVos.get(i);
        ImageCache.imageLoader(recommendVo.getImgUrl(), holder.iv_album_image);
        return view;
    }

    class ViewHolder {
        private ImageView iv_album_image;

        public ViewHolder(View view) {
            iv_album_image = (ImageView) view.findViewById(R.id.iv_album_image);
            view.setTag(this);
        }
    }
}
